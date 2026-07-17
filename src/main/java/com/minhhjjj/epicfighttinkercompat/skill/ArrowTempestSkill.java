package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.TCScanAttackAnimation;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.entity.ThrownTool;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSetSkillContainerValue;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArrowTempestSkill extends SimpleWeaponInnateSkill {
    private static final UUID EVENT_UUID = UUID.fromString("c9e8b1d4-6f2a-47bd-98f1-1d6c5a72e4b9");
    private static final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/gui/skills/weapon_innate/steel_whirlwind.png");
    public static final float DEFAULT_COOLDOWN = 10f;
    public static final String KEY_SCAN_ATTACK = "key_scan_attack";
    public static final String KEY_SKILL_ATTACK = "key_skill_attack";
    public static final String KEY_RESOURCE = "key_resource";
    public static final String KEY_STACKS = "key_stacks";

    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
            if (event.getProjectile() instanceof AbstractArrow arrow) {
                if (arrow.getOwner() != null && arrow.getOwner().level().isClientSide) return;
                if (arrow.getOwner() instanceof ServerPlayer player) {
                    float bonusResource = 0f;
                    if (arrow.getTags().contains(KEY_SCAN_ATTACK)) {
                        bonusResource = 5.0f;
                    } else if (!arrow.getTags().contains(KEY_SKILL_ATTACK)) {
                        bonusResource = 10.0f;
                    }

                    if (bonusResource > 0) {
                        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                        if (playerPatch != null) {
                            SkillContainer container = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                            if (container != null && container.getSkill() instanceof ArrowTempestSkill) {
                                int lastStack = container.getStack();
                                float lastResource = container.getResource();
                                float newResource = lastResource + bonusResource;
                                container.setResource(newResource);
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.resource(SkillSlots.WEAPON_INNATE, newResource, player.getId()));
                                EpicFightTinkerCompat.LOGGER.info("last stack: {}, current stack: {}, last resource: {}, new resource: {}, current resource: {}", lastStack, container.getStack(), lastResource, newResource, container.getResource());
//                                if (container.getResource() == 0.0f && container.getStack() != lastStack) {
//                                    EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.stacks(SkillSlots.WEAPON_INNATE, container.getStack(), player.getId()));
//                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrowTempestSkill(SimpleWeaponInnateSkill.Builder builder) {
        super(builder.setResource(Resource.COOLDOWN));
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        if (container.getExecutor().getOriginal() != null) {
            ItemStack itemStack = container.getExecutor().getOriginal().getMainHandItem();
            if (!itemStack.isEmpty()) {
                EpicFightTinkerCompat.LOGGER.info("Item: {}", itemStack.getItem());
                container.setResource(itemStack.getOrCreateTag().getFloat(KEY_RESOURCE));
                container.setStack(itemStack.getOrCreateTag().getInt(KEY_STACKS));
                EpicFightTinkerCompat.LOGGER.info("resource: {}, stacks: {}", container.getResource(), container.getStack());


                if (!container.getExecutor().getOriginal().level().isClientSide) {
                    EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.stacks(SkillSlots.WEAPON_INNATE, container.getStack(), container.getExecutor().getOriginal().getId()));
                    EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.resource(SkillSlots.WEAPON_INNATE, container.getResource(), container.getExecutor().getOriginal().getId()));
                }
            }
        }

//        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID, (event) -> {
//            if (!(container.getExecutor().getOriginal() instanceof ServerPlayer)) return;
////            if (Arrays.stream(EFBowAnimations.getComboAttack()).anyMatch(a -> a.get().equals(event.getDamageSource().getAnimation()))) {
//            if (List.of(EFBowAnimations.BOW_ATTACK).contains(event.getDamageSource().getAnimation())) {
//                container.setResource(container.getResource() + 5);
//            }
//        });
//
//        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.PROJECTILE_HIT_EVENT, EVENT_UUID, (event) -> {
//            EpicFightTinkerCompat.LOGGER.info("Damage source: {} | owner: {}", event.getPlayerPatch().getOriginal(), event.getForgeEvent().getProjectile().getOwner());
//            if (event.getForgeEvent().getProjectile() instanceof AbstractArrow arrow) {
//                if (arrow.getTags().contains(KEY_SCAN_ATTACK)) {
//                    container.setResource(container.getResource() + 5);
//                } else if (!arrow.getTags().contains(KEY_SKILL_ATTACK)) {
//                    container.setResource(container.getResource() + 10);
//                }
//            }
//        });
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
//        if (container.getExecutor().getOriginal() != null) {
//            ItemStack itemStack = container.getExecutor().getOriginal().getMainHandItem();
//            if (!itemStack.isEmpty()) {
//                itemStack.getOrCreateTag().putFloat(KEY_RESOURCE, container.getResource());
//                itemStack.getOrCreateTag().putInt(KEY_STACKS, container.getStack());
//            }
//        }
//        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWeaponSwap(LivingEquipmentChangeEvent event) {
        if (event.getSlot() == EquipmentSlot.MAINHAND && event.getEntity() instanceof ServerPlayer player) {
            ItemStack oldItem = event.getFrom();
            EpicFightTinkerCompat.LOGGER.info("oldItem: {}", oldItem.getItem());
            if (!oldItem.isEmpty() && oldItem.getItem() instanceof ModifiableBowItem) {
                PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                if (playerPatch != null) {
                    SkillContainer container = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                    if (container != null && container.getSkill() instanceof ArrowTempestSkill) {
                        oldItem.getOrCreateTag().putFloat(KEY_RESOURCE, container.getResource());
                        oldItem.getOrCreateTag().putInt(KEY_STACKS, container.getStack());
                        EpicFightTinkerCompat.LOGGER.info("resource: {}, stacks: {}", container.getResource(), container.getStack());
                    }
                }
            }
        }
    }

    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
    }

    public ResourceLocation getSkillTexture() {
        return resourceLocation;
    }

    public float getCooldownRegenPerSecond(PlayerPatch<?> executor) {
        return 1f;
    }

    public static AnimationEvent.InTimeEvent<?> shoot(float time, float bonusAngle) {
        return AnimationEvent.InTimeEvent.create(time, ((livingEntityPatch, assetAccessor, animationParameters) -> {
            ItemStack itemStack = livingEntityPatch.getOriginal().getMainHandItem();
            ToolStack toolStack = ToolStack.from(itemStack);
            Item item = itemStack.getItem();
            if (item instanceof ModifiableBowItem bowItem) {
                int shootTime = toolStack.getModifierLevel(EpicFightModifiers.ARROW_TEMPEST) > 1 ? 1 : 3;
                for (int i = 0; i < shootTime; i++) {
                    shootOnce(livingEntityPatch, bonusAngle + i * 15f, itemStack, toolStack, bowItem);
                }
            }
            livingEntityPatch.getOriginal().stopUsingItem();
        }), AnimationEvent.Side.BOTH);
    }

    public static void shootOnce(LivingEntityPatch<?> livingEntityPatch, float bonusAngle, ItemStack itemStack, ToolStack toolStack, ModifiableBowItem bowItem) {
        int modifierLevel = toolStack.getModifierLevel(EpicFightModifiers.ARROW_TEMPEST);
        LivingEntity living = livingEntityPatch.getOriginal();
        itemStack.getOrCreateTag().putBoolean("is_full", false);
        Item item = itemStack.getItem();
        Level level = living.level();
        int leftTime = 0;
        if (livingEntityPatch.getOriginal() instanceof ServerPlayer player && !toolStack.isBroken()) {
            boolean flag = player.getAbilities().instabuild || itemStack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0 && !toolStack.getVolatileData().getBoolean(BowAmmoModifierHook.SKIP_INVENTORY_AMMO);

            Predicate<ItemStack> ammoPredicate;
            switch (toolStack.getPersistentData().getInt(ModifiableBowItem.KEY_BALLISTA)) {
                case 1 -> ammoPredicate = null;
                case 2 -> ammoPredicate = (stack) -> stack.is(TinkerTags.Items.BALLISTA_AMMO);
                case 3 -> ammoPredicate = bowItem.getSupportedHeldProjectiles();
                default -> ammoPredicate = ModifiableBowItem.isBallista(toolStack) ? bowItem.getSupportedBallistaAmmo() : bowItem.getSupportedHeldProjectiles();
            }
            ItemStack foundAmmo = BowAmmoModifierHook.getAmmo(toolStack, itemStack, player, ammoPredicate);

            int i = item.getUseDuration(itemStack) - leftTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(itemStack, level, player, i, !foundAmmo.isEmpty() || flag);
            if (i < 0) return;

            if (!foundAmmo.isEmpty() || flag) {
                if (foundAmmo.isEmpty()) {
                    foundAmmo = new ItemStack(Items.ARROW);
                }

                float f = 1.0F;
                boolean flag1 = player.getAbilities().instabuild || (foundAmmo.getItem() instanceof ArrowItem && ((ArrowItem) foundAmmo.getItem()).isInfinite(foundAmmo, itemStack, player));
                if (!level.isClientSide) {
                    int originalSlot = -1;
                    int desiredProjectiles = 1;
                    if (foundAmmo.is(TinkerTags.Items.BALLISTA_AMMO)) {
                        if (foundAmmo == living.getOffhandItem()) {
                            originalSlot = 40;
                        } else {
                            Inventory inventory = player.getInventory();

                            for(i = 0; i < 36; ++i) {
                                if (inventory.getItem(i) == foundAmmo) {
                                    originalSlot = i;
                                    break;
                                }
                            }
                        }
                    } else {
                        desiredProjectiles = BowAmmoModifierHook.getDesiredProjectiles(toolStack);
                    }

                    ItemStack ammo = BowAmmoModifierHook.consumeAmmo(toolStack, itemStack, player, player, ammoPredicate, desiredProjectiles);
                    if (ammo.isEmpty()) {
                        ammo = new ItemStack(Items.ARROW);
                    }

                    ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                    float power = ConditionalStatModifierHook.getModifiedStat(toolStack, player, ToolStats.VELOCITY);
                    float velocity = ConditionalStatModifierHook.getModifiedStat(toolStack, player, ToolStats.VELOCITY);
                    boolean thrownTool = ammo.is(TinkerTags.Items.BALLISTA_AMMO);
                    float waterInertia = 0.6F;
                    float startAngle = ModifiableBowItem.getAngleStart(ammo.getCount());
                    int primaryIndex = ammo.getCount() / 2;
                    SoundEvent sound = SoundEvents.ARROW_SHOOT;
                    if (thrownTool) {
                        sound = SoundEvents.TRIDENT_THROW;
                        IToolStackView thrown = ToolStack.from(ammo);
                        float thrownVelocity = ConditionalStatModifierHook.getModifiedStat(thrown, living, ToolStats.VELOCITY);
                        power *= thrownVelocity * ConditionalStatModifierHook.getModifiedStat(thrown, living, ToolStats.DRAW_SPEED) / 1.5F;
                        if (ammo.is(TinkerTags.Items.MELEE_WEAPON)) {
                            power *= thrown.getStats().get(ToolStats.ATTACK_SPEED);
                        }

                        velocity *= thrownVelocity;
                        waterInertia = ConditionalStatModifierHook.getModifiedStat(thrown, living, ToolStats.WATER_INERTIA);
                    }

                    for (int arrowIndex = 0;  arrowIndex < ammo.getCount(); ++arrowIndex) {
                        AbstractArrow abstractarrow;
                        if (thrownTool) {
                            ThrownTool thrown = new ThrownTool(level, player, ammo, f, velocity, waterInertia);
                            thrown.setOriginalSlot(originalSlot);
                            abstractarrow = thrown;
                        } else {
                            abstractarrow = arrowitem.createArrow(level, foundAmmo, player);
                        }

                        abstractarrow.setPos(EFBowAnimations.getJointWorldPos(livingEntityPatch, Armatures.BIPED.get().handL));
                        float angle = startAngle + (float)(10 * arrowIndex);

                        abstractarrow.shootFromRotation(player, living.getXRot() + angle, livingEntityPatch.getYRot() + bonusAngle, 0.0F, power * 3.0F, ModifierUtil.getInaccuracy(toolStack, player));

                        float baseArrowDamage = (float)(abstractarrow.getBaseDamage() - (double)2.0F + (double)toolStack.getStats().get(ToolStats.PROJECTILE_DAMAGE));
                        abstractarrow.setBaseDamage(ConditionalStatModifierHook.getModifiedStat(toolStack, living, ToolStats.PROJECTILE_DAMAGE, baseArrowDamage));
                        ModifierNBT modifiers = toolStack.getModifiers();
                        EntityModifierCapability.getCapability(abstractarrow).addModifiers(modifiers);
                        ModDataNBT arrowData = PersistentDataCapability.getOrWarn(abstractarrow);

                        for(ModifierEntry entry : modifiers.getModifiers()) {
                            (entry.getHook(ModifierHooks.PROJECTILE_LAUNCH)).onProjectileLaunch(toolStack, entry, living, ammo, abstractarrow, abstractarrow, arrowData, arrowIndex == primaryIndex);
                        }

                        if (thrownTool) {
                            ((ThrownTool)abstractarrow).onRelease(living, arrowData);
                        }

                        if (flag1 || player.getAbilities().instabuild && (foundAmmo.is(Items.SPECTRAL_ARROW) || foundAmmo.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        LivingEntity target = TCScanAttackAnimation.getTarget(livingEntityPatch);
                        if (modifierLevel == 1) {
                            abstractarrow.setKnockback(10);
                        } else {
                            HomingArrowTracker.addArrow(abstractarrow, target);
                        }

                        abstractarrow.getTags().add(KEY_SKILL_ATTACK);
                        level.addFreshEntity(abstractarrow);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    }

                    int damage = ammo.getCount() * (thrownTool ? 1 : 3);
                    ToolDamageUtil.damage(toolStack, damage, player, itemStack);
                }

                player.awardStat(Stats.ITEM_USED.get(bowItem));
            }
        }
    }

}
