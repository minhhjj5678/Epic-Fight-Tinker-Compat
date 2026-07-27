package com.minhhjjj.epicfighttinkercompat.gameasset.profiles;

import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTAnimations;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTSkills;
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
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.guard.GuardSkill.BlockType;

public class CombatProfiles {
	private CombatProfiles() {
	}

	public static CombatProfile.CombatProfileBuilder fist() {
		return CombatProfile.builder().addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3, Animations.FIST_DASH, Animations.FIST_AIR_SLASH});
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder sword1HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH})
		.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.SWEEPING_EDGE)
		.addGuardAnimations(BlockType.GUARD, Animations.SWORD_GUARD_HIT)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_GUARD)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder sword2HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH})
		.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.DANCING_EDGE)
		.addGuardAnimations(BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
		.canBeVisibleOffhand();
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder swordMountSet() {
		return CombatProfile.builder()
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder pickaxeSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.AXE_AUTO1, Animations.AXE_AUTO2, Animations.AXE_DASH, Animations.AXE_AIRSLASH})
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder axe1HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.AXE_AUTO1, Animations.AXE_AUTO2, Animations.AXE_DASH, Animations.AXE_AIRSLASH})
		.addInnateSkill((item, patch) -> EpicFightSkills.GUILLOTINE_AXE)
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_SWIM);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder dagger1HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.DAGGER_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH})
		.addInnateSkill((item, playerPatch) -> EpicFightSkills.EVISCERATE)
		.addLivingMotionsRecursive(Animations.BIPED_IDLE, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder dagger2HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.DAGGER_DUAL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH})
		.addInnateSkill((item, playerPatch) -> EpicFightSkills.BLADE_RUSH)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_DUAL_WEAPON, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE, LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
		.canBeVisibleOffhand();
	}

	public static CombatProfile.CombatProfileBuilder shieldSet() {
		return CombatProfile.builder()
		.addComboAttacks(Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3, Animations.FIST_DASH, Animations.FIST_AIR_SLASH)
		.addLivingMotionModifier(LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder javelin1H() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.TRIDENT_AUTO1, Animations.TRIDENT_AUTO2, Animations.TRIDENT_AUTO3, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addGuardAnimations(BlockType.GUARD, Animations.SPEAR_GUARD)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SPEAR_GUARD)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() ? LivingMotions.AIM : null);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder spear1HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_ONEHAND_AUTO, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((item, patch) -> EpicFightSkills.HEARTPIERCER)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder spear2HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_TWOHAND_AUTO1, Animations.SPEAR_TWOHAND_AUTO2, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH})
		.addGuardAnimations(BlockType.GUARD, Animations.SPEAR_GUARD_HIT)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((item, patch) -> EpicFightSkills.GRASPING_SPIRE)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_SPEAR, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SPEAR_GUARD)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() ? LivingMotions.AIM : null);
	}
	
	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder agonyGroundSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_AUTO_1, AnimsAgony.AGONY_AUTO_2, AnimsAgony.AGONY_AUTO_3, AnimsAgony.AGONY_AUTO_4, AnimsAgony.AGONY_CLAWSTRIKE, AnimsAgony.AGONY_RIPPING_FANGS})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
		.addInnateSkill((itemStack, playerPatch) -> WOMSkills.AGONY_PLUNGE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_GUARD_HIT_1, AnimsAgony.AGONY_GUARD_HIT_2})
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsAgony.AGONY_WALK)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.CHASE, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsAgony.AGONY_GUARD);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder agonyAirSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_AIR_ATTACK_1, AnimsAgony.AGONY_AIR_ATTACK_2, AnimsAgony.AGONY_AIR_ATTACK_3, AnimsAgony.AGONY_AIR_ATTACK_4, AnimsAgony.AGONY_RIPPING_FANGS})
		.addInnateSkill((itemStack, playerPatch) -> WOMSkills.AGONY_PLUNGE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsAgony.AGONY_GUARD_HIT_1, AnimsAgony.AGONY_GUARD_HIT_2})
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsAgony.AGONY_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsAgony.AGONY_WALK)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.CHASE, AnimsAgony.AGONY_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsAgony.AGONY_GUARD);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder cleaver2HSet() {
		return CombatProfile.builder()
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
	public static CombatProfile.CombatProfileBuilder ruine2HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_AUTO_1, AnimsRuine.RUINE_AUTO_2, AnimsRuine.RUINE_AUTO_3, AnimsRuine.RUINE_AUTO_4, AnimsRuine.RUINE_CHATIMENT, AnimsRuine.RUINE_COMET})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.RUINE_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.ENDER_ARCANE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_GUARD, AnimsRuine.RUINE_BLOCK_1, AnimsRuine.RUINE_BLOCK_2})
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsRuine.RUINE_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsRuine.RUINE_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsRuine.RUINE_WALK)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsRuine.RUINE_GUARD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.CHASE, LivingMotions.SNEAK, LivingMotions.KNEEL, LivingMotions.JUMP, LivingMotions.SWIM});
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder ruineOchsSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_AUTO_1, AnimsRuine.RUINE_AUTO_2, AnimsRuine.RUINE_AUTO_3, AnimsRuine.RUINE_AUTO_4, AnimsRuine.RUINE_CHATIMENT, AnimsRuine.RUINE_COMET})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.RUINE_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.ENDER_ARCANE)
		.addGuardAnimations(BlockType.GUARD, new AnimationManager.AnimationAccessor[]{AnimsRuine.RUINE_BLOCK_1, AnimsRuine.RUINE_BLOCK_2})
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addLivingMotionModifier(LivingMotions.IDLE, AnimsRuine.RUINE_BOOSTED_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, AnimsRuine.RUINE_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, AnimsRuine.RUINE_BOOSTED_WALK)
		.addLivingMotionModifier(LivingMotions.BLOCK, AnimsRuine.RUINE_GUARD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.CHASE, LivingMotions.SNEAK, LivingMotions.KNEEL, LivingMotions.JUMP, LivingMotions.SWIM});
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder sledgehammerSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH})
		.addInnateSkill((itemstack, playerpatch) -> EpicFightSkills.STEEL_WHIRLWIND)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
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
	public static CombatProfile.CombatProfileBuilder scythe2HSet() {
	return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH})
		.addGuardAnimations(BlockType.GUARD, Animations.GREATSWORD_GUARD, Animations.GREATSWORD_GUARD_HIT)
		.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
		.addInnateSkill((itemstack, playerpatch) -> EpicFightSkills.STEEL_WHIRLWIND)
		.addLivingMotionsRecursive(Animations.BIPED_HOLD_GREATSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.JUMP, LivingMotions.KNEEL, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLY, LivingMotions.CREATIVE_FLY, LivingMotions.CREATIVE_IDLE})
		.addLivingMotionsRecursive(Animations.BIPED_WALK_GREATSWORD, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE})
		.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
    }

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder torment2HSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_AUTO_1, WOMAnimations.TORMENT_AUTO_2, WOMAnimations.TORMENT_AUTO_3, WOMAnimations.TORMENT_AUTO_4, WOMAnimations.TORMENT_DASH, WOMAnimations.TORMENT_AIRSLAM})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.TORMENT_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.TRUE_BERSERK)
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
	public static CombatProfile.CombatProfileBuilder tormentOchsSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{WOMAnimations.TORMENT_BERSERK_AUTO_1, WOMAnimations.TORMENT_BERSERK_AUTO_2, WOMAnimations.TORMENT_BERSERK_DASH, WOMAnimations.TORMENT_BERSERK_AIRSLAM})
		.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SWORD_MOUNT_ATTACK})
		.setPassiveSkill(WOMSkills.TORMENT_PASSIVE)
		.addInnateSkill((itemstack, playerpatch) -> WOMSkills.TRUE_BERSERK)
		.addLivingMotionModifier(LivingMotions.IDLE, WOMAnimations.TORMENT_BERSERK_IDLE)
		.addLivingMotionModifier(LivingMotions.RUN, WOMAnimations.TORMENT_BERSERK_RUN)
		.addLivingMotionModifier(LivingMotions.WALK, WOMAnimations.TORMENT_BERSERK_WALK)
		.addLivingMotionModifier(LivingMotions.CHASE, WOMAnimations.TORMENT_BERSERK_RUN)
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
		.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder longbowSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3, Animations.FIST_DASH, Animations.FIST_AIR_SLASH})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
		.addLivingMotionModifier(LivingMotions.IDLE, Animations.BIPED_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, Animations.BIPED_WALK)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() && ((LivingEntity)entityPatch.getOriginal()).getUseItem().getUseAnimation() == UseAnim.BOW ? LivingMotions.AIM : null);
	}

	public static CombatProfile.CombatProfileBuilder p1neroBow() {
		return CombatProfile.builder()
				.addComboAttacks(EFBowAnimations.getComboAttack())
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
				.addLivingMotionModifier(LivingMotions.IDLE, Animations.BIPED_IDLE)
				.addLivingMotionModifier(LivingMotions.WALK, Animations.BIPED_WALK)
				.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() && ((LivingEntity)entityPatch.getOriginal()).getUseItem().getUseAnimation() == UseAnim.BOW ? LivingMotions.AIM : null);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder staffSet() {
		return CombatProfile.builder()
		.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.TRIDENT_AUTO1, Animations.TRIDENT_AUTO2, Animations.TRIDENT_AUTO3, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH})
		.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
		.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
		.addLivingMotionModifier(LivingMotions.IDLE, Animations.BIPED_IDLE)
		.addLivingMotionModifier(LivingMotions.WALK, Animations.BIPED_WALK)
		.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() && ((LivingEntity)entityPatch.getOriginal()).getUseItem().getUseAnimation() == UseAnim.SPEAR ? LivingMotions.AIM : null);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder katanaBaseSet() {
		return CombatProfile.builder()
				.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.UCHIGATANA_AUTO1, Animations.UCHIGATANA_AUTO2, Animations.UCHIGATANA_AUTO3, Animations.UCHIGATANA_DASH, Animations.UCHIGATANA_AIR_SLASH})
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_UCHIGATANA, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.SWIM, LivingMotions.FALL})
				.addLivingMotionsRecursive(Animations.BIPED_WALK_UCHIGATANA, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE, LivingMotions.SNEAK})
				.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.UCHIGATANA_GUARD)
				.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.BATTOJUTSU)
				.setPassiveSkill(EpicFightSkills.BATTOJUTSU_PASSIVE);
	}

	@SuppressWarnings("unchecked")
	public static CombatProfile.CombatProfileBuilder katanaSheathedSet() {
		return CombatProfile.builder()
				.addComboAttacks(new AnimationManager.AnimationAccessor[]{Animations.UCHIGATANA_SHEATHING_AUTO, Animations.UCHIGATANA_SHEATHING_DASH, Animations.UCHIGATANA_SHEATH_AIR_SLASH})
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_UCHIGATANA_SHEATHING, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.SWIM, LivingMotions.FALL})
				.addLivingMotionsRecursive(Animations.BIPED_WALK_UCHIGATANA_SHEATHING, new LivingMotion[]{LivingMotions.WALK, LivingMotions.CHASE, LivingMotions.SNEAK})
				.addLivingMotionModifier(LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA_SHEATHING)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.UCHIGATANA_GUARD)
				.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.BATTOJUTSU)
				.setPassiveSkill(EpicFightSkills.BATTOJUTSU_PASSIVE);
	}

	public static CombatProfile.CombatProfileBuilder tachiSet() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.TACHI_AUTO1, Animations.TACHI_AUTO2, Animations.TACHI_AUTO3, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH)
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_TACHI, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK, LivingMotions.SWIM, LivingMotions.FLOAT, LivingMotions.FALL})
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
				.addInnateSkill((itemStack, playerPatch) -> EpicFightSkills.RUSHING_TEMPO);
	}

	public static CombatProfile.CombatProfileBuilder battlestaff() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.SPEAR_TWOHAND_AUTO1, Animations.SPEAR_TWOHAND_AUTO2, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH)
				.addInnateSkill((item, patch) -> EpicFightSkills.GRASPING_SPIRE)
				.addGuardAnimations(BlockType.GUARD, Animations.SPEAR_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addMountAttacks(new AnimationManager.AnimationAccessor[]{Animations.SPEAR_MOUNT_ATTACK})
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_SPEAR, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK})
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionModifier(LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SPEAR_GUARD)
				.setMotionPredicate((entityPatch, interactionHand) -> ((LivingEntity)entityPatch.getOriginal()).isUsingItem() ? LivingMotions.AIM : null);
	}

	public static CombatProfile.CombatProfileBuilder flamberge2HSet() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.LONGSWORD_AUTO1, Animations.LONGSWORD_AUTO2, Animations.LONGSWORD_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
				.addInnateSkill((item, patch) -> EpicFightSkills.LIECHTENAUER)
				.addGuardAnimations(BlockType.GUARD, Animations.LONGSWORD_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_LONGSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK, LivingMotions.SWIM});
	}

	public static CombatProfile.CombatProfileBuilder flamberge1HSet() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.LONGSWORD_AUTO1, Animations.LONGSWORD_AUTO2, Animations.LONGSWORD_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
				.addInnateSkill((item, patch) -> EpicFightSkills.SHARP_STAB)
				.addGuardAnimations(BlockType.GUARD, Animations.LONGSWORD_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_LONGSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK, LivingMotions.SWIM});
	}

	public static CombatProfile.CombatProfileBuilder flambergeOchsSet() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.LONGSWORD_LIECHTENAUER_AUTO1, Animations.LONGSWORD_LIECHTENAUER_AUTO2, Animations.LONGSWORD_LIECHTENAUER_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
				.addInnateSkill((item, patch) -> EpicFightSkills.LIECHTENAUER)
				.addGuardAnimations(BlockType.GUARD, Animations.LONGSWORD_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_LIECHTENAUER, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK, LivingMotions.SWIM});
	}

	public static CombatProfile.CombatProfileBuilder wand() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
				.addGuardAnimations(BlockType.GUARD, Animations.SWORD_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addLivingMotionModifier(LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_GUARD)
				.addLivingMotionsRecursive(Animations.BIPED_HOLD_LONGSWORD, new LivingMotion[]{LivingMotions.IDLE, LivingMotions.KNEEL, LivingMotions.WALK, LivingMotions.FALL, LivingMotions.FLOAT, LivingMotions.FALL, LivingMotions.CHASE, LivingMotions.RUN, LivingMotions.SNEAK, LivingMotions.SWIM});
	}

	public static CombatProfile.CombatProfileBuilder swasher() {
		return CombatProfile.builder()
				.addComboAttacks(Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
				.addGuardAnimations(BlockType.GUARD, Animations.SWORD_GUARD_HIT)
				.addGuardAnimations(BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED)
				.addLivingMotionModifier(LivingMotions.AIM, EFTAnimations.BIPED_SWASHER_AIM)
				.addLivingMotionModifier(LivingMotions.SHOT, EFTAnimations.BIPED_SWASHER_SHOT)
				.addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_GUARD);
	}
}
