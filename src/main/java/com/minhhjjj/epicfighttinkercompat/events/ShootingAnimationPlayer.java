package com.minhhjjj.epicfighttinkercompat.events;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.network.NetworkManager;
import com.minhhjjj.epicfighttinkercompat.network.SyncLivingMotionPacket;
import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TCWeaponCapability;
import com.minhhjjj.epicfighttinkercompat.tool.capabilities.TinkerCrossbowCapability;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPAnimatorControl;
import yesman.epicfight.network.common.AnimatorControlPacket;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.*;

@Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID)
public class ShootingAnimationPlayer {
    private static final Set<Player> PENDING_ANIMATIONS = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
    private static final Set<Player> PENDING_UPDATE_MOTIONS = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    @SubscribeEvent
    public static void onToolShot(LivingEntityUseItemEvent.Stop event) {
        ItemStack stack = event.getItem();
        LivingEntity living = event.getEntity();
        if (stack.getItem() instanceof IModifiable && living != null) {
            CapabilityItem cap = EpicFightCapabilities.getItemStackCapabilityOr(stack, CapabilityItem.EMPTY);
            if (cap instanceof TCWeaponCapability) {
                LivingEntityPatch<?> patch = EpicFightCapabilities.getEntityPatch(living, LivingEntityPatch.class);
                if (patch != null) {
                    int chargeTime = event.getItem().getUseDuration() - event.getDuration();
                    if (chargeTime >= 10 && patch.currentCompositeMotion == yesman.epicfight.api.animation.LivingMotions.AIM) {
                        triggerShotAnimation(patch, cap);
                        if (living instanceof Player player) {
                            PENDING_ANIMATIONS.add(player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCrossbowShot(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ModifiableCrossbowItem) {
            Player player = event.getEntity();
            CapabilityItem cap = EpicFightCapabilities.getItemStackCapabilityOr(stack, CapabilityItem.EMPTY);
            PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
            if (cap instanceof TinkerCrossbowCapability) {
                ToolStack tool = ToolStack.from(stack);
                if (!tool.getPersistentData().getCompound(ModifiableCrossbowItem.KEY_CROSSBOW_AMMO).isEmpty()) {
                    triggerShotAnimation(patch, cap);
                    PENDING_ANIMATIONS.add(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isServer()) return;
        Player player = event.player;
        if (PENDING_UPDATE_MOTIONS.contains(player)) {
            PlayerPatch<?> playerPatch = EpicFightCapabilities.getPlayerPatch(event.player);
            if (playerPatch != null && !playerPatch.getClientAnimator().currentCompositeMotion().isSame(LivingMotions.SHOT)) {
                PENDING_UPDATE_MOTIONS.remove(player);
                SyncLivingMotionPacket packet = new SyncLivingMotionPacket();
                NetworkManager.CHANNEL.send(PacketDistributor.SERVER.noArg(), packet);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        PENDING_UPDATE_MOTIONS.remove(event.getOriginal());
    }

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING_UPDATE_MOTIONS.remove(event.getEntity());
    }

    public static boolean consumeFlag(Player player) {
        if (PENDING_ANIMATIONS.remove(player)) {
            PENDING_UPDATE_MOTIONS.add(player);
            return true;
        }
        return false;
    }

    public static void removeUpdateMotionFlag(Player player) {
        PENDING_UPDATE_MOTIONS.remove(player);
    }

    private static void triggerShotAnimation(LivingEntityPatch<?> patch, CapabilityItem cap) {
        var shotAnimation = cap.getLivingMotionModifier(patch, InteractionHand.MAIN_HAND).get(LivingMotions.SHOT);
        if (shotAnimation != null) {
            patch.currentCompositeMotion = LivingMotions.NONE;
            if (!patch.isLogicalClient()) {
                patch.playAnimationSynchronized(shotAnimation, 0.0f);
            } else {
                patch.getAnimator().playAnimation(shotAnimation, 0.0f);
                EpicFightNetworkManager.sendToServer(
                        new CPAnimatorControl(
                                AnimatorControlPacket.Action.PLAY_INSTANTLY,
                                shotAnimation,
                                0.0F,
                                false,
                                true,
                                false
                        )
                );
            }
        }
    }

}
