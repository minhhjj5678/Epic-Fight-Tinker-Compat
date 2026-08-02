package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfile;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfileReloadListener;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.*;
import net.minecraft.sounds.SoundEvent;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;

public class TCWeaponCapability extends CapabilityItem {
    protected Function<LivingEntityPatch<?>, Style> styleProvider;
    protected BiFunction<LivingEntityPatch<?>, InteractionHand, LivingMotion> motionPredicator;
    protected Map<Style, Map<GuardSkill.BlockType, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions;
    protected final Map<Style, List<AnimationManager.AnimationAccessor<? extends AttackAnimation>>> autoAttackMotions;
    protected final Map<Style, Function<ItemStack, Skill>> innateSkill;
    protected final Skill passiveSkill;
    protected final Map<Style, Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotionModifiers;
    protected final Function<LivingEntityPatch<?>, Boolean> weaponCombinationPredicator;
    protected SoundEvent smashingSound;
    protected SoundEvent hitSound;
    protected HitParticleType hitParticle;
    protected final boolean canBePlacedOffhand;
    protected final Function<Style, Boolean> comboCancel;
    protected final ComboCounterHandleEvent.ComboCounterHandler comboCounterHandler;
    protected final CapabilityItem.ZoomInType zoomInType;
    protected final float reach;
    protected final ToolStack tool;

    private static final ModifierId BLOCKING_ID = new ModifierId(TConstruct.MOD_ID, "blocking");

    private final Map<ResourceLocation, CapabilityItem> weaponCache = new ConcurrentHashMap<>();

    protected TCWeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        Builder tcBuilder = (Builder)builder;
        this.styleProvider = tcBuilder.styleProvider;
        this.motionPredicator = tcBuilder.motionPredicator;
        this.guardMotions = tcBuilder.guardMotions;
        this.autoAttackMotions = tcBuilder.autoAttackMotionMap;
        this.innateSkill = tcBuilder.innateSkillByStyle;
        this.passiveSkill = tcBuilder.passiveSkill;
        this.livingMotionModifiers = tcBuilder.livingMotionModifiers;
        this.weaponCombinationPredicator = tcBuilder.weaponCombinationPredicator;
        this.smashingSound = tcBuilder.swingSound;
        this.hitSound = tcBuilder.hitSound;
        this.hitParticle = tcBuilder.hitParticle;
        this.canBePlacedOffhand = tcBuilder.canBePlacedOffhand;
        this.comboCancel = tcBuilder.comboCancel;
        this.comboCounterHandler = tcBuilder.comboCounterHandler;
        this.zoomInType = tcBuilder.zoomInType;
        this.reach = tcBuilder.reach;
        this.tool = tcBuilder.tool;
    }

    public Style getStyle(LivingEntityPatch<?> patch) {
        CapabilityItem weapon = this.getWeapon();
        if (weapon != null) {
            Style weaponStyle = weapon.getStyle(patch);
            if (weaponStyle != Styles.COMMON) {
                return weaponStyle;
            }
        }

        if (this.styleProvider != null) {
            return this.styleProvider.apply(patch);
        }

        return Styles.COMMON;
    }

    public CapabilityItem getWeapon() {
        CapabilityItem weapon = null;
        ModifierProfile modifierProfile = this.getModifierProfile(profile -> profile.weaponType() != null && WeaponTypeReloadListener.get(profile.weaponType()) != null);
        if (modifierProfile != null) {
            ResourceLocation rl = modifierProfile.weaponType();
            Item item = this.getToolStack().getItem();
            if (weaponCache.containsKey(rl)) {
                weapon = weaponCache.get(rl);
            } else {
                Function<Item, CapabilityItem.Builder> func = WeaponTypeReloadListener.get(rl);
                weapon = func.apply(item).build();
                weaponCache.put(rl, weapon);
            }
        }
        return weapon;
    }

    @Override
    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getAutoAttackMotion(PlayerPatch<?> playerpatch) {
        CapabilityItem delegatedCapability = this.getWeapon();
        return delegatedCapability != null ? delegatedCapability.getAutoAttackMotion(playerpatch)
                : this.autoAttackMotions.getOrDefault(this.styleProvider.apply(playerpatch), this.autoAttackMotions.get(Styles.COMMON));
    }

    @Override
    public SoundEvent getSmashingSound() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getSmashingSound();
        return this.smashingSound == null ? super.getSmashingSound() : this.smashingSound;
    }

    @Override
    public SoundEvent getHitSound() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getHitSound();
        return this.hitSound == null ? super.getHitSound() : this.hitSound;
    }

    @Override
    public HitParticleType getHitParticle() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getHitParticle();
        return this.hitParticle == null ? super.getHitParticle() : this.hitParticle;
    }

    @SuppressWarnings("removal")
    public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getMountAttackMotion() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getMountAttackMotion();
        return this.autoAttackMotions.get(Styles.MOUNT);
    }

    public Skill getInnateSkill(PlayerPatch<?> playerpatch, ItemStack itemstack) {
        ToolStack tool = this.getToolStack();
        ModifierProfile modifierProfile = this.getModifierProfile(profile -> profile.innateSkill() != null && profile.innateSkill().apply(tool, playerpatch) != null);
        if (modifierProfile != null) {
            return modifierProfile.innateSkill().apply(tool, playerpatch);
        }

        CapabilityItem delegatedCapability = this.getWeapon();
        if (delegatedCapability != null) {
            Skill skill = delegatedCapability.getInnateSkill(playerpatch, itemstack);
            if (skill != null) {
                return skill;
            }
        }

        Function<ItemStack, Skill> innateProvider = this.innateSkill.getOrDefault(this.styleProvider.apply(playerpatch), this.innateSkill.get(Styles.COMMON));
        return innateProvider == null ? null : innateProvider.apply(itemstack);
    }

    @Override
    public WeaponCategory getWeaponCategory() {
        ModifierProfile modifierProfile = this.getModifierProfile(profile -> profile.weaponCategory() != null);
        if (modifierProfile != null) {
            return modifierProfile.weaponCategory();
        }

        CapabilityItem weapon = this.getWeapon();
        if (weapon != null && weapon.getWeaponCategory() != WeaponCategories.FIST) {
            return weapon.getWeaponCategory();
        }

        return this.weaponCategory;
    }

    @Override
    public Collider getWeaponCollider() {
        ToolStack tool = this.getToolStack();
        ModifierProfile modifierProfile = this.getModifierProfile(profile -> profile.collider() != null && profile.collider().apply(tool) != null);
        if (modifierProfile != null) {
            return modifierProfile.collider().apply(tool);
        }

        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) {
            return delegatedCap.getWeaponCollider();
        }

        return this.collider;
    }

    @Override
    public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifier(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        CapabilityItem delegatedCapability = this.getWeapon();
        if (delegatedCapability != null) {
            return delegatedCapability.getLivingMotionModifier(entityPatch, hand);
        }

        if (this.livingMotionModifiers == null) {
            return Collections.emptyMap();
        }

        Style currentStyle = this.styleProvider.apply(entityPatch);
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> styleMotions = this.livingMotionModifiers.get(currentStyle);
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> commonMotions = this.livingMotionModifiers.get(Styles.COMMON);

        if (commonMotions == null || commonMotions.isEmpty()) {
            return styleMotions != null ? styleMotions : Collections.emptyMap();
        }
        if (styleMotions == null || styleMotions.isEmpty()) {
            return commonMotions;
        }

        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> result = new HashMap<>(commonMotions);
        result.putAll(styleMotions);
        return result;
    }

    @Override
    public boolean checkOffhandValid(LivingEntityPatch<?> entityPatch) {
        CapabilityItem delegatedCapability = this.getWeapon();
        if (delegatedCapability != null) {
            return delegatedCapability.checkOffhandValid(entityPatch);
        }

        return super.checkOffhandValid(entityPatch) || this.weaponCombinationPredicator.apply(entityPatch);
    }

    @Override
    public LivingMotion getLivingMotion(LivingEntityPatch<?> entityPatch, InteractionHand hand) {
        CapabilityItem delegatedCapability = this.getWeapon();

        if (delegatedCapability != null) {
            LivingMotion motion = delegatedCapability.getLivingMotion(entityPatch, hand);
            if (motion != null) {
                return motion;
            }
        }

        LivingMotion motion = this.motionPredicator.apply(entityPatch, hand);
        if (motion != null) {
            return motion;
        }

        if (!entityPatch.getOriginal().isUsingItem()) return null;
        if (entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.DRINK || entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.EAT) return null;
        if (entityPatch.getOriginal().getUseItem().getUseAnimation() == UseAnim.BLOCK && entityPatch instanceof PlayerPatch<?> playerPatch) {
            if (!playerPatch.getSkill(SkillSlots.GUARD).isEmpty() && !playerPatch.getOriginal().isCrouching()) {
                return null;
            }
        }
        return LivingMotions.AIM;
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getGuardMotion(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch) {
        ToolStack toolStack = getToolStack();
        if (toolStack == null || toolStack.getModifierLevel(Objects.requireNonNull(BLOCKING_ID, "blocking modifier id")) <= 0) {
            return null;
        }

        CapabilityItem delegatedCapability = this.getWeapon();
        if (delegatedCapability != null && delegatedCapability.getGuardMotion(skill, blockType, playerpatch) != null) {
            return delegatedCapability.getGuardMotion(skill, blockType, playerpatch);
        }

        Map<GuardSkill.BlockType, AnimationManager.AnimationAccessor<? extends StaticAnimation>> styleGuardMotions = this.guardMotions.get(this.styleProvider.apply(playerpatch));
        return styleGuardMotions != null ? styleGuardMotions.get(blockType) : null;
    }

    @Override
    public UseAnim getUseAnimation(LivingEntityPatch<?> entityPatch) {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null && delegatedCap.getUseAnimation(entityPatch) != UseAnim.NONE) {
            return delegatedCap.getUseAnimation(entityPatch);
        }

        ToolStack toolStack = getToolStack();
        Style baseStyle = this.styleProvider.apply(entityPatch);
        if (this.livingMotionModifiers.containsKey(baseStyle) && this.livingMotionModifiers.get(baseStyle).containsKey(LivingMotions.BLOCK) && toolStack != null && toolStack.getModifierLevel(BLOCKING_ID) > 0) {
            return UseAnim.BLOCK;
        }
        return UseAnim.NONE;
    }

    @Override
    public boolean canBePlacedOffhand() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.canBePlacedOffhand();
        return this.canBePlacedOffhand;
    }

    @Override
    public CapabilityItem.ZoomInType getZoomInType() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getZoomInType();
        return this.zoomInType == ZoomInType.NONE ? ZoomInType.AIMING : this.zoomInType;
    }

    @Override
    public float getReach() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.getReach();
        return this.reach;
    }

    @SuppressWarnings("removal")
    public Skill getPassiveSkill() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null && delegatedCap.getPassiveSkill() != null) {
            return delegatedCap.getPassiveSkill();
        }
        return this.passiveSkill;
    }

    public ToolStack getToolStack() {
        return this.tool;
    }

    public ModifierProfile getModifierProfile(Predicate<ModifierProfile> profilePredicate) {
        ToolStack toolStack = getToolStack();
        ModifierProfile foundProfile = null;
        if (toolStack != null) {
            for (ModifierEntry entry : toolStack.getModifierList()) {
                if (entry.getLevel() <= 0) continue;
                ModifierProfile profile = ModifierProfileReloadListener.get(entry.getId());
                if (profile != null && (foundProfile == null || profile.priority() > foundProfile.priority())) {
                    if (profilePredicate.test(profile)) {
                        foundProfile = profile;
                    }
                }
            }
        }

        return foundProfile;
    }

    public boolean canHoldInOffhandAlone() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.canHoldInOffhandAlone();
        return this.canBePlacedOffhand;
    }

    @SuppressWarnings("removal")
    public boolean availableOnHorse() {
        CapabilityItem delegatedCap = this.getWeapon();
        if (delegatedCap != null) return delegatedCap.availableOnHorse();
        return this.autoAttackMotions.containsKey(Styles.MOUNT);
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
        Skill passiveSkill = null;
        Map<Style, Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotionModifiers;
        Function<LivingEntityPatch<?>, Boolean> weaponCombinationPredicator;
        BiFunction<LivingEntityPatch<?>, InteractionHand, LivingMotion> motionPredicator;
        Map<Style, Map<GuardSkill.BlockType, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMotions;
        Function<Style, Boolean> comboCancel;
        ComboCounterHandleEvent.ComboCounterHandler comboCounterHandler;
        boolean canBePlacedOffhand;
        CapabilityItem.ZoomInType zoomInType;
        float reach;
        ToolStack tool;

        Builder() {
            super();
            this.constructor(TCWeaponCapability::new);
            this.styleProvider = (entityPatch) -> Styles.ONE_HAND;
            this.swingSound = yesman.epicfight.gameasset.EpicFightSounds.WHOOSH.get();
            this.hitSound = yesman.epicfight.gameasset.EpicFightSounds.BLUNT_HIT.get();
            this.hitParticle = yesman.epicfight.particle.EpicFightParticles.HIT_BLADE.get();
            this.autoAttackMotionMap = new HashMap<>();
            this.innateSkillByStyle = new HashMap<>();
            this.livingMotionModifiers = new HashMap<>();
            this.livingMotionModifier(Styles.COMMON, LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM);
            this.livingMotionModifier(Styles.COMMON, LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW);
            this.guardMotions = new HashMap<>();
            this.weaponCombinationPredicator = (entitypatch) -> false;
            this.motionPredicator = (patch, hand) -> null;
            this.canBePlacedOffhand = true;
            this.comboCancel = (style) -> true;
            this.comboCounterHandler = ComboCounterHandleEvent.ComboCounterHandler.DEFAULT_COMBO_HANDLER;
            this.zoomInType = CapabilityItem.ZoomInType.NONE;
            this.reach = 0.2F;
            this.tool = null;
        }

        public Builder innateSkill(Style style, Function<ItemStack, Skill> innateSkill) {
            this.innateSkillByStyle.put(style, innateSkill);
            return this;
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

        @SafeVarargs
        public final Builder newStyleCombo(Style style, AnimationManager.AnimationAccessor<? extends AttackAnimation>... animation) {
            this.autoAttackMotionMap.put(style, Lists.newArrayList(animation));
            return this;
        }

        public Builder livingMotionModifier(Style wieldStyle, LivingMotion livingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation> animation) {
            if (AnimationManager.checkNull(animation)) {
                EpicFightTinkerCompat.LOGGER.warn("Unable to put an empty animation to weapon capability builder: {}, {}", livingMotion.toString(), animation.toString());
            } else {
                if (this.livingMotionModifiers == null) {
                    this.livingMotionModifiers = Maps.newHashMap();
                }

                if (!this.livingMotionModifiers.containsKey(wieldStyle)) {
                    this.livingMotionModifiers.put(wieldStyle, Maps.newHashMap());
                }

                this.livingMotionModifiers.get(wieldStyle).put(livingMotion, animation);
            }
            return this;
        }

        public Builder weaponCombinationPredicator(Function<LivingEntityPatch<?>, Boolean> predicator) {
            this.weaponCombinationPredicator = predicator;
            return this;
        }

        public Builder motionPredicator(BiFunction<LivingEntityPatch<?>, InteractionHand, LivingMotion> motionPredicate) {
            this.motionPredicator = motionPredicate;
            return this;
        }

        public Builder guardMotion(Style style, GuardSkill.BlockType blockType, AnimationManager.AnimationAccessor<? extends StaticAnimation> animation) {
            this.guardMotions.computeIfAbsent(style, k -> new HashMap<>()).put(blockType, animation);
            return this;
        }

        public Builder passiveSkill(Skill passiveSkill) {
            this.passiveSkill = passiveSkill;
            return this;
        }

        public Builder tool(ItemStack stack) {
            if (stack.getItem() instanceof IModifiable) {
                this.tool = ToolStack.from(stack);
            }
            return this;
        }

    }
}