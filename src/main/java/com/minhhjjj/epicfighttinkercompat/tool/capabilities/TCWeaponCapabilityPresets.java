package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.function.Function;

import com.minhhjjj.epicfighttinkercompat.compat.p1nerobow.EFBowAnimations;
import com.minhhjjj.epicfighttinkercompat.gameasset.EFTAnimations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.Collider;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;

public class TCWeaponCapabilityPresets {
	public static final Collider SLEDGE_HAMMER = ColliderPreset.registerCollider(ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "sledge_hammer"), new MultiOBBCollider(3, 0.6D, 0.6D, 0.5D, 0D, 0D, -1.5D));

	public static final Function<Item, CapabilityItem.Builder> TC_SLEDGE_HAMMER = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.GREATSWORD)
			.collider(SLEDGE_HAMMER)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.canBePlacedOffhand(false)
			.reach(1.0F)
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.newStyleCombo(Styles.TWO_HAND, Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.STEEL_WHIRLWIND)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> TC_SWORD = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.SWORD)
			.collider(ColliderPreset.SWORD)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.styleProvider((patch) -> {
				if (patch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD) {
					return Styles.TWO_HAND;
				}
				return Styles.ONE_HAND;
			})
			.newStyleCombo(Styles.ONE_HAND, Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.SWEEPING_EDGE)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.weaponCombinationPredicator((entitypatch) -> entitypatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD);

	public static final Function<Item, CapabilityItem.Builder> TC_AXE = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.AXE)
			.collider(ColliderPreset.TOOLS)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.AXE_AUTO1, Animations.AXE_AUTO2, Animations.AXE_DASH, Animations.AXE_AIRSLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.GUILLOTINE_AXE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> TC_PICKAXE = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.PICKAXE)
			.collider(ColliderPreset.TOOLS)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.AXE_AUTO1, Animations.AXE_AUTO2, Animations.AXE_DASH, Animations.AXE_AIRSLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK);

	public static final Function<Item, CapabilityItem.Builder> TC_DAGGER = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.DAGGER)
			.collider(ColliderPreset.DAGGER)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.styleProvider((patch) -> patch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.DAGGER ? Styles.TWO_HAND : Styles.ONE_HAND)
			.weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == WeaponCategories.DAGGER)
			.newStyleCombo(Styles.ONE_HAND, Animations.DAGGER_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DASH, Animations.DAGGER_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.DAGGER_DUAL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.EVISCERATE)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.BLADE_RUSH)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON);

	public static final Function<Item, CapabilityItem.Builder> TC_SHIELD = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.SHIELD)
			.collider(ColliderPreset.FIST)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.canBePlacedOffhand(true)
			.reach(0.5F)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.alternativeUseAnimations(null, null)
			.livingMotionModifier(Styles.COMMON, LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);

	public static final Function<Item, CapabilityItem.Builder> SCYTHE = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.GREATSWORD)
			.collider(ColliderPreset.GREATSWORD)
			.canBePlacedOffhand(false)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.reach(1.0F)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.newStyleCombo(Styles.TWO_HAND, Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.STEEL_WHIRLWIND)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> CLEAVER = (item) -> TCWeaponCapability.builder()
			.canBePlacedOffhand(false)
			.category(WeaponCategories.GREATSWORD)
			.collider(ColliderPreset.GREATSWORD)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.reach(1.0F)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.newStyleCombo(Styles.TWO_HAND, Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.STEEL_WHIRLWIND)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> JAVELIN = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.SPEAR)
			.collider(ColliderPreset.SPEAR)
			.canBePlacedOffhand(false)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.reach(1.0F)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SHIELD ? Styles.ONE_HAND : Styles.TWO_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.SPEAR_ONEHAND_AUTO, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SPEAR_TWOHAND_AUTO1, Animations.SPEAR_TWOHAND_AUTO2, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.HEARTPIERCER)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.GRASPING_SPIRE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD);

	public static final Function<Item, CapabilityItem.Builder> LONGBOW = (item) -> TCWeaponCapability.builder()
			.newStyleCombo(Styles.TWO_HAND, EFBowAnimations.getComboAttack())
			.category(WeaponCategories.BOW)
			.collider(ModList.get().isLoaded("p1nero_bow") ? EFBowAnimations.BOW_SCAN_LEVEL0 : ColliderPreset.FIST)
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.livingMotionModifier(Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_IDLE)
			.livingMotionModifier(Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK)
			.alternativeUseAnimations(Animations.BIPED_BOW_AIM, Animations.BIPED_BOW_SHOT);

	public static final Function<Item, CapabilityItem.Builder> STAFF = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.TRIDENT)
			.collider(ColliderPreset.FIST)
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.livingMotionModifier(Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_IDLE)
			.livingMotionModifier(Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK)
			.livingMotionModifier(Styles.COMMON, LivingMotions.AIM, Animations.BIPED_JAVELIN_AIM)
			.livingMotionModifier(Styles.COMMON, LivingMotions.SHOT, Animations.BIPED_JAVELIN_THROW);

	public static final Function<Item, CapabilityItem.Builder> KATANA = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.TACHI)
			.collider(ColliderPreset.TACHI)
			.canBePlacedOffhand(false)
			.swingSound(EpicFightSounds.WHOOSH.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.reach(1.0F)
			.newStyleCombo(Styles.TWO_HAND, Animations.TACHI_AUTO1, Animations.TACHI_AUTO2, Animations.TACHI_AUTO3, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.RUSHING_TEMPO)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> FUMA_SHURIKEN = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.AXE)
			.collider(ColliderPreset.TOOLS)
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> SHURIKEN = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.FIST)
			.collider(ColliderPreset.FIST)
			.canBePlacedOffhand(true)
			.reach(0.4F)
			.zoomInType(CapabilityItem.ZoomInType.ALWAYS)
			.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> BATTLE_STAFF = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.SPEAR)
			.collider(ColliderPreset.SPEAR)
			.canBePlacedOffhand(true)
			.reach(1.5F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.SPEAR_ONEHAND_AUTO, Animations.SPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SPEAR_TWOHAND_AUTO1, Animations.SPEAR_TWOHAND_AUTO2, Animations.SPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.HEARTPIERCER)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.GRASPING_SPIRE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD);

	public static final Function<Item, CapabilityItem.Builder> FLAMBERGE = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.LONGSWORD)
			.collider(ColliderPreset.LONGSWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> {
				if (patch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SHIELD) {
					return Styles.ONE_HAND;
				}
				if (patch instanceof PlayerPatch<?> playerPatch && playerPatch.getSkill(SkillSlots.WEAPON_INNATE).isActivated()) {
					return Styles.OCHS;
				}
				return Styles.TWO_HAND;
			})
			.newStyleCombo(Styles.ONE_HAND, Animations.LONGSWORD_AUTO1, Animations.LONGSWORD_AUTO2, Animations.LONGSWORD_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.LONGSWORD_AUTO1, Animations.LONGSWORD_AUTO2, Animations.LONGSWORD_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.OCHS, Animations.LONGSWORD_LIECHTENAUER_AUTO1, Animations.LONGSWORD_LIECHTENAUER_AUTO2, Animations.LONGSWORD_LIECHTENAUER_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.SHARP_STAB)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.LIECHTENAUER)
			.innateSkill(Styles.OCHS, (itemstack) -> EpicFightSkills.LIECHTENAUER)
			.livingMotionModifier(Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.CHASE, Animations.BIPED_WALK_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.RUN, Animations.BIPED_RUN_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.SNEAK, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.KNEEL, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.JUMP, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.SWIM, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.COMMON, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
			.livingMotionModifier(Styles.OCHS, LivingMotions.IDLE, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.WALK, Animations.BIPED_WALK_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.CHASE, Animations.BIPED_WALK_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.RUN, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.SNEAK, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.KNEEL, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.JUMP, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.OCHS, LivingMotions.SWIM, Animations.BIPED_HOLD_LIECHTENAUER)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
			.livingMotionModifier(Styles.OCHS, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);

	public static final Function<Item, CapabilityItem.Builder> WAND = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.SWORD)
			.collider(ColliderPreset.SWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.SWEEPING_EDGE)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON);

	public static final Function<Item, CapabilityItem.Builder> SWASHER = item -> TCWeaponCapability.builder()
			.category(WeaponCategories.SWORD)
			.collider(ColliderPreset.SWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.SWEEPING_EDGE)
			.innateSkill(Styles.TWO_HAND, (itemstack) -> EpicFightSkills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON)
			.alternativeUseAnimations(EFTAnimations.BIPED_SWASHER_AIM, EFTAnimations.BIPED_SWASHER_SHOT);

	public static final Function<Item, CapabilityItem.Builder> UNKNOWN = (item) -> TCWeaponCapability.builder()
			.category(WeaponCategories.FIST)
			.collider(ColliderPreset.SWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND)
			.newStyleCombo(Styles.ONE_HAND, Animations.FIST_AUTO1, Animations.FIST_AUTO2, Animations.FIST_AUTO3, Animations.FIST_DASH, Animations.FIST_AIR_SLASH)
			.innateSkill(Styles.ONE_HAND, (itemstack) -> EpicFightSkills.RELENTLESS_COMBO);
}