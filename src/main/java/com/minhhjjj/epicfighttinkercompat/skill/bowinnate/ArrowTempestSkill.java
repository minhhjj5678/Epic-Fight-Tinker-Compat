package com.minhhjjj.epicfighttinkercompat.skill.bowinnate;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.TCScanAttackAnimation;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTAnimations;
import com.minhhjjj.epicfighttinkercompat.modifiers.EpicFightModifiers;
import com.minhhjjj.epicfighttinkercompat.skill.PersistentWeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
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
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.entity.ThrownTool;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSetSkillContainerValue;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.*;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArrowTempestSkill extends PersistentWeaponInnateSkill {
    public static final int AMMO_COUNT = 8;
    public static final float RADIUS = 20;
    public static final float RANGE = 20;
    public static final String KEY_SCAN_ATTACK = "key_scan_attack";
    public static final String KEY_SKILL_ATTACK = "key_skill_attack";

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
                            if (container != null && container.getSkill() != null) {
                                float lastResource = container.getResource();
                                float newResource = lastResource + bonusResource;
                                container.setResource(newResource);
                                EpicFightNetworkManager.sendToAll(SPSetSkillContainerValue.resource(SkillSlots.WEAPON_INNATE, newResource, player.getId()));
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrowTempestSkill(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder.setResource(Resource.COOLDOWN));
    }

    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        container.getExecutor().playAnimationSynchronized(EFTAnimations.ARROW_TEMPEST, 0.0F);
        super.executeOnServer(container, args);
    }

    public static AnimationEvent.InTimeEvent<?> shoot(float time) {
        return AnimationEvent.InTimeEvent.create(time, ((livingEntityPatch, assetAccessor, animationParameters) -> {
            ItemStack itemStack = livingEntityPatch.getOriginal().getMainHandItem();
            ToolStack toolStack = ToolStack.from(itemStack);
            Item item = itemStack.getItem();
            if (item instanceof ModifiableBowItem bowItem) {
                shootOnce(livingEntityPatch, itemStack, toolStack, bowItem);
            }
            livingEntityPatch.getOriginal().stopUsingItem();
        }), AnimationEvent.Side.BOTH);
    }

    public static void shootOnce(LivingEntityPatch<?> livingEntityPatch, ItemStack itemStack, ToolStack toolStack, ModifiableBowItem bowItem) {
        int modifierLevel = toolStack.getModifierLevel(EpicFightModifiers.ARROW_TEMPEST);
        LivingEntity living = livingEntityPatch.getOriginal();

        if (livingEntityPatch.getOriginal().level().isClientSide) {
            EFBowAnimations.DRAWING_PLAYERS.remove(livingEntityPatch.getOriginal().getUUID());
            living.getMainHandItem().getOrCreateTag().remove("is_full");
        }
        CompoundTag ticPersistent = itemStack.getTagElement("tic_persistent");
        if (ticPersistent != null) {
            ticPersistent.remove(ModifiableLauncherItem.KEY_DRAWBACK_AMMO.toString());
        }

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

                AABB aabb = player.getBoundingBox().inflate(RANGE);
                List<LivingEntity> targetList = player.level().getEntitiesOfClass(LivingEntity.class, aabb);
                targetList.removeIf(entity -> entity.equals(player) || !entity.isAlive());

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
                    } else if (modifierLevel == 1) {
                        desiredProjectiles = targetList.size();
                    } else if (modifierLevel == 2) {
                        desiredProjectiles = AMMO_COUNT;
                    }

                    ItemStack ammo = BowAmmoModifierHook.consumeAmmo(toolStack, itemStack, player, player, ammoPredicate, desiredProjectiles);
                    if (ammo.isEmpty()) {
                        return;
//                        ammo = new ItemStack(Items.ARROW);
                    }

                    ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                    float power = ConditionalStatModifierHook.getModifiedStat(toolStack, player, ToolStats.VELOCITY);
                    float velocity = ConditionalStatModifierHook.getModifiedStat(toolStack, player, ToolStats.VELOCITY);
                    boolean thrownTool = ammo.is(TinkerTags.Items.BALLISTA_AMMO);
                    float waterInertia = 0.6F;
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

                    int ammoCount = ammo.getCount();
                    for (int arrowIndex = 0;  arrowIndex < ammoCount; ++arrowIndex) {
                        AbstractArrow abstractarrow;
                        if (thrownTool) {
                            ThrownTool thrown = new ThrownTool(level, player, ammo, f, velocity, waterInertia);
                            thrown.setOriginalSlot(originalSlot);
                            abstractarrow = thrown;
                        } else {
                            abstractarrow = arrowitem.createArrow(level, foundAmmo, player);
                        }

                        abstractarrow.setPos(EFBowAnimations.getJointWorldPos(livingEntityPatch, Armatures.BIPED.get().handL));
                        if (modifierLevel > 1) {
                            float angleX = Mth.randomBetween(player.level().random, -RADIUS, RADIUS);
                            float angleY = (float) (Math.sqrt(RADIUS * RADIUS - angleX * angleX) * (Math.random() > 0.5 ? 1 : -1));
                            abstractarrow.shootFromRotation(player, living.getXRot() + angleX, livingEntityPatch.getYRot() + angleY, 0.0f, power * 3.0F, ModifierUtil.getInaccuracy(toolStack, player));
                        } else {
                            abstractarrow.shootFromRotation(player, -75, livingEntityPatch.getYRot(), 0.0F, power, ModifierUtil.getInaccuracy(toolStack, player));
                        }

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
                            if (arrowIndex < targetList.size() && !targetList.isEmpty()) {
                                TempestArrowTracker.addArrow(abstractarrow, targetList.get(arrowIndex).getEyePosition());
                                abstractarrow.setKnockback(abstractarrow.getKnockback() + 2);
                            }
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
