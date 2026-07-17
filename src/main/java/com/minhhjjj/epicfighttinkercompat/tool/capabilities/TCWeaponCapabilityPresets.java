package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.function.Function;

import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.CombatProfiles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.Collider;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfiles;

public class TCWeaponCapabilityPresets {
	public static final Collider SLEDGE_HAMMER = ColliderPreset.registerCollider(ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "sledge_hammer"), new MultiOBBCollider(3, 0.6D, 0.6D, 0.5D, 0D, 0D, -1.5D));

	public static final Function<Item, CapabilityItem.Builder> TC_SLEDGE_HAMMER = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.sledgehammerSet())
		.category(WeaponCategories.GREATSWORD)
		.collider(SLEDGE_HAMMER)
		.styleProvider((patch) -> Styles.TWO_HAND)
		.swingSound(EpicFightSounds.WHOOSH_BIG.get())
		.canBePlacedOffhand(false)
		.reach(1.0F)
		.hitSound(EpicFightSounds.BLUNT_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLUNT.get());

	public static final Function<Item, CapabilityItem.Builder> TC_SWORD = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.sword1HSet())
		.addWeaponSet(Styles.TWO_HAND, CombatProfiles.sword2HSet())
		.category(WeaponCategories.SWORD)
		.collider(ColliderPreset.SWORD)
		.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.canBePlacedOffhand(true)
		.reach(1.0F)
		.styleProvider((patch) -> {
			if (TCWeaponUtils.getDynamicProperty(patch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.SWORD) {
				return Styles.TWO_HAND;
			}
			return Styles.ONE_HAND;
		});

	public static final Function<Item, CapabilityItem.Builder> TC_AXE = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.axe1HSet())
		.category(WeaponCategories.AXE)
		.collider(ColliderPreset.TOOLS)
		.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
		.hitSound(EpicFightSounds.BLUNT_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLUNT.get())
		.canBePlacedOffhand(true)
		.reach(1.0F)
		.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> TC_PICKAXE = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.pickaxeSet())
		.category(WeaponCategories.PICKAXE)
		.collider(ColliderPreset.TOOLS)
		.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
		.hitSound(EpicFightSounds.BLUNT_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLUNT.get())
		.canBePlacedOffhand(true)
		.reach(0.8F)
		.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> TC_DAGGER = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.dagger1HSet())
		.addWeaponSet(Styles.ONE_HAND, CombatProfiles.dagger1HSet())
		.addWeaponSet(Styles.TWO_HAND, CombatProfiles.dagger2HSet())
		.category(WeaponCategories.DAGGER)
		.collider(ColliderPreset.DAGGER)
		.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.canBePlacedOffhand(true)
		.reach(1.0F)
		.styleProvider((patch) -> TCWeaponUtils.getDynamicProperty(patch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.DAGGER ? Styles.TWO_HAND : Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> TC_SHIELD = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.shieldSet())
		.category(WeaponCategories.SHIELD)
		.collider(ColliderPreset.FIST)
		.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
		.canBePlacedOffhand(true)
		.reach(0.5F)
		.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> SCYTHE = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.scythe2HSet())
		.addModifier(ModifierProfiles.TORMENT)
		.category(WeaponCategories.GREATSWORD)
		.collider(ColliderPreset.GREATSWORD)
		.canBePlacedOffhand(false)
		.swingSound(EpicFightSounds.WHOOSH_BIG.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.reach(1.0F)
		.styleProvider((patch) -> Styles.TWO_HAND);

	public static final Function<Item, CapabilityItem.Builder> CLEAVER = (item) -> TCWeaponCapability.builder()
		.defaultMoveSet(CombatProfiles.cleaver2HSet())
		.addModifier(ModifierProfiles.RUINE)
		.canBePlacedOffhand(false)
		.category(WeaponCategories.GREATSWORD)
		.collider(ColliderPreset.GREATSWORD)
		.swingSound(EpicFightSounds.WHOOSH_BIG.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.reach(1.0F)
		.styleProvider((patch) -> Styles.TWO_HAND);

	public static final Function<Item, CapabilityItem.Builder> JAVELIN = (item) -> TCWeaponCapability.builder()
		.addModifier(ModifierProfiles.SPEARY)
		.addModifier(ModifierProfiles.AGONY)
		.defaultMoveSet(CombatProfiles.javelin1H())
		.category(WeaponCategories.SPEAR)
		.collider(ColliderPreset.SPEAR)
		.canBePlacedOffhand(false)
		.swingSound(EpicFightSounds.WHOOSH_BIG.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
		.reach(1.0F);

	public static final Function<Item, CapabilityItem.Builder> LONGBOW = (item) -> {
		TCWeaponCapability.Builder builder = TCWeaponCapability.builder();
		builder.defaultMoveSet(CombatProfiles.p1neroBow())
			.addModifier(ModifierProfiles.ARROW_TEMPEST)
			.category(WeaponCategories.BOW)
			.collider(ColliderPreset.FIST)
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND);
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> STAFF = (item) -> {
		TCWeaponCapability.Builder builder = TCWeaponCapability.builder();
		builder.defaultMoveSet(CombatProfiles.staffSet())
			.category(WeaponCategories.TRIDENT)
			.collider(ColliderPreset.FIST)
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(1.0F);
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> KATANA = (item) -> TCWeaponCapability.builder()
			.defaultMoveSet(CombatProfiles.tachiSet())
			.addWeaponSet(Styles.TWO_HAND, CombatProfiles.tachiSet())
			.category(WeaponCategories.TACHI)
			.collider(ColliderPreset.TACHI)
			.canBePlacedOffhand(false)
			.swingSound(EpicFightSounds.WHOOSH.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND)
			.reach(1.0F);

	public static final Function<Item, CapabilityItem.Builder> FUMA_SHURIKEN = (item) -> TCWeaponCapability.builder()
			.defaultMoveSet(CombatProfiles.axe1HSet())
			.addWeaponSet(Styles.ONE_HAND, CombatProfiles.axe1HSet())
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
			.defaultMoveSet(CombatProfiles.battlestaff())
			.addWeaponSet(Styles.TWO_HAND,  CombatProfiles.battlestaff())
			.category(WeaponCategories.SPEAR)
			.collider(ColliderPreset.SPEAR)
			.canBePlacedOffhand(true)
			.reach(1.5F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.TWO_HAND);

	public static final Function<Item, CapabilityItem.Builder> FLAMBERGE = (item) -> TCWeaponCapability.builder()
			.defaultMoveSet(CombatProfiles.flamberge2HSet())
			.addWeaponSet(Styles.ONE_HAND,  CombatProfiles.flamberge1HSet())
			.addWeaponSet(Styles.TWO_HAND,  CombatProfiles.flamberge2HSet())
			.addWeaponSet(Styles.OCHS,  CombatProfiles.flambergeOchsSet())
			.category(WeaponCategories.LONGSWORD)
			.collider(ColliderPreset.LONGSWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> {
				if (TCWeaponUtils.getDynamicProperty(patch, patch.getHoldingItemCapability(InteractionHand.OFF_HAND), InteractionHand.OFF_HAND, CapabilityItem::getWeaponCategory, TCWeaponCapability::getWeaponCategory) == WeaponCategories.SHIELD) {
					return Styles.ONE_HAND;
				}
				if (patch instanceof PlayerPatch<?> playerPatch && playerPatch.getSkill(SkillSlots.WEAPON_INNATE).isActivated()) {
					return Styles.OCHS;
				}
				return Styles.TWO_HAND;
			});

	public static final Function<Item, CapabilityItem.Builder> WAND = (item) -> TCWeaponCapability.builder()
			.defaultMoveSet(CombatProfiles.wand())
			.addWeaponSet(Styles.ONE_HAND,  CombatProfiles.wand())
			.category(WeaponCategories.SWORD)
			.collider(ColliderPreset.SWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND);

	public static final Function<Item, CapabilityItem.Builder> UNKNOWN = (item) -> TCWeaponCapability.builder()
			.defaultMoveSet(CombatProfiles.fist())
			.addWeaponSet(Styles.ONE_HAND,  CombatProfiles.fist())
			.category(WeaponCategories.FIST)
			.collider(ColliderPreset.SWORD)
			.reach(1.0F)
			.zoomInType(CapabilityItem.ZoomInType.USE_TICK)
			.styleProvider((patch) -> Styles.ONE_HAND);
}