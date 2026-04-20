package com.minhhjjj.epicfighttinkercompat.tool.capabilities;

import java.util.List;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.ex_cap.core.provider.ProviderConditional;
import yesman.epicfight.api.ex_cap.core.provider.ProviderConditionalType;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.Collider;
import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;

public class TCWeaponCapabilityPresets {
	public static final Collider SLEDGE_HAMMER = ColliderPreset.registerCollider(ResourceLocation.fromNamespaceAndPath(EpicFightTinkerCompat.MODID, "sledge_hammer"), new MultiOBBCollider(3, 0.6D, 0.6D, 0.5D, 0D, 0D, -1.5D));

	public static final Function<Item, CapabilityItem.Builder> TC_SLEDGE_HAMMER = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.sledgehammerSet())
			.category(WeaponCategories.GREATSWORD)
			.collider(SLEDGE_HAMMER)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.TWO_HAND)
					.build()
			))
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.canBePlacedOffhand(false)
			.reach(1.0F)
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get());
		
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> TC_SWORD = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.sword1HSet())
			.addMoveSet(Styles.COMMON, TCMoveSets.sword2HSet())
			.addMoveSet(Styles.MOUNT, TCMoveSets.swordMountSet())
			.category(WeaponCategories.SWORD)
			.collider(ColliderPreset.SWORD)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.CUSTOM)
					.setWieldStyle(Styles.COMMON)
					.isVisibleOffHand(true)
					.setCustomFunction((patch) -> {
						ItemStack itemStack = patch.getOriginal().getOffhandItem();
						if (!itemStack.isEmpty()) {
							CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(itemStack);
							return itemCap != null && itemCap != CapabilityItem.EMPTY && itemCap.getWeaponCategory() == WeaponCategories.SWORD;
						}
						return false;
					})
					.build(),
				ProviderConditional.builder()
					.setType(ProviderConditionalType.CUSTOM)
					.setWieldStyle(Styles.MOUNT)
					.setCustomFunction((patch) -> patch.getOriginal().getOffhandItem().isEmpty() && patch.getOriginal().isPassenger())
					.build(),
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.ONE_HAND)
					.build()
			));

			return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> TC_AXE = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.axe1HSet())
			.category(WeaponCategories.AXE)
			.collider(ColliderPreset.TOOLS)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(1.0F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.ONE_HAND)
					.build()
			));
			return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> TC_PICKAXE = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.pickaxeSet())
			.category(WeaponCategories.PICKAXE)
			.collider(ColliderPreset.TOOLS)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.ONE_HAND)
					.build()
			));
			return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> TC_DAGGER = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.dagger1HSet())
			.category(WeaponCategories.DAGGER)
			.collider(ColliderPreset.DAGGER)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.canBePlacedOffhand(true)
			.offHandAlone(true)
			.reach(1.0F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.CUSTOM)
					.setWieldStyle(Styles.COMMON)
					.isVisibleOffHand(true)
					.setCustomFunction((patch) -> {
						ItemStack itemStack = patch.getOriginal().getOffhandItem();
						if (!itemStack.isEmpty()) {
							CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(itemStack);
							return itemCap != null && itemCap != CapabilityItem.EMPTY && itemCap.getWeaponCategory() == WeaponCategories.DAGGER;
						}
						return false;
					})
					.build(),
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.ONE_HAND)
					.build()
			))
			.addMoveSet(Styles.ONE_HAND, TCMoveSets.dagger1HSet())
			.addMoveSet(Styles.TWO_HAND, TCMoveSets.dagger2HSet());
			return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> TC_SHIELD = (item) -> {
		WeaponCapability.Builder builder = TCWeaponCapability.builder()
			.defaultMoveSet(TCMoveSets.shieldSet())
			.category(WeaponCategories.SHIELD)
			.collider(ColliderPreset.FIST)
			.swingSound(EpicFightSounds.WHOOSH_SMALL.get())
			.canBePlacedOffhand(true)
			.offHandAlone(true)
			.reach(0.5F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.ONE_HAND)
					.build()
			));
			return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> SCYTHE = (item) -> {
		TCWeaponCapability.Builder builder = (TCWeaponCapability.Builder)TCWeaponCapability.builder();
		builder.defaultMoveSet(TCMoveSets.scythe2HSet())
			.addModifier(TCModifierProfiles.TORMENT)
			.category(WeaponCategories.GREATSWORD)
			.collider(ColliderPreset.GREATSWORD)
			.canBePlacedOffhand(false)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.reach(1.0F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.TWO_HAND)
					.build()
			));
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> CLEAVER = (item) -> {
		TCWeaponCapability.Builder builder = (TCWeaponCapability.Builder)TCWeaponCapability.builder();
		builder.defaultMoveSet(TCMoveSets.cleaver2HSet())
			.addModifier(TCModifierProfiles.RUINE)
			.canBePlacedOffhand(false)
			.category(WeaponCategories.GREATSWORD)
			.collider(ColliderPreset.GREATSWORD)
			.swingSound(EpicFightSounds.WHOOSH_BIG.get())
			.hitSound(EpicFightSounds.BLADE_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLADE.get())
			.reach(1.0F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.TWO_HAND)
					.build()
			));
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> JAVELIN = (item) -> {
		TCWeaponCapability.Builder builder = TCWeaponCapability.builder();
		builder.addModifier(TCModifierProfiles.SPEARY)
		.addModifier(TCModifierProfiles.AGONY)
		.defaultMoveSet(TCMoveSets.javelin1H())
		.category(WeaponCategories.SPEAR)
		.collider(ColliderPreset.SPEAR)
		.canBePlacedOffhand(false)
		.swingSound(EpicFightSounds.WHOOSH_BIG.get())
		.hitSound(EpicFightSounds.BLADE_HIT.get())
		.hitParticle(EpicFightParticles.HIT_BLADE.get())
		.reach(1.0F);
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> LONGBOW = (item) -> {
		TCWeaponCapability.Builder builder = TCWeaponCapability.builder();
		builder.defaultMoveSet(TCMoveSets.longbowSet())
			.category(WeaponCategories.RANGED)
			.collider(ColliderPreset.FIST)
			.canBePlacedOffhand(true)
			.reach(0.8F)
			.addConditionals(List.of(
				ProviderConditional.builder()
					.setType(ProviderConditionalType.DEFAULT)
					.setWieldStyle(Styles.TWO_HAND)
					.build()
			));
		return builder;
	};

	public static final Function<Item, CapabilityItem.Builder> STAFF = (item) -> {
		TCWeaponCapability.Builder builder = TCWeaponCapability.builder();
		builder.defaultMoveSet(TCMoveSets.staffSet())
			.category(WeaponCategories.TRIDENT)
			.collider(ColliderPreset.FIST)
			.hitSound(EpicFightSounds.BLUNT_HIT.get())
			.hitParticle(EpicFightParticles.HIT_BLUNT.get())
			.canBePlacedOffhand(true)
			.reach(1.0F);
		return builder;
	};
}