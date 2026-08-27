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

    private final SubToolMaker sheathMaker;
    private final SubToolMaker unsheathedMaker;

    private record SubToolMaker(ToolStack mold, int startIndex, int partCount) {
        public ItemStack createSubTool(ToolStack tool) {
            ItemStack subTool = new ItemStack(mold.getItem());
            MaterialNBT materials = mold.getMaterials();
            for (int i = 0; i < partCount; i++) {
                materials = materials.replaceMaterial(i, tool.getMaterial(i + startIndex));
            }
            mold.setMaterials(materials);
            mold.updateStack(subTool, true);
            return subTool;
        }

        public Pair<ResourceLocation, List<MaterialId>> getKeyFrom(ToolStack tool) {
            List<MaterialId> materialIds = new ArrayList<>();
            MaterialNBT materials = tool.getMaterials();
            for (int i = 0; i < partCount; i++) {
                materialIds.add(materials.get(i + startIndex).getId());
            }
            return Pair.of(ForgeRegistries.ITEMS.getKey(mold.getItem()), materialIds);
        }
    }

    public RenderToolKatana(JsonElement jsonElement) {
        super(jsonElement);
        SubToolMaker tempSheathMaker = null;
        SubToolMaker tempUnsheathedMaker = null;

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
                } else if (sheathObject.has("sheath_item") && sheathObject.has("unsheathed_item")) {
                    ToolStack baseTool = ToolStack.from(new ItemStack(baseItem));
                    int toolPartCount = baseTool.getHook(ToolHooks.TOOL_PARTS).getParts(baseTool.getDefinition()).size();
                    boolean isValidToolPartCount = toolPartCount > 0;
                    if (!isValidToolPartCount) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid tool parts count: {}, must be greater than 0 and not less than sheath parts count and equal the number of base tool part. Using the default tool builder", toolPartCount);
                    }

                    // sheath tool
                    Item sheathItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(sheathObject.get("sheath_item").getAsString()));
                    boolean isValidSheath = sheathItem instanceof IModifiable;
                    if (!isValidSheath && sheathItem != null) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid sheath item: {}, sheath item must be a Modifiable item. Using the default sheath builder", sheathItem.getDefaultInstance().getDisplayName().getString());
                    }

                    ToolStack sheathTool = isValidSheath ? ToolStack.from(new ItemStack(sheathItem)) : null;
                    int sheathPartCount = isValidSheath ? sheathTool.getHook(ToolHooks.TOOL_PARTS).getParts(sheathTool.getDefinition()).size() : 0;
                    boolean isValidSheathPartCount = sheathPartCount > 0 && sheathPartCount <= toolPartCount;
                    if (!isValidSheathPartCount) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid sheath parts count: {}, must be greater than 0 and equal the number of sheath tool parts. Using the default sheath builder", sheathPartCount);
                    }

                    // unsheathed tool
                    Item unsheathedItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(sheathObject.get("unsheathed_item").getAsString()));
                    boolean isValidUnsheathed = unsheathedItem instanceof IModifiable;
                    if (!isValidUnsheathed && unsheathedItem != null) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid sheath item: {}, unsheathed item must be a Modifiable item. Using the default sheath builder", unsheathedItem.getDefaultInstance().getDisplayName().getString());
                    }

                    ToolStack unsheathedTool = isValidUnsheathed ? ToolStack.from(new ItemStack(unsheathedItem)) : null;
                    int unsheathedPartCount = isValidUnsheathed ? unsheathedTool.getHook(ToolHooks.TOOL_PARTS).getParts(unsheathedTool.getDefinition()).size() : 0;
                    boolean isValidUnsheathedPartCount = unsheathedPartCount > 0 && unsheathedPartCount <= toolPartCount;
                    if (!isValidUnsheathedPartCount) {
                        EpicFightTinkerCompat.LOGGER.warn("Invalid unsheathed parts count: {}, must be greater than 0 and equal the number of sheath tool parts. Using the default sheath builder", unsheathedPartCount);
                    }

                    if (isValidToolPartCount) {
                        if (isValidSheath && isValidSheathPartCount) {
                            tempSheathMaker = new SubToolMaker(sheathTool, toolPartCount - sheathPartCount, sheathPartCount);
                        }

                        if (isValidUnsheathed && isValidUnsheathedPartCount) {
                            tempUnsheathedMaker = new SubToolMaker(unsheathedTool, 0, unsheathedPartCount);
                        }
                    }
                }
            }
        } catch (Exception e) {
            EpicFightTinkerCompat.LOGGER.error("Error loading ToolKatana JSON!", e);
        }

        if (tempSheathMaker == null) {
            EpicFightTinkerCompat.LOGGER.warn("Invalid sheath configuration, using the default sheath builder");
            tempSheathMaker = new SubToolMaker(ToolStack.from(new ItemStack(ItemRegistry.ODACHI_SHEATH.get())), 4, 2);
        }

        if (tempUnsheathedMaker == null) {
            EpicFightTinkerCompat.LOGGER.warn("Invalid unsheathed configuration, using the default unsheathed builder");
            tempUnsheathedMaker = new SubToolMaker(ToolStack.from(new ItemStack(ItemRegistry.UNSHEATHED_ODACHI.get())), 0, 4);
        }

        this.sheathMaker = tempSheathMaker;
        this.unsheathedMaker = tempUnsheathedMaker;
    }

    @Override
    public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        OpenMatrix4f modelMatrix = this.getCorrectionMatrix(entitypatch, InteractionHand.MAIN_HAND, poses);
        poseStack.pushPose();

        ItemStack unsheathedStack = getSubTool(stack, this.unsheathedMaker);
        MathUtils.mulStack(poseStack, modelMatrix);
        itemRenderer.renderStatic(unsheathedStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
        poseStack.popPose();

        modelMatrix = this.getCorrectionMatrix(entitypatch, InteractionHand.OFF_HAND, poses);

        poseStack.pushPose();

        ItemStack sheathStack = getSubTool(stack, this.sheathMaker);
        MathUtils.mulStack(poseStack, modelMatrix);
        itemRenderer.renderStatic(sheathStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
        poseStack.popPose();
    }

    private static ItemStack getSubTool(ItemStack stack, SubToolMaker toolMaker) {
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);
            var key = toolMaker.getKeyFrom(tool);
            ItemStack subTool = CACHE.get(key);
            if (subTool == null) {
                subTool = toolMaker.createSubTool(tool);
                CACHE.put(key, subTool);
            }
            return subTool;
        }
        return ItemStack.EMPTY;
    }

    @Mod.EventBusSubscriber(modid = EpicFightTinkerCompat.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void onItemRendererRegister(PatchedRenderersEvent.RegisterItemRenderer event) {
            event.addItemRenderer(rl, RenderToolKatana::new);
        }
    }
}
