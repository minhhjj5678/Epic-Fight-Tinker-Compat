package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTSkills;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryManager;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.gameasset.EpicFightSkills;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import net.minecraft.world.item.ItemStack;

public class SwapSkill extends Skill {
    private static final ModifierId THROWING_ID = ModifierId.tryBuild(TConstruct.MOD_ID, "throwing");
    private static final String NBT_KEY_ORIGINAL_GUARD = "original_guard";

    public SwapSkill(SkillBuilder builder) {
        super(builder);
    }

    // Hàm này chạy MỖI TICK khi người chơi đang cầm vũ khí này trên tay
    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        PlayerPatch<?> playerPatch = container.getExecutor();

        CompoundTag compoundTag = playerPatch.getOriginal().getPersistentData();

        // Lấy ổ cắm GUARD hiện tại của người chơi
        SkillContainer guardSlot = playerPatch.getSkill(SkillSlots.GUARD);
        Skill currentGuardSkill = guardSlot.getSkill();

        // 1. Kiểm tra điều kiện động (Ví dụ: Vũ khí không bị hỏng và có modifier Ném)
        boolean canThrow = checkThrowCondition(playerPatch);

        // 2. Logic tráo skill liên tục
        if (canThrow) {
            // Nếu đủ điều kiện, nhưng ô GUARD chưa phải là Phóng Lao -> Lắp Phóng Lao vào
            if (currentGuardSkill != EFTSkills.TINKER_RIGHT_CLICK_SKILL) {
                EpicFightTinkerCompat.LOGGER.info("Swapping skill from {} to {}", currentGuardSkill, EFTSkills.TINKER_RIGHT_CLICK_SKILL);
                if (currentGuardSkill != null) {
                    compoundTag.putString(NBT_KEY_ORIGINAL_GUARD, currentGuardSkill.getRegistryName().toString());
                }
                guardSlot.setSkill(EFTSkills.TINKER_RIGHT_CLICK_SKILL);
            }
        } else {
            // Nếu KHÔNG đủ điều kiện (ví dụ: vũ khí vừa gãy), mà ô GUARD vẫn đang là Phóng Lao -> Tháo ra, trả về Guard gốc
            if (currentGuardSkill == EFTSkills.TINKER_RIGHT_CLICK_SKILL) {
                EpicFightTinkerCompat.LOGGER.info("Swapping skill from {} to original skill", EFTSkills.TINKER_RIGHT_CLICK_SKILL);
                playerPatch.resetHolding();
                guardSlot.setSkill(getOriginalGuardSkill(playerPatch));
            }
        }
    }

    // Hàm cất vũ khí đi thì dọn dẹp (Trả lại Guard gốc)
    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        PlayerPatch<?> playerPatch = container.getExecutor();
        SkillContainer guardSlot = playerPatch.getSkill(SkillSlots.GUARD);

        if (guardSlot.getSkill() == EFTSkills.TINKER_RIGHT_CLICK_SKILL) {
            guardSlot.setSkill(getOriginalGuardSkill(playerPatch));
        }
    }

    // ====================================================================
    // CÁC HÀM HỖ TRỢ (HELPER METHODS)
    // ====================================================================

    // Hàm check điều kiện của ông (Viết logic TConstruct vào đây)
    private boolean checkThrowCondition(PlayerPatch<?> playerPatch) {
        ItemStack itemStack = playerPatch.getOriginal().getMainHandItem();
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ModifiableItem)) {
            return false;
        }

        if (playerPatch.getSkillCapability().skillContainers[SkillSlots.GUARD.universalOrdinal()].getSkill() instanceof GuardSkill && !playerPatch.getOriginal().isCrouching()) {
            return false;
        }

        CompoundTag tag =  playerPatch.getOriginal().getPersistentData();
        if (tag.contains(NBT_KEY_ORIGINAL_GUARD) && SkillManager.getSkill(tag.getString(NBT_KEY_ORIGINAL_GUARD)) instanceof GuardSkill && !playerPatch.getOriginal().isCrouching()) {
            return false;
        }

        ToolStack toolStack = ToolStack.from(itemStack);
        // Ví dụ: Không bị hỏng (Broken) và có Modifier Ném
        // TODO: Đổi tên modifier ID của ông vào đây
        return !toolStack.isBroken() && toolStack.getModifierLevel(THROWING_ID) > 0;
    }

    private Skill getOriginalGuardSkill(PlayerPatch<?> playerPatch) {
        Skill originalSkill = null;
        CompoundTag tag = playerPatch.getOriginal().getPersistentData();
        if (tag.contains(NBT_KEY_ORIGINAL_GUARD)) {
            originalSkill = SkillManager.getSkill(tag.getString(NBT_KEY_ORIGINAL_GUARD));
            tag.remove(NBT_KEY_ORIGINAL_GUARD);
        }
        return originalSkill;
    }
}