package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import com.minhhjjj.epicfighttinkercompat.mixin.itemspoof.common.MixinWeaponCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SkillToItemDictionary {
    private static final Map<Skill, WeaponSpoofProfile> SKILL_TO_ITEM_DICTIONARY = new HashMap<>();
    private static final ThreadLocal<WeaponSpoofProfile> BOX = new ThreadLocal<>();

    public record WeaponSpoofProfile(Item spoofItem, ResourceLocation weaponType) {}

    @SuppressWarnings("removal")
    public static void init(Map<Item, CompoundTag> tags, boolean printLog) {
        long start = System.nanoTime();
        SKILL_TO_ITEM_DICTIONARY.clear();

        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.getValue();
            if (item instanceof BlockItem) return;
            if (item.isEdible()) return;
            if (item instanceof ModifiableItem) return;

            ItemStack stack = new ItemStack(item);
            CapabilityItem cap = EpicFightCapabilities.getItemStackCapability(stack);
            if (cap == null || cap.isEmpty()) return;

            ResourceLocation itemRegistryName = entry.getKey().location();
            ResourceLocation weaponType = null;
            CompoundTag tag = tags.get(item);

            if (tag != null && tag.contains("type")) {
                weaponType = ResourceLocation.parse(tag.getString("type"));
            } else {
                for (Map.Entry<ResourceLocation, ItemKeywordReloadListener.ItemRegex> regexEntry : ItemKeywordReloadListener.getRegexes().entrySet()) {
                    if (regexEntry.getValue().matchesAny(itemRegistryName.toString())) {
                        weaponType = regexEntry.getKey();
                        break;
                    }
                }
            }

            if (weaponType == null) {
                weaponType = itemRegistryName;
            }

            if (cap instanceof WeaponCapability weaponCap) {
                Map<Style, Function<ItemStack, Skill>> innateSkillMap = ((MixinWeaponCapability) weaponCap).getInnateSkill();
                if (innateSkillMap != null && !innateSkillMap.isEmpty()) {
                    for (Function<ItemStack, Skill> skillFunc : innateSkillMap.values()) {
                        if (skillFunc == null) continue;
                        Skill skill = skillFunc.apply(stack);
                        if (isMatchingNamespace(skill, itemRegistryName)) {
                            SKILL_TO_ITEM_DICTIONARY.put(skill, new WeaponSpoofProfile(item, weaponType));
                        }
                    }
                }
            } else {
                try {
                    Skill skill = cap.getInnateSkill(null, stack);
                    if (isMatchingNamespace(skill, itemRegistryName)) {
                        SKILL_TO_ITEM_DICTIONARY.put(skill, new WeaponSpoofProfile(item, weaponType));
                    }
                } catch (Exception e) {
                    EpicFightTinkerCompat.LOGGER.warn("Failed to retrieve innate skill for item '{}' due to a null PlayerPatch in getInnateSkill(). Skipping this item for the dictionary.", itemRegistryName);
                }
            }

            Skill passiveSkill = cap.getPassiveSkill();
            if (isMatchingNamespace(passiveSkill, itemRegistryName)) {
                SKILL_TO_ITEM_DICTIONARY.put(passiveSkill, new WeaponSpoofProfile(item, weaponType));
            }
        });

        if (printLog) {
            EpicFightTinkerCompat.LOGGER.info("Loaded {} entries into dictionary in {} ms:",  SKILL_TO_ITEM_DICTIONARY.size(), (System.nanoTime() - start)/1E6);
//            int count = 1;
//            for (Map.Entry<Skill, WeaponSpoofProfile> entry : SKILL_TO_ITEM_DICTIONARY.entrySet()) {
//                EpicFightTinkerCompat.LOGGER.info("{}. {} -> {} | {}", count++, entry.getKey(), entry.getValue().spoofItem, entry.getValue().weaponType);
//            }
        }

    }

    private static boolean isMatchingNamespace(Skill skill, ResourceLocation item) {
        return (skill != null && skill.getRegistryName() != null && (skill.getRegistryName().getNamespace().equals(item.getNamespace())
                || (skill.getRegistryName().getNamespace().equals("epicfight") && item.getNamespace().equals("minecraft"))));
    }

    public static WeaponSpoofProfile getProfile() {
        return BOX.get();
    }

    public static void put(Skill skill) {
        WeaponSpoofProfile profile = SKILL_TO_ITEM_DICTIONARY.get(skill);
        if (profile != null) {
            BOX.set(profile);
        } else {
            BOX.remove();
        }
    }

    public static boolean isEmpty() {
        return BOX.get() == null;
    }

    public static void remove() {
        BOX.remove();
    }
}
