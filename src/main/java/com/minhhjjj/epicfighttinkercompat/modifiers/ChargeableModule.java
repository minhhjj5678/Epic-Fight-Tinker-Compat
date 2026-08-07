package com.minhhjjj.epicfighttinkercompat.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import net.minecraft.world.item.UseAnim;

import java.util.List;

public class ChargeableModule implements ModifierModule, GeneralInteractionModifierHook, UsingToolModifierHook {

    public static final RecordLoadable<ChargeableModule> LOADER = RecordLoadable.create(
            IntLoadable.FROM_ONE.defaultField("duration", 72000, m -> m.duration),
            ChargeableModule::new
    );

    private final int duration;

    public ChargeableModule(int duration) {
        this.duration = duration;
    }

    @Override
    public @NotNull RecordLoadable<ChargeableModule> getLoader() {
        return LOADER;
    }

    // tools with this modifier can only perform charge skills
    @Override
    public Integer getPriority() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.GENERAL_INTERACT, ModifierHooks.TOOL_USING);
    }

    @Override
    public @NotNull InteractionResult onToolUse(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull Player player, @NotNull InteractionHand hand, @NotNull InteractionSource source) {
        GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier) {
        return this.duration;
    }

    @Override
    public @NotNull UseAnim getUseAction(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier) {
        return UseAnim.NONE;
    }
}