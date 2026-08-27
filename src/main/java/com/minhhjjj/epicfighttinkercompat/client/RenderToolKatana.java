package com.minhhjjj.epicfighttinkercompat.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.tool.item.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderToolKatana extends RenderItemBase {
    private static final ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "tinker_katana");
    private static final Map<Pair<ResourceLocation, List<MaterialId>>, ItemStack> CACHE = new HashMap<>();

    private final SheathMaker sheathMaker;

    private record SheathMaker(ToolStack mold, int sheathPartCount, int toolPartCount) {
        public ItemStack createSheath(ToolStack tool) {
            ItemStack sheath = new ItemStack(mold.getItem());
            MaterialNBT materials = mold.getMaterials();
            for (int i = 0; i < toolPartCount - sheathPartCount; i++) {
                materials = materials.replaceMaterial(i, tool.getMaterial(i+sheathPartCount));
            }
            mold.setMaterials(materials);
            mold.updateStack(sheath, true);
            return sheath;
        }

        public Pair<ResourceLocation, List<MaterialId>> getKeyFrom(ToolStack tool) {
            List<MaterialId> materialIds = new ArrayList<>();
            MaterialNBT materials = tool.getMaterials();
            for (int i = 0; i < toolPartCount - sheathPartCount; i++) {
                materialIds.add(materials.get(i+sheathPartCount).getId());
            }
            return Pair.of(ForgeRegistries.ITEMS.getKey(mold.getItem()), materialIds);
        }
    }

    public RenderToolKatana(JsonElement jsonElement) {
        super(jsonElement);
        SheathMaker tempMaker = null;

        try {
            if (jsonElement.getAsJsonObject().has("sheath")) {
                JsonObject sheathObject = jsonElement.getAsJsonObject().get("sheath").getAsJsonObject();
                boolean isValidBaseItem = false;
                Item baseItem = null;
                if (sheathObject.has("base_item")) {
                    baseItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(sheathObject.get("base_item").getAsString()));
                    if (baseItem instanceof IModifiable) {
                        isValidBaseItem = true;
                    }
                }

                if (!isValidBaseItem) {
                    EpicFightTinkerCompat.LOGGER.error("Unable to load item skin for item {}", sheathObject);
                } else if (sheathObject.has("sheath_item") && sheathObject.has("sheath_parts_count") && sheathObject.has("tool_parts_count")) {
                    Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(sheathObject.get("sheath_item").getAsString()));
                    boolean isValidItem = item instanceof IModifiable;
                    if (!isValidItem && item != null) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid sheath item: {}, sheath item must be a Modifiable item. Using the default sheath builder", item.getDefaultInstance().getDisplayName().getString());
                    }

                    ToolStack tool = isValidItem ? ToolStack.from(new ItemStack(item)) : null;
                    int sheathPartCount = sheathObject.get("sheath_parts_count").getAsInt();
                    boolean isValidSheathPartCount = sheathPartCount > 0 && isValidItem && tool.getHook(ToolHooks.TOOL_PARTS).getParts(tool.getDefinition()).size() == sheathPartCount;
                    if (!isValidSheathPartCount) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid sheath parts count: {}, must be greater than 0 and equal the number of sheath tool parts. Using the default sheath builder", sheathPartCount);
                    }

                    int toolPartCount = sheathObject.get("tool_parts_count").getAsInt();
                    ToolStack baseTool = ToolStack.from(new ItemStack(baseItem));
                    boolean isValidToolPartCount = toolPartCount > 0 && toolPartCount >= sheathPartCount && (baseTool.getHook(ToolHooks.TOOL_PARTS).getParts(baseTool.getDefinition()).size() == toolPartCount);
                    if (!isValidToolPartCount) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid tool parts count: {}, must be greater than 0 and not less than sheath parts count and equal the number of base tool part. Using the default tool builder", toolPartCount);
                    }

                    if (isValidItem && isValidSheathPartCount && isValidToolPartCount) {
                        tempMaker = new SheathMaker(ToolStack.from(new ItemStack(item)), sheathPartCount, toolPartCount);
                    }
                }
            }
        } catch (Exception e) {
            EpicFightTinkerCompat.LOGGER.error("Error loading ToolKatana JSON!", e);
        }

        if (tempMaker == null) {
            EpicFightTinkerCompat.LOGGER.warn("Invalid sheath configuration, using the default sheath builder");
            tempMaker = new SheathMaker(ToolStack.from(new ItemStack(ItemRegistry.SHEATH.get())), 2, 6);
        }
        this.sheathMaker = tempMaker;
    }

    @Override
    public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        OpenMatrix4f modelMatrix = this.getCorrectionMatrix(entitypatch, InteractionHand.MAIN_HAND, poses);
        poseStack.pushPose();
        MathUtils.mulStack(poseStack, modelMatrix);
        itemRenderer.renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
        poseStack.popPose();

        modelMatrix = this.getCorrectionMatrix(entitypatch, InteractionHand.OFF_HAND, poses);

        poseStack.pushPose();

        ItemStack sheathStack = ItemStack.EMPTY;
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);

            var key = this.sheathMaker.getKeyFrom(tool);
            sheathStack = CACHE.get(key);

            if (sheathStack == null) {
                sheathStack = this.sheathMaker.createSheath(tool);
                CACHE.put(key, sheathStack);
            }

        }
        MathUtils.mulStack(poseStack, modelMatrix);
        itemRenderer.renderStatic(sheathStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
        poseStack.popPose();
    }

    @Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void onItemRendererRegister(PatchedRenderersEvent.RegisterItemRenderer event) {
            event.addItemRenderer(rl, RenderToolKatana::new);
        }
    }
}
