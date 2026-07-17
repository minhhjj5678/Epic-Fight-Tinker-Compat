package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.CombatProfile;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.CombatProfiles;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfile;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfiles;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ModifiableArrowItem;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import net.minecraft.sounds.SoundEvent;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;


public class TCWeaponCapability extends CapabilityItem {
    protected CombatProfile defaultWeaponSet;
    protected Map<Style, CombatProfile> weaponSets;
    protected List<ModifierProfile> modifierProfiles;
    protected Function<LivingEntityPatch<?>, Style> styleProvider;

    // copied from WeaponCapability
    protected SoundEvent smashingSound;
    protected SoundEvent hitSound;
    protected HitParticleType hitParticle;
    protected final boolean canBePlacedOffhand;
    protected final Function<Style, Boolean> comboCancel;
    protected final ComboCounterHandleEvent.ComboCounterHandler comboCounterHandler;
    protected final CapabilityItem.ZoomInType zoomInType;
    protected final float reach;
    private static final ModifierId THROWING_ID = new ModifierId(TConstruct.MOD_ID, "throwing");
    private static final ModifierId BLOCKING_ID = new ModifierId(TConstruct.MOD_ID, "blocking");

    private final WeakHashMap<ItemStack, CachedProfile> profileCache = new WeakHashMap<>();

    private static class CachedProfile {
        final CompoundTag nbtHash;
        final ModifierProfile modifierProfile;

        CachedProfile(CompoundTag nbtHash, ModifierProfile modifierProfile) {
            this.nbtHash = nbtHash;
            this.modifierProfile = modifierProfile;
        }
    }

    protected TCWeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        Builder tcBuilder = (Builder)builder;
        this.defaultWeaponSet = tcBuilder.defaultWeaponSet;
        this.modifierProfiles = tcBuilder.modifierProfiles;
        this.weaponSets = tcBuilder.weaponSets;
        this.styleProvider = tcBuilder.styleProvider;

        // assign copied weapon-capability fields
        this.smashingSound = tcBuilder.swingSound;
        this.hitSound = tcBuilder.hitSound;
        this.hitParticle = tcBuilder.hitParticle;
        this.canBePlacedOffhand = tcBuilder.canBePlacedOffhand;
        this.comboCancel = tcBuilder.comboCancel;
        this.comboCounterHandler = tcBuilder.comboCounterHandler;
        this.zoomInType = tcBuilder.zoomInType;
        this.reach = tcBuilder.reach;
        sortModifierProfiles();
    }

    public CombatProfile getCurrentCP(LivingEntityPatch<?> patch) {
        ItemStack stack = patch.getOriginal().getMainHandItem();
        if (stack.isEmpty()) {
            return this.defaultWeaponSet != null ? this.defaultWeaponSet : this.weaponSets.get(Styles.COMMON);
        }

        CompoundTag stackNBT = stack.getTag();
        CachedProfile cached = profileCache.get(stack);
        if (cached != null && Objects.equals(cached.nbtHash, stackNBT)) {
            if (cached.modifierProfile != null && cached.modifierProfile.styleProvider() != null) {
                CombatProfile CP = cached.modifierProfile.weaponSets().get(cached.modifierProfile.styleProvider().apply(patch));
                if (CP != null) {
                    return CP;
                }
            }
            return this.weaponSets.getOrDefault(this.styleProvider.apply(patch), this.defaultWeaponSet != null ? this.defaultWeaponSet : this.weaponSets.get(Styles.COMMON));
        }

        CombatProfile combatProfile = null;
        ModifierProfile cachedMP = null;
        ModifierProfile modifierProfile = this.getModifierProfile(patch, InteractionHand.MAIN_HAND);
        if (modifierProfile != null) {
            Style resolvedStyle = modifierProfile.styleProvider().apply(patch);
            CombatProfile moveSet = modifierProfile.weaponSets().get(resolvedStyle);
            if (moveSet != null) {
                combatProfile = moveSet;
            }
            cachedMP = modifierProfile;
        }

        if (combatProfile == null) {
            Style style = this.styleProvider.apply(patch);
            if (style != Styles.COMMON) {
                CombatProfile styleMoveSet = this.weaponSets.get(style);
                if (styleMoveSet != null) {
                    combatProfile = styleMoveSet;
                }
            }
        }

        combatProfile = (combatProfile == null) ? this.defaultWeaponSet != null ? this.defaultWeaponSet : this.weaponSets.get(Styles.COMMON) : combatProfile;
        profileCache.put(stack, new CachedProfile(stackNBT == null ? null : stackNBT.copy(), cachedMP));
        return combatProfile;
    }

    public Style getStyle(LivingEntityPatch<?> patch) {
        ModifierProfile modifierProfile = this.getModifierProfile(patch, InteractionHand.MAIN_HAND);
        if (modifierProfile != null && modifierProfile.styleProvider() != null) {
            Style modifierStyle = modifierProfile.styleProvider().apply(patch);
            if (modifierStyle != null) {
                return modifierStyle;
            }
        }

        if (this.styleProvider != null) {
            Style baseStyle = this.styleProvider.apply(patch);
            if (baseStyle != null) {
                return baseStyle;
            }
        }

        return Styles.COMMON;
    }

    @Override
    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getAutoAttackMotion(PlayerPatch<?> playerpatch) {
        return this.getCurrentCP(playerpatch).attackMotions();
    }

    @Override
    public SoundEvent getSmashingSound() {
        return this.smashingSound == null ? super.getSmashingSound() : this.smashingSound;
    }

    @Override
    public SoundEvent getHitSound() {
        return this.hitSound == null ? super.getHitSound() : this.hitSound;
    }

    @Override
    public HitParticleType getHitParticle() {
        return this.hitParticle == null ? super.getHitParticle() : this.hitParticle;
    }

    @SuppressWarnings("removal")
    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getMountAttackMotion() {
        return this.defaultWeaponSet.mountAttackMotions();
    }

    public Skill getInnateSkill(PlayerPatch<?> playerpatch, ItemStack itemstack) {
        if (itemstack.getItem() instanceof IModifiableDisplay) {
            ToolStack tool = ToolStack.from(itemstack);
            for (ModifierProfile modifierProfile : this.modifierProfiles) {
                if (tool.getModifierLevel(modifierProfile.modifierId()) > 0 && modifierProfile.innateSkill() != null) {
                    return modifierProfile.innateSkill();
                }
            }
        }

        BiFunction<ItemStack, PlayerPatch<?>, Skill> innateSkillFunction = this.getCurrentCP(playerpatch).innateSkill();
        return innateSkillFunction == null ? null : innateSkillFunction.apply(itemstack, playerpatch);
    }

    public WeaponCategory getWeaponCategory(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        ModifierProfile modifierProfile = this.getModifierProfile(entityPatch, hand);
        if (modifierProfile != null && modifierProfile.weaponCategory() != null) {
            return modifierProfile.weaponCategory();
        }
        return super.getWeaponCategory();
    }

    @Override
    public Collider getWeaponCollider() {
        ModifierProfile modifierProfile = this.getModifierProfile();
        if (modifierProfile != null && modifierProfile.collider() != null) {
            return modifierProfile.collider();
        }
        return super.getWeaponCollider();
    }

    @Override
    public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifier(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        CombatProfile set = getCurrentCP(entityPatch);

        if (set == null) {
            return Collections.emptyMap();
        }
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> result = new HashMap<>();

        for (Map.Entry<LivingMotion, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>> entry : set.livingMotions().entrySet()) {
            List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> animations = entry.getValue();
            if (animations != null && !animations.isEmpty()) {
                result.put(entry.getKey(), animations.get(0));
            }
        }

        ToolStack toolStack = getToolStack(entityPatch, hand);
        if (entityPatch.getOriginal().isCrouching() && result.containsKey(LivingMotions.BLOCK) && toolStack != null && toolStack.getModifierLevel(Objects.requireNonNull(THROWING_ID, "throwing id")) > 0) {
            result.remove(LivingMotions.BLOCK);
        }

        return result;
    }

    @Override
    public boolean checkOffhandValid(LivingEntityPatch<?> entityPatch) {
        return super.checkOffhandValid(entityPatch) || this.getCurrentCP(entityPatch).visibleOffhand();
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        InteractionHand checkedHand = Objects.requireNonNull(hand, "hand");
        CombatProfile set = getCurrentCP(entityPatch);

        if (set != null && set.motionPredicate() != null && entityPatch instanceof PlayerPatch<?> playerPatch) {
            LivingMotion motion = set.motionPredicate().apply(playerPatch, checkedHand);
            if (motion != null) {
                return motion;
            }
        }

        if (!entityPatch.getOriginal().isUsingItem()) return null;

        if (entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.DRINK || entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.EAT || entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.BLOCK) return null;

        return LivingMotions.AIM;
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getGuardMotion(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch) {
        ToolStack toolStack = getToolStack(playerpatch, null);
        if (toolStack == null || toolStack.getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) <= 0) {
            return null;
        }

        CombatProfile set = getCurrentCP(playerpatch);
        if (set == null) {
            return null;
        }

        List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> motions = set.guardMotions().get(blockType);
        if (motions == null || motions.isEmpty()) {
            return null;
        }

        return motions.get(0);
    }

    @Override
    public UseAnim getUseAnimation(LivingEntityPatch<?> entityPatch) {
        CombatProfile set = getCurrentCP(entityPatch);
        ToolStack toolStack = getToolStack(entityPatch, null);
        if (set != null && set.livingMotions().containsKey(LivingMotions.BLOCK) && toolStack != null && toolStack.getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) > 0) {
            return UseAnim.BLOCK;
        }
        return UseAnim.NONE;
    }

    @Override
    public boolean canBePlacedOffhand() {
        return this.canBePlacedOffhand;
    }

    @Override
    public CapabilityItem.ZoomInType getZoomInType() {
        return this.zoomInType == ZoomInType.NONE ? ZoomInType.AIMING : this.zoomInType;
    }

    @Override
    public float getReach() {
        return this.reach;
    }

    public Skill getPassiveSkill(PlayerPatch<?> playerPatch) {
        CombatProfile set = getCurrentCP(playerPatch);
        if (set != null) {
            return set.passiveSkill();
        }
        return null;
    }

    @SuppressWarnings("removal")
    public Skill getPassiveSkill() {
        CombatProfile set = this.defaultWeaponSet;
        if (set != null) {
            return set.passiveSkill();
        }
        return null;
    }

    @SuppressWarnings("null")
    public ToolStack getToolStack(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        if (entityPatch != null) {
            ItemStack itemStack = entityPatch.getOriginal().getItemInHand(hand != null ? hand : InteractionHand.MAIN_HAND);
            return itemStack.isEmpty() ? null : ToolStack.from(itemStack);
        }
        return null;
    }

    protected ModifierProfile getModifierProfile(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        ToolStack toolStack = getToolStack(entityPatch, hand);
        if (toolStack != null) {
            for (ModifierProfile modifierProfile : this.modifierProfiles) {
                ModifierId modifierId = modifierProfile.modifierId();
                if (modifierId == null || modifierProfile.weaponSets().isEmpty()) {
                    continue;
                }

                int level = toolStack.getModifierLevel(modifierId);
                if (level > 0) {
                    return modifierProfile;
                }
            }
        }
        return null;
    }

    protected ModifierProfile getModifierProfile() {
        return null;
    }

    protected void sortModifierProfiles() {
        this.warnDuplicatePriorities();
        this.modifierProfiles.sort((first, second) -> {
            if (first.modifierId() == null && second.modifierId() == null) {
                return 0;
            }
            if (first.modifierId() == null) {
                return 1;
            }
            if (second.modifierId() == null) {
                return -1;
            }
            return Integer.compare(second.priority(), first.priority());
        });
    }

    protected void warnDuplicatePriorities() {
        Map<Integer, List<ModifierProfile>> groupedByPriority = new HashMap<>();
        for (ModifierProfile modifierProfile : this.modifierProfiles) {
            groupedByPriority.computeIfAbsent(modifierProfile.priority(), key -> new ArrayList<>()).add(modifierProfile);
        }

        for (Map.Entry<Integer, List<ModifierProfile>> entry : groupedByPriority.entrySet()) {
            List<ModifierProfile> profiles = entry.getValue();
            if (profiles.size() < 2) {
                continue;
            }
            String profileList = profiles.stream().map(profile -> String.valueOf(profile.modifierId())).collect(java.util.stream.Collectors.joining(", "));
            com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat.LOGGER.warn("Duplicate TC modifier priority {} detected for profiles: {}", entry.getKey(), profileList);
        }
    }

    public boolean canHoldInOffhandAlone() {
        return this.canBePlacedOffhand;
    }

    @SuppressWarnings("removal")
    public boolean availableOnHorse() {
        return this.defaultWeaponSet != null && !this.defaultWeaponSet.mountAttackMotions().isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends CapabilityItem.Builder {
        Function<LivingEntityPatch<?>, Style> styleProvider;
        SoundEvent swingSound;
        SoundEvent hitSound;
        HitParticleType hitParticle;
        Map<Style, List<AnimationManager.AnimationAccessor<? extends AttackAnimation>>> autoAttackMotionMap;
        Map<Style, Function<ItemStack, Skill>> innateSkillByStyle;
        Function<Style, Boolean> comboCancel;
        ComboCounterHandleEvent.ComboCounterHandler comboCounterHandler;
        boolean canBePlacedOffhand;
        CapabilityItem.ZoomInType zoomInType;
        float reach;

        protected CombatProfile defaultWeaponSet;
        protected List<ModifierProfile> modifierProfiles;
        protected Map<Style, CombatProfile> weaponSets;

        Builder() {
            super();
            this.constructor(TCWeaponCapability::new);
            this.styleProvider = (entityPatch) -> Styles.ONE_HAND;
            this.swingSound = yesman.epicfight.gameasset.EpicFightSounds.WHOOSH.get();
            this.hitSound = yesman.epicfight.gameasset.EpicFightSounds.BLUNT_HIT.get();
            this.hitParticle = yesman.epicfight.particle.EpicFightParticles.HIT_BLADE.get();
            this.autoAttackMotionMap = new HashMap<>();
            this.innateSkillByStyle = new HashMap<>();
            this.canBePlacedOffhand = true;
            this.comboCancel = (style) -> true;
            this.comboCounterHandler = ComboCounterHandleEvent.ComboCounterHandler.DEFAULT_COMBO_HANDLER;
            this.zoomInType = CapabilityItem.ZoomInType.NONE;
            this.reach = 0.2F;

            // TC defaults
            this.modifierProfiles = new ArrayList<>();
            this.weaponSets = new HashMap<>();
            this.weaponSets.put(Styles.COMMON, CombatProfiles.fist().build());
            this.modifierProfiles.addAll(ModifierProfiles.defaultModifierProfiles);
        }

        public Builder styleProvider(Function<LivingEntityPatch<?>, Style> styleProvider) {
            this.styleProvider = styleProvider;
            return this;
        }

        public Builder category(WeaponCategory category) {
            super.category(category);
            return this;
        }

        public Builder swingSound(SoundEvent swingSound) {
            this.swingSound = swingSound;
            return this;
        }

        public Builder hitSound(SoundEvent hitSound) {
            this.hitSound = hitSound;
            return this;
        }

        public Builder hitParticle(HitParticleType hitParticle) {
            this.hitParticle = hitParticle;
            return this;
        }

        @Override
        public Builder collider(Collider collider) {
            super.collider(collider);
            return this;
        }

        public Builder canBePlacedOffhand(boolean canBePlacedOffhand) {
            this.canBePlacedOffhand = canBePlacedOffhand;
            return this;
        }

        public Builder reach(float reach) {
            this.reach = reach;
            return this;
        }

        public Builder addStyleAttibutes(Style style, Pair<Attribute, AttributeModifier> attributePair) {
            super.addStyleAttibutes(style, attributePair);
            return this;
        }

        public Builder zoomInType(CapabilityItem.ZoomInType zoomInType) {
            this.zoomInType = zoomInType;
            return this;
        }

        public Builder defaultMoveSet(CombatProfile.CombatProfileBuilder weaponSet) {
            this.defaultWeaponSet = weaponSet.build();
            return this;
        }

        public Builder addWeaponSet(Styles style, CombatProfile.CombatProfileBuilder set) {
            this.weaponSets.computeIfAbsent(style, (k) -> set.build());
            return this;
        }

        public Builder addModifier(ModifierProfile modifierProfile) {
            if (modifierProfile != null && !this.modifierProfiles.contains(modifierProfile)) {
                this.modifierProfiles.add(modifierProfile);
            }
            return this;
        }
    }
}
