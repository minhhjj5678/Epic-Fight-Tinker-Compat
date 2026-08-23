package com.minhhjjj.epicfighttinkercompat.client;

import com.google.gson.JsonElement;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.tool.item.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RenderToolKatana extends RenderItemBase {
    private static final Map<Pair<Pair<MaterialId, MaterialId>, ResourceLocation>, ItemStack> CACHE = new ConcurrentHashMap<>();

    private final ToolStack builder;

    public RenderToolKatana(JsonElement jsonElement) {
        super(jsonElement);

        if (jsonElement.getAsJsonObject().has("scabbard")) {
            var item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(jsonElement.getAsJsonObject().get("scabbard").getAsString()));
            if (item instanceof IModifiable) {
                builder = ToolStack.from(new ItemStack(item));
                return;
            } else if (item != null) {
                EpicFightTinkerCompat.LOGGER.warn("Invalid scabbard item: {}, scabbard item must be a Modifiable item. Using the default scabbard builder", item.getDefaultInstance().getDisplayName().getString());
            }
        }
        builder = ToolStack.from(new ItemStack(ItemRegistry.SCABBARD.get()));

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

        ItemStack scabbardStack = ItemStack.EMPTY;
        if (stack.getItem() instanceof IModifiable) {
            ToolStack tool = ToolStack.from(stack);

            // TODO: Support dynamic scabbard tool parts instead of hardcoding for a 6-part tool
            var mat1 = tool.getMaterial(4);
            var mat2 = tool.getMaterial(5);
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(builder.getItem());
            var key = Pair.of(Pair.of(mat1.getId(), mat2.getId()), rl);
            scabbardStack = CACHE.getOrDefault(key, ItemStack.EMPTY);

            if (scabbardStack.isEmpty()) {
                builder.replaceMaterial(0, mat1);
                builder.replaceMaterial(1, mat2);
                builder.rebuildStats();
                scabbardStack = builder.createStack();
                CACHE.put(key, scabbardStack);
            }

        }
        MathUtils.mulStack(poseStack, modelMatrix);
        itemRenderer.renderStatic(scabbardStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, null, 0);
        poseStack.popPose();
    }
}
