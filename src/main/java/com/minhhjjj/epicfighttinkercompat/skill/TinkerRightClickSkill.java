package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.input.InputManager;
import yesman.epicfight.api.client.input.PlayerInputState;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSetSkillContainerValue;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.modules.HoldableSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;
import yesman.epicfight.world.entity.eventlistener.SkillCancelEvent;

import java.util.UUID;

public class TinkerRightClickSkill extends Skill implements HoldableSkill {
    private static final UUID EVENT_UUID = UUID.fromString("c8b5a9fa-fe2c-3634-8108-96e4fe265330");
    private static final ModifierId THROWING_ID = ModifierId.tryBuild(TConstruct.MOD_ID, "throwing");

//    public static class Builder extends SkillBuilder<TinkerRightClickSkill> {
//        // TODO: Thêm các biến cấu hình riêng của ông ở đây nếu cần (như modifierId, v.v.)
//    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, (event) -> {
            if (container.isActivated() && container.getExecutor().isHoldingSkill(this)) {
                container.getExecutor().getOriginal().setSprinting(false);
                ControlEngine.setSprintingKeyStateNotDown();
                PlayerInputState current = event.getInputState();
                PlayerInputState updated = current.withForwardImpulse(current.forwardImpulse() * 0.5F).withLeftImpulse(current.leftImpulse() * 0.5F);
                InputManager.setInputState(updated);
            }
        });

        container.getExecutor().getEventListener().addEventListener(EventType.TAKE_DAMAGE_EVENT_HURT, EVENT_UUID, (event) -> {
            if (event.getDamageSource() instanceof EpicFightDamageSource epicFightDamageSource) {
                epicFightDamageSource.setStunType(StunType.LONG);
            }
        });

        container.getExecutor().getEventListener().addEventListener(EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            if (container.isActivated() && container.getExecutor().isHoldingSkill(this)) {
                container.getExecutor().playAnimationSynchronized(Animations.BIPED_JAVELIN_AIM, 0.0f);
            }
        });
    }

//    @OnlyIn(Dist.CLIENT)
//    public void onInitiateClient(SkillContainer container) {
//        super.onInitiateClient(container);
//        container.getExecutor().getEventListener().addEventListener(EventType.UPDATE_BASE_LIVING_MOTION_EVENT, EVENT_UUID, (event) -> {
//            if (container.isActivated()) {
//                event.setMotion(LivingMotions.AIM);
//            }
//        });
//    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(EventType.TAKE_DAMAGE_EVENT_HURT, EVENT_UUID);
    }

//    @OnlyIn(Dist.CLIENT)
//    public void onRemoveClient(SkillContainer container) {
//        super.onRemoveClient(container);
//        container.getExecutor().getEventListener().removeListener(EventType.UPDATE_COMPOSITE_LIVING_MOTION_EVENT, EVENT_UUID);
//    }

    public TinkerRightClickSkill(SkillBuilder<?> builder) {
        // Bắt buộc phải set ActivateType là HELD để hệ thống nhận diện đây là skill giữ phím
        super(builder.setActivateType(ActivateType.HELD));
    }

    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        // TODO: Đọc các thông số cấu hình từ file datapack JSON ở đây
    }

    // =================================================================================
    // CÁC HÀM CỐT LÕI CỦA HOLDABLE SKILL (Nơi ông gắn logic TConstruct)
    // =================================================================================

    @Override
    public void startHolding(SkillContainer container) {
        container.activate();

        // Đồng bộ lên Server cho các người chơi khác thấy
        if (!container.getExecutor().isLogicalClient()) {
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(
                    SPSetSkillContainerValue.activate(container.getSlot(), true, container.getExecutor().getOriginal().getId()),
                    container.getExecutor().getOriginal()
            );
        }

        // TODO: Logic khởi đầu khi VỪA BẤM chuột phải (Ví dụ: phát âm thanh kéo cung, bắt đầu animation AIM)
        EpicFightTinkerCompat.LOGGER.info("Start holding...");
    }

    @Override
    public void holdTick(SkillContainer container) {
        // TODO: Logic chạy MỖI TICK khi người chơi đang ĐÈ chuột phải.
        // Phân biệt code chạy trên Server hay Client bằng lệnh:
        // if (!container.getExecutor().isLogicalClient()) { ... Server logic ... }
        if (container.getExecutor().isLogicalClient()) {
            EpicFightTinkerCompat.LOGGER.info("Current living animaion: {}", container.getExecutor().getClientAnimator().currentMotion());
        }
    }

    @Override
    public void onStopHolding(SkillContainer container, SPSkillExecutionFeedback feedback) {
        container.deactivate();

        // TODO: Logic chốt hạ khi người chơi NHẢ chuột phải (Ví dụ: Bắn cung, phóng lao, chém đòn mạnh)
        // Gọi thẳng hàm `onToolUse` của TConstruct ở đây là đẹp nhất!
        EpicFightTinkerCompat.LOGGER.info("Stop holding...");
    }

    @Override
    public void resetHolding(SkillContainer container) {
        if (container == null) return;
        container.deactivate();
        // TODO: Logic khi kỹ năng bị reset (buộc phải dừng)
    }

    @Override
    public void gatherHoldArguments(SkillContainer container, ControlEngine controlEngine, FriendlyByteBuf buffer) {
        // Gửi data từ Client lên Server trong lúc hold nếu cần (thường để trống)
    }

    // =================================================================================
    // BẮT PHÍM CHUỘT PHẢI
    // =================================================================================

    @OnlyIn(Dist.CLIENT)
    @Override
    public KeyMapping getKeyMapping() {
        // Trả về phím "Use Item" (Mặc định là Chuột Phải) của Minecraft Vanilla
        return EpicFightKeyMappings.GUARD;
    }

    // =================================================================================
    // ĐIỀU KIỆN KÍCH HOẠT VÀ HỦY KỸ NĂNG
    // =================================================================================

    @Override
    public boolean isExecutableState(PlayerPatch<?> executor) {
        return executor.isEpicFightMode()
                && !executor.isInAir()
                && !executor.getEntityState().hurt()
                && executor.getEntityState().canUseSkill()
                && !executor.isHoldingAny();
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        ToolStack toolStack = getToolStack(container);
        boolean canThrow = toolStack != null && toolStack.getModifierLevel(THROWING_ID) > 0 && !toolStack.isBroken();
        return this.checkExecuteCondition(container) && canThrow;
    }

    @Override
    public void cancelOnServer(SkillContainer container, FriendlyByteBuf args) {
        container.deactivate();
        container.getExecutor().resetHolding();

        ServerPlayerPatch executor = container.getServerExecutor();
        SkillCancelEvent skillCancelEvent = new SkillCancelEvent(executor, container);
        executor.getEventListener().triggerEvents(EventType.SKILL_CANCEL_EVENT, skillCancelEvent);

        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(
                SPSetSkillContainerValue.activate(container.getSlot(), false, container.getExecutor().getOriginal().getId()),
                container.getExecutor().getOriginal()
        );

        // TODO: Logic dọn dẹp trên server nếu skill bị gián đoạn (VD: bị quái đánh choáng lúc đang gồng)
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void cancelOnClient(SkillContainer container, FriendlyByteBuf args) {
        container.deactivate();
        super.cancelOnClient(container, args);
    }

    public ToolStack getToolStack(SkillContainer container) {
        Player player = container.getExecutor().getOriginal();
        ItemStack itemStack = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ModifiableItem)) { return null; }
        return ToolStack.from(itemStack);
    }
}