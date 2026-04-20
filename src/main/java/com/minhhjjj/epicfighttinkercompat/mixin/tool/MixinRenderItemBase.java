package com.minhhjjj.epicfighttinkercompat.mixin.tool;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = RenderItemBase.class, remap = false)
public abstract class MixinRenderItemBase {

    @Shadow
    protected static ItemInHandRenderer itemInHandRenderer;

    @Shadow
    public abstract OpenMatrix4f getCorrectionMatrix(LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses);

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void injectTinkerLongbowRender(ItemStack itemstack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks, CallbackInfo ci) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemstack.getItem());
        if (itemId != null && itemId.getNamespace().equals("tconstruct") && itemId.getPath().equals("longbow")) {
            LivingEntity livingEntity = Objects.requireNonNull((LivingEntity) entitypatch.getOriginal(), "livingEntity");
            OpenMatrix4f modelMatrix = this.getCorrectionMatrix(entitypatch, InteractionHand.OFF_HAND, poses);
            poseStack.pushPose();
            MathUtils.mulStack(poseStack, modelMatrix);
            itemInHandRenderer.renderItem(
                livingEntity,
                itemstack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                false,
                poseStack,
                Objects.requireNonNull(buffer, "buffer"),
                packedLight
            );
            poseStack.popPose();
            ci.cancel();
        }
    }

    @Inject(
        method = "renderItemInHand",
        at = @At(
            value = "INVOKE",
            target = "Lyesman/epicfight/api/utils/math/MathUtils;mulStack(Lcom/mojang/blaze3d/vertex/PoseStack;Lyesman/epicfight/api/utils/math/OpenMatrix4f;)V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void injectTinkerShieldRender(ItemStack itemstack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks, CallbackInfo ci) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemstack.getItem());
        if (itemId != null && itemId.getNamespace().equals("tconstruct") && (itemId.getPath().equals("plate_shield") || itemId.getPath().equals("travelers_shield"))) {

            ItemDisplayContext transformType = (hand == InteractionHand.MAIN_HAND) ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            boolean leftHand = !(hand == InteractionHand.MAIN_HAND);
            net.minecraft.client.renderer.entity.ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            BakedModel baseModel = itemRenderer.getItemModelShaper().getItemModel(itemstack);

            BakedModel finalModel = baseModel.getOverrides().resolve(
                baseModel, 
                itemstack, 
                (net.minecraft.client.multiplayer.ClientLevel) entitypatch.getOriginal().level(), 
                entitypatch.getOriginal(), 
                entitypatch.getOriginal().getId()
            );
            
            if (finalModel == null) finalModel = baseModel;

            MultiBufferSource.BufferSource vanillaBuffer = Objects.requireNonNull(Minecraft.getInstance().renderBuffers().bufferSource(), "bufferSource");

            poseStack.pushPose();

            baseModel.applyTransform(transformType, poseStack, leftHand);

            itemRenderer.render(
                itemstack,
                ItemDisplayContext.NONE,
                leftHand,
                poseStack,
                vanillaBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                finalModel
            );

            vanillaBuffer.endBatch();
            poseStack.popPose();
            poseStack.popPose();
            ci.cancel(); 
        } 
    } 
}