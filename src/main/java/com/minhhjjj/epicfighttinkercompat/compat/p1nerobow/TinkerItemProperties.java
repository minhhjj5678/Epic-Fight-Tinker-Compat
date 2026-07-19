package com.minhhjjj.epicfighttinkercompat.compat.p1nerobow;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@EventBusSubscriber(
        modid = EpicFightTinkerCompat.MODID,
        bus = Bus.MOD,
        value = {Dist.CLIENT}
)
public class TinkerItemProperties {
    public TinkerItemProperties() {
    }

    private static final ResourceLocation CHARGING_ID = TConstruct.getResource("charging");
    private static final ItemPropertyFunction CHARGING = (stack, level, holder, seed) -> {
        if (holder != null && EFBowAnimations.DRAWING_PLAYERS.contains(holder.getUUID())) {
            return 1.0f;
        }

        if (holder != null && holder.isUsingItem() && holder.getUseItem() == stack) {
            UseAnim anim = stack.getUseAnimation();
            if (anim == UseAnim.BLOCK) {
                return ModifierUtil.checkPersistentPresent(stack, ModifiableLauncherItem.KEY_DRAWBACK_AMMO) ? 2.5F : 2.0F;
            }

            if (anim == UseAnim.SPEAR) {
                return 1.75F;
            }

            if (anim != UseAnim.EAT && anim != UseAnim.DRINK) {
                return ModifierUtil.checkPersistentPresent(stack, ModifiableLauncherItem.KEY_DRAWBACK_AMMO) ? 1.5F : 1.0F;
            }
        }

        return 0.0F;
    };
    private static final ResourceLocation CHARGE_ID = TConstruct.getResource("charge");
    private static final ItemPropertyFunction CHARGE = (stack, level, holder, seed) -> {
        if (holder != null && EFBowAnimations.DRAWING_PLAYERS.contains(holder.getUUID())) {
            return 0.9f;
        }

        if (holder != null && holder.getUseItem() == stack) {
            int drawtime = ModifierUtil.getPersistentInt(stack, GeneralInteractionModifierHook.KEY_DRAWTIME, -1);
            return drawtime == -1 ? 0.0F : (float)(stack.getUseDuration() - holder.getUseItemRemainingTicks()) / (float)drawtime;
        } else {
            return 0.0F;
        }
    };

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Item longbow = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(TConstruct.MOD_ID, "longbow"));
            if (longbow == null) return;

            ItemProperties.register(longbow, CHARGING_ID, CHARGING);
            ItemProperties.register(longbow, CHARGE_ID, CHARGE);
        });
    }
}
