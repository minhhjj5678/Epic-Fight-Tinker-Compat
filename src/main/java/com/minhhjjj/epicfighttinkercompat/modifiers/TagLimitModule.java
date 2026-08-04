package com.minhhjjj.epicfighttinkercompat.modifiers;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;

import java.util.List;

public class TagLimitModule implements ModifierModule, ValidateModifierHook, RequirementsModifierHook {

    public static final RecordLoadable<TagLimitModule> LOADER = RecordLoadable.create(
            TinkerLoadables.MODIFIER_TAGS.requiredField("tag", m -> m.tag),
            IntLoadable.FROM_ONE.defaultField("limit", 1, m -> m.limit),
            StringLoadable.DEFAULT.requiredField("translation_key", m -> m.translationKey),
            TagLimitModule::new
    );

    private final TagKey<Modifier> tag;
    private final int limit;
    private final String translationKey;
    private final Component errorMessage;

    public TagLimitModule(TagKey<Modifier> tag, int limit, String translationKey) {
        this.tag = tag;
        this.limit = limit;
        this.translationKey = translationKey;
        this.errorMessage = Component.translatable(translationKey);
    }

    @Override
    public @NotNull RecordLoadable<TagLimitModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.VALIDATE_UPGRADE, ModifierHooks.REQUIREMENTS);
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, @NotNull ModifierEntry modifier) {
        int count = 0;

        for (ModifierEntry entry : tool.getModifierList()) {
            if (ModifierManager.isInTag(entry.getModifier().getId(), this.tag)) {
                count++;
            }
        }

        if (count > this.limit) {
            return this.errorMessage;
        }

        return null;
    }

    @Nullable
    @Override
    public Component requirementsError(@NotNull ModifierEntry entry) {
        return this.errorMessage;
    }
}