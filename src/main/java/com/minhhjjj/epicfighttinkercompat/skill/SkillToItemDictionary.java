package com.minhhjjj.epicfighttinkercompat.skill;

import com.minhhjjj.epicfighttinkercompat.EpicFightTinkerCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SkillToItemDictionary {
    private static final Map<Skill, ItemStack> SKILL_TO_ITEM_DICTIONARY = new HashMap<>();
    private static Field innateSkillField;
    private static ThreadLocal<ItemStack> BOX = new ThreadLocal<>();

    @SuppressWarnings("removal")
    public static void init(boolean printLog) {
        long start = System.currentTimeMillis();
        SKILL_TO_ITEM_DICTIONARY.clear();

        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.getValue();
            if (item instanceof BlockItem) return;
            if (item.isEdible()) return;
            if (item instanceof ModifiableItem) return;

            ItemStack stack = item.getDefaultInstance();
            CapabilityItem cap = EpicFightCapabilities.getItemStackCapability(stack);

            if (cap != null && cap != CapabilityItem.EMPTY) {
                ResourceLocation itemRegistryName = ForgeRegistries.ITEMS.getKey(item);
                if (cap instanceof WeaponCapability weaponCap) {
                    Map<Style, Function<ItemStack, Skill>> innateSkillMap = getInnateSkillField(weaponCap);
                    if (innateSkillMap != null && !innateSkillMap.isEmpty()) {
                        for (Function<ItemStack, Skill> skillFunc : innateSkillMap.values()) {
                            if (skillFunc == null) continue;
                            Skill skill = skillFunc.apply(stack);
                            if (skill != null && skill.getRegistryName() != null && itemRegistryName != null
                                    && skill.getRegistryName().getNamespace().equals(itemRegistryName.getNamespace())) {
                                SKILL_TO_ITEM_DICTIONARY.putIfAbsent(skill, stack);
                            }
                        }
                    }
                } else {
                    try {
                        Skill skill = cap.getInnateSkill(null, stack);
                        if (skill != null && skill.getRegistryName() != null && itemRegistryName != null
                                && skill.getRegistryName().getNamespace().equals(itemRegistryName.getNamespace())) {
                            SKILL_TO_ITEM_DICTIONARY.putIfAbsent(skill, stack);
                        }
                    } catch (Exception e) {
                        EpicFightTinkerCompat.LOGGER.warn("Failed to retrieve innate skill for item '{}' due to a null PlayerPatch in getInnateSkill(). Skipping this item for the dictionary.", itemRegistryName);
                    }
                }

                Skill passiveSkill = cap.getPassiveSkill();
                if (passiveSkill != null && passiveSkill.getRegistryName() != null && itemRegistryName != null
                        && passiveSkill.getRegistryName().getNamespace().equals(itemRegistryName.getNamespace())) {
                    SKILL_TO_ITEM_DICTIONARY.putIfAbsent(passiveSkill, stack);
                }
            }
        });

        if (printLog) {
            EpicFightTinkerCompat.LOGGER.info("Loaded {} entries into dictionary in {} ms:",  SKILL_TO_ITEM_DICTIONARY.size(), (System.currentTimeMillis() - start));
//            int count = 1;
//            for (Map.Entry<Skill, ItemStack> entry : SKILL_TO_ITEM_DICTIONARY.entrySet()) {
//                EpicFightTinkerCompat.LOGGER.info("{}. {} -> {}", count++, entry.getKey(), entry.getValue().getItem());
//            }
        }

    }

    public static ItemStack getItem() {
        return BOX.get();
    }

    public static void put(Skill skill) {
        BOX.set(SKILL_TO_ITEM_DICTIONARY.get(skill));
    }

    public static boolean isEmpty() {
        return BOX.get() == null;
    }

    public static void remove() {
        BOX.remove();
    }

    @SuppressWarnings("unchecked")
    public static Map<Style, Function<ItemStack, Skill>> getInnateSkillField(WeaponCapability cap) {
        if (innateSkillField == null) {
            try {
                innateSkillField = WeaponCapability.class.getDeclaredField("innateSkill");
                innateSkillField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                EpicFightTinkerCompat.LOGGER.warn("Failed to find innateSkill field", e);
            }
        }

        if (innateSkillField != null) {
            try {
                return (Map<Style, Function<ItemStack, Skill>>) innateSkillField.get(cap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }
}