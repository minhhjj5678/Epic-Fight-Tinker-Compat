package com.minhhjjj.epicfighttinkercompat.client.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.RegistryAccess;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import slimeknights.tconstruct.library.client.armor.ArmorModelManager;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier.TextureType;
import slimeknights.tconstruct.library.client.armor.texture.TintedArmorTexture;
import slimeknights.tconstruct.library.client.armor.texture.TrimArmorTextureSupplier.TrimArmorTexture;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class TinkerArmorExtractor {

    private static Field TINTED_TEXTURE_FIELD = null;
    private static Field TRIM_SPRITE_FIELD = null;
    private static Method DISPATCHER_GET_NAME_METHOD = null;

    static {
        try {
            TINTED_TEXTURE_FIELD = TintedArmorTexture.class.getDeclaredField("texture");
            TINTED_TEXTURE_FIELD.setAccessible(true);

            TRIM_SPRITE_FIELD = TrimArmorTexture.class.getDeclaredField("trimSprite");
            TRIM_SPRITE_FIELD.setAccessible(true);

            DISPATCHER_GET_NAME_METHOD = ArmorModelManager.ArmorModelDispatcher.class.getDeclaredMethod("getName");
            DISPATCHER_GET_NAME_METHOD.setAccessible(true);
        } catch (Exception e) {
            System.err.println("[EpicFight-TinkerCompat] Can't load Reflection for TinkerArmorExtractor!");
            e.printStackTrace();
        }
    }

    public static boolean supportsTinkerArmorRendering(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem && getModelIdFromStack(stack) != null;
    }

    public static List<ArmorLayerInfo> getLayerPaths(ItemStack stack, EquipmentSlot slot, RegistryAccess registryAccess) {
        List<ArmorLayerInfo> layerInfos = new ArrayList<>();

        ResourceLocation modelId = getModelIdFromStack(stack);

        if (modelId == null) {
            return layerInfos;
        }

        ArmorModelManager.ArmorModel model = ArmorModelManager.INSTANCE.getModel(modelId);

        if (model == ArmorModelManager.ArmorModel.EMPTY) {
            return layerInfos;
        }

        TextureType type = TextureType.fromSlot(slot);
        for (ArmorTextureSupplier supplier : model.layers()) {
            ArmorTextureSupplier.ArmorTexture tex = supplier.getArmorTexture(stack, type, registryAccess);

            if (tex != ArmorTextureSupplier.ArmorTexture.EMPTY) {
                processTextureLayer(tex, layerInfos);
            }
        }

        return layerInfos;
    }

    private static void processTextureLayer(ArmorTextureSupplier.ArmorTexture tex, List<ArmorLayerInfo> list) {
        if (tex instanceof TintedArmorTexture tintedTex) {
            try {
                if (TINTED_TEXTURE_FIELD != null) {
                    // Dùng Field đã cache
                    ResourceLocation path = (ResourceLocation) TINTED_TEXTURE_FIELD.get(tintedTex);
                    int color = tintedTex.color();
                    list.add(new ArmorLayerInfo(path, color));
                }
            } catch (Exception e) {
            }
        }

        else if (tex instanceof TrimArmorTexture trimTex) {
            try {
                if (TRIM_SPRITE_FIELD != null) {
                    // Dùng Field đã cache
                    TextureAtlasSprite sprite = (TextureAtlasSprite) TRIM_SPRITE_FIELD.get(trimTex);

                    if (sprite != null) {
                        ResourceLocation spriteId = sprite.contents().name();

                        ResourceLocation realTexturePath = ResourceLocation.fromNamespaceAndPath(
                                spriteId.getNamespace(),
                                "textures/" + spriteId.getPath() + ".png"
                        );

                        list.add(new ArmorLayerInfo(realTexturePath, -1));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Nullable
    private static ResourceLocation getModelIdFromStack(ItemStack stack) {
        try {
            Object extension = IClientItemExtensions.of(stack);
            if (extension instanceof ArmorModelManager.ArmorModelDispatcher dispatcher) {
                if (DISPATCHER_GET_NAME_METHOD != null) {
                    // Dùng Method đã cache
                    ResourceLocation modelId = (ResourceLocation) DISPATCHER_GET_NAME_METHOD.invoke(dispatcher);
                    if (modelId != null) {
                        return modelId;
                    }
                }
            }
        } catch (Exception e) {
        }

        return null;
    }

    public record ArmorLayerInfo(ResourceLocation texturePath, int colorTint) {}
}