package com.minhhjjj.epicfighttinkercompat.gameasset;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.TCScanAttackAnimation;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
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
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.damagesource.StunType;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class EFTAnimations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> BIPED_HOLD_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_WALK_HAMMER;
    public static AnimationManager.AnimationAccessor<MovementAnimation> BIPED_RUN_HAMMER;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> HAMMER_AUTO1;

    public static AnimationManager.AnimationAccessor<AttackAnimation> TEST;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightTinkerCompat.MODID, EFTAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        BIPED_HOLD_HAMMER = builder.nextAccessor("biped/living/hold_hammer", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED));
        BIPED_WALK_HAMMER = builder.nextAccessor("biped/living/walk_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        BIPED_RUN_HAMMER = builder.nextAccessor("biped/living/run_hammer", (accessor) -> new MovementAnimation(true, accessor, Armatures.BIPED));
        HAMMER_AUTO1 = builder.nextAccessor("biped/combat/hammer_auto1", (accessor) -> new BasicAttackAnimation(0.25f, 0.15f, 0.52f, 0.95f, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0f));
        TEST = builder.nextAccessor("biped/combat/hammer_auto2",(accessor) -> new AttackAnimation(0.25f,
                0.15f, 0.52f, 0.95f, 0.95f, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
        .addEvents(
                shootOnce(7.5F / 60), shootOnce(15F / 60), shootOnce(22.5F / 60),
                shootOnce(30F / 60), shootOnce(37.5F / 60), shootOnce(45F / 60),
                shootOnce(52.5F / 60), shootOnce(1)));
        EFBowAnimations.buildBowAnimations(builder);
    }

    public static AnimationEvent.InTimeEvent<?> shootOnce(float time) {
        return AnimationEvent.InTimeEvent.create(time, (livingEntityPatch, assetAccessor, animationParameters) -> {
            EpicFightTinkerCompat.LOGGER.info("Flag1");
            LivingEntity living = livingEntityPatch.getOriginal();
            ItemStack itemStack = living.getMainHandItem();
            itemStack.getOrCreateTag().putBoolean("is_full", false);
            ToolStack toolStack = ToolStack.from(itemStack);
            Item item = itemStack.getItem();
            Level level = living.level();
            int leftTime = 0;
            if (livingEntityPatch.getOriginal() instanceof ServerPlayer player && item instanceof ModifiableBowItem bowItem && !toolStack.isBroken()) {
                EpicFightTinkerCompat.LOGGER.info("Flag2");
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
                    EpicFightTinkerCompat.LOGGER.info("Flag3");
                    if (foundAmmo.isEmpty()) {
                        foundAmmo = new ItemStack(Items.ARROW);
                    }

                    float f = 1.0F;
                    boolean flag1 = player.getAbilities().instabuild || (foundAmmo.getItem() instanceof ArrowItem && ((ArrowItem) foundAmmo.getItem()).isInfinite(foundAmmo, itemStack, player));
                    if (!level.isClientSide) {
                        EpicFightTinkerCompat.LOGGER.info("Flag4");
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
                            EpicFightTinkerCompat.LOGGER.info("Flag" + arrowIndex + 5);
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

                            abstractarrow.shootFromRotation(player, living.getXRot() + angle, livingEntityPatch.getYRot(), 0.0F, power * 3.0F, ModifierUtil.getInaccuracy(toolStack, player));

                            float baseArrowDamage = (float)(abstractarrow.getBaseDamage() - (double)2.0F + (double)(Float)toolStack.getStats().get(ToolStats.PROJECTILE_DAMAGE));
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
                            level.addFreshEntity(abstractarrow);
                            level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        }

                        int damage = ammo.getCount() * (thrownTool ? 1 : 3);
                        ToolDamageUtil.damage(toolStack, damage, player, itemStack);
                    }
                    EpicFightTinkerCompat.LOGGER.info("Flag 20");

                    player.awardStat(Stats.ITEM_USED.get(bowItem));
                }
                EpicFightTinkerCompat.LOGGER.info("Flag 22");
            }
            EpicFightTinkerCompat.LOGGER.info("Flag 24");
        }, AnimationEvent.Side.BOTH);
    }

}
