package com.minhhjjj.epicfighttinkercompat.tool;

import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfile;
import com.minhhjjj.epicfighttinkercompat.gameasset.profiles.ModifierProfileReloadListener;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponTypeReloadListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ModifierProfileManager {
    private static final Map<Integer, Map<String, ModifierProfile>> CACHE = new HashMap<>();

    public record Filter(String id, Predicate<ModifierProfile> predicate) implements Predicate<ModifierProfile> {
        @Override
        public boolean test(ModifierProfile profile) {
            return predicate.test(profile);
        }
    }

    public static final Filter WEAPON_FILTER = new Filter("WEAPON", profile -> profile.weaponType() != null && WeaponTypeReloadListener.get(profile.weaponType()) != null);
    public static final Filter CATEGORY_FILTER = new Filter("CATEGORY", profile -> profile.weaponCategory() != null);

    public static Filter colliderFilter(ToolStack tool) {
        return new Filter("COLLIDER", profile -> profile.collider() != null && profile.collider().apply(tool) != null);
    }

    public static Filter innateSkillFilter(ToolStack tool, PlayerPatch<?> patch) {
        return new Filter("INNATE_SKILL", profile -> profile.innateSkill() != null && profile.innateSkill().apply(tool, patch) != null);
    }

    public static ModifierProfile getProfile(ToolStack toolStack, Filter filter) {
        ModifierProfile foundProfile = null;
        if (toolStack != null) {
            List<ModifierEntry> modifierList = toolStack.getModifierList();
            int listKey = modifierList.hashCode();
            String filterKey = filter == null ? "NONE" : filter.id();

            if (CACHE.computeIfAbsent(listKey, k -> new HashMap<>()).containsKey(filterKey)) {
                foundProfile = CACHE.get(listKey).get(filterKey);
            } else {
                for (ModifierEntry entry : toolStack.getModifierList()) {
                    if (entry.getLevel() <= 0) continue;
                    ModifierProfile profile = ModifierProfileReloadListener.get(entry.getId());
                    if (profile != null && (foundProfile == null || profile.priority() > foundProfile.priority())) {
                        if (filter != null && !filter.test(profile)) {
                            continue;
                        }
                        foundProfile = profile;
                    }
                }
                CACHE.computeIfAbsent(listKey, k -> new HashMap<>()).put(filterKey, foundProfile);
            }
        }

        return foundProfile;
    }

    public static ModifierProfile getProfile(ItemStack stack, Filter filter) {
        if (stack != null && stack.getItem() instanceof ModifiableItem) {
            return getProfile(ToolStack.from(stack), filter);
        }
        return null;
    }
}
