package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import com.minhhjjj.epicfighttinkercompat.skill.AutoGuardPassiveSkill;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.UseAnim;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.gameasset.animations.weapons.AnimsAgony;
import reascer.wom.gameasset.animations.weapons.AnimsRuine;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.ex_cap.core.data.MoveSet;
import yesman.epicfight.api.ex_cap.core.data.MoveSet.MoveSetBuilder;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.guard.GuardSkill.BlockType;

public class TCMoveSets {
	private TCMoveSets() {
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder sword1HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH})
		.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.SWEEPING_EDGE)
		.addGuardAnimations(BlockType.GUARD, Animations.SWORD_GUARD, Animations.SWORD_GUARD_HIT)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_GUARD)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder sword2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH})
		.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.DANCING_EDGE)
		.addGuardAnimations(BlockType.GUARD, Animations.SWORD_DUAL_GUARD, Animations.SWORD_DUAL_GUARD_HIT)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder swordMountSet() {
		return MoveSet.builder()
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder pickaxeSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.TOOL_AUTO1, Animations.TOOL_AUTO2, Animations.TOOL_DASH})
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder axe1HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.AXE_AUTO1, Animations.AXE_AUTO2, Animations.AXE_DASH, Animations.AXE_AIRSLASH})
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_SWIM);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder dagger1HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.DAGGER_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH})
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder dagger2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.DAGGER_DUAL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH})
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_DUAL_WEAPON, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE, LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);
	}

	public static MoveSetBuilder shieldSet() {
		return MoveSet.builder()
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK)
		.addLivingMotionModifier(LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder javelin1H() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.TRIDENT_AUTO1, Animations.TRIDENT_AUTO2, Animations.TRIDENT_AUTO3, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM).addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder spear1HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_ONEHAND_AUTO, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((item, patch) -> EpicFightSkills.HEARTPIERCER)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder spear2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_TWOHAND_AUTO1, Animations.SPEAR_TWOHAND_AUTO2, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH})
		.addGuardAnimations(BlockType.GUARD, Animations.SPEAR_GUARD, Animations.SPEAR_GUARD_HIT)
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((item, patch) -> EpicFightSkills.GRASPING_SPIRE)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}
	
	@SuppressWarnings("unchecked")
	public static MoveSetBuilder agonyGroundSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_AUTO_1, AnimsAgony.AGONY_AUTO_2, AnimsAgony.AGONY_AUTO_3, AnimsAgony.AGONY_AUTO_4, AnimsAgony.AGONY_CLAWSTRIKE, AnimsAgony.AGONY_RIPPING_FANGS})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((itemStack, playerPatch) -> WOMSkills.AGONY_PLUNGE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_GUARD, AnimsAgony.AGONY_GUARD_HIT_1, AnimsAgony.AGONY_GUARD_HIT_2})
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsAgony.AGONY_WALK)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.CHASE, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsAgony.AGONY_GUARD);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder agonyAirSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_AIR_ATTACK_1, AnimsAgony.AGONY_AIR_ATTACK_2, AnimsAgony.AGONY_AIR_ATTACK_3, AnimsAgony.AGONY_AIR_ATTACK_4, AnimsAgony.AGONY_RIPPING_FANGS})
		.addInnateSkill((itemStack, playerPatch) -> WOMSkills.AGONY_PLUNGE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_GUARD_HIT_1, AnimsAgony.AGONY_GUARD_HIT_2})
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsAgony.AGONY_WALK)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.CHASE, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsAgony.AGONY_GUARD);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder cleaver2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH})
		.addGuardAnimations(BlockType.GUARD, Animations.GREATSWORD_GUARD, Animations.GREATSWORD_GUARD_HIT)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.GREATSWORD_GUARD_BREAK)
		.addInnateSkill((itemstack, playerpatch) -> EpicFightSkills.STEEL_WHIRLWIND)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK_GREATSWORD, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder ruine2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_AUTO_1, AnimsRuine.RUINE_AUTO_2, AnimsRuine.RUINE_AUTO_3, AnimsRuine.RUINE_AUTO_4, AnimsRuine.RUINE_CHATIMENT, AnimsRuine.RUINE_COMET})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.RUINE_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.ENDER_ARCANE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_GUARD, AnimsRuine.RUINE_BLOCK_1, AnimsRuine.RUINE_BLOCK_2})
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsRuine.RUINE_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsRuine.RUINE_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsRuine.RUINE_WALK)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsRuine.RUINE_GUARD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.CHASE, LivingMotions.SNEAK, LivingMotions.KNEEL, LivingMotions.JUMP, LivingMotions.SWIM});
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder ruineOchsSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_AUTO_1, AnimsRuine.RUINE_AUTO_2, AnimsRuine.RUINE_AUTO_3, AnimsRuine.RUINE_AUTO_4, AnimsRuine.RUINE_CHATIMENT, AnimsRuine.RUINE_COMET})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.RUINE_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.ENDER_ARCANE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_BLOCK_1, AnimsRuine.RUINE_BLOCK_2})
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsRuine.RUINE_BOOSTED_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsRuine.RUINE_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsRuine.RUINE_BOOSTED_WALK)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsRuine.RUINE_GUARD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.CHASE, LivingMotions.SNEAK, LivingMotions.KNEEL, LivingMotions.JUMP, LivingMotions.SWIM});
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder sledgehammerSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH})
		.addInnateSkill((itemstack, playerpatch) -> EpicFightSkills.STEEL_WHIRLWIND)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.GREATSWORD_GUARD_BREAK)
		.addGuardAnimations(BlockType.GUARD, Animations.GREATSWORD_GUARD, Animations.GREATSWORD_GUARD_HIT)
		.setPassiveSkill(AutoGuardPassiveSkill.AUTO_GUARD_PASSIVE)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK_GREATSWORD, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);		
	}

    @SuppressWarnings("unchecked")
	public static MoveSetBuilder scythe2HSet() {
	return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH})
		.addGuardAnimations(BlockType.GUARD, Animations.GREATSWORD_GUARD, Animations.GREATSWORD_GUARD_HIT)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.GREATSWORD_GUARD_BREAK)
		.addInnateSkill((itemstack, playerpatch) -> EpicFightSkills.STEEL_WHIRLWIND)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK_GREATSWORD, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
    }

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder torment2HSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_AUTO_1, WOMAnimations.TORMENT_AUTO_2, WOMAnimations.TORMENT_AUTO_3, WOMAnimations.TORMENT_AUTO_4, WOMAnimations.TORMENT_DASH, WOMAnimations.TORMENT_AIRSLAM})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.TORMENT_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.TRUE_BERSERK)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_DASH})
		.addLivingMotionModifier(LivingMotions.IDLE, WOMAnimations.TORMENT_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, WOMAnimations.TORMENT_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, WOMAnimations.TORMENT_WALK)
		.addLivingMotionModifier(LivingMotions.CHASE, WOMAnimations.TORMENT_RUN)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, WOMAnimations.TORMENT_DASH);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder tormentOchsSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_BERSERK_AUTO_1, WOMAnimations.TORMENT_BERSERK_AUTO_2, WOMAnimations.TORMENT_BERSERK_DASH, WOMAnimations.TORMENT_BERSERK_AIRSLAM})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.TORMENT_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.TRUE_BERSERK)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_BERSERK_DASH})
		.addLivingMotionModifier(LivingMotions.IDLE, WOMAnimations.TORMENT_BERSERK_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, WOMAnimations.TORMENT_BERSERK_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, WOMAnimations.TORMENT_BERSERK_WALK)
		.addLivingMotionModifier(LivingMotions.CHASE, WOMAnimations.TORMENT_BERSERK_RUN)
		.addLivingMotionModifier(LivingMotions.BLOCK, WOMAnimations.TORMENT_BERSERK_DASH)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder longbowSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3, Animations.FIST_DASH, Animations.FIST_AIR_SLASH})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
		.addLivingMotionModifier(LivingMotions.IDLE, Animations.BIPED_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, Animations.BIPED_WALK)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() && ((LivingEntity)entityPatch.getOriginal()).getUseItem().getUseAnimation() == UseAnim.BOW ? LivingMotions.AIM : null);
	}

	@SuppressWarnings("unchecked")
	public static MoveSetBuilder staffSet() {
		return MoveSet.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.TRIDENT_AUTO1, Animations.TRIDENT_AUTO2, Animations.TRIDENT_AUTO3, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
		.addLivingMotionModifier(LivingMotions.IDLE, Animations.BIPED_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, Animations.BIPED_WALK)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() && ((LivingEntity)entityPatch.getOriginal()).getUseItem().getUseAnimation() == UseAnim.SPEAR ? LivingMotions.AIM : null);
	}
}
