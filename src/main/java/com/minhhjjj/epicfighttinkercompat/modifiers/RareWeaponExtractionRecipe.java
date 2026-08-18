package com.minhhjjj.epicfighttinkercompat.modifiers;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.common.ItemStackLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.IMutableTinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class RareWeaponExtractionRecipe implements ITinkerStationRecipe {

    private static final LoadableField<List<SizedIngredient>, RareWeaponExtractionRecipe> INPUTS_FIELD = SizedIngredient.LOADABLE.list(1).requiredField("inputs", r -> r.inputs);
    private static final LoadableField<Ingredient, RareWeaponExtractionRecipe> TOOLS_FIELD = IngredientLoadable.DISALLOW_EMPTY.requiredField("tools", r -> r.toolRequirement);

    public record ExtractionMapping(ModifierId modifier, ItemStack returnItem) {
        public static final RecordLoadable<ExtractionMapping> LOADER = RecordLoadable.create(
                ModifierId.PARSER.requiredField("modifier", ExtractionMapping::modifier),
                ItemStackLoadable.REQUIRED_ITEM.requiredField("result", ExtractionMapping::returnItem),
                ExtractionMapping::new
        );
    }

    public static final RecordLoadable<RareWeaponExtractionRecipe> LOADER = RecordLoadable.create(
            ContextKey.ID.requiredField(),
            INPUTS_FIELD,
            TOOLS_FIELD,
            ExtractionMapping.LOADER.list(1).requiredField("mappings", r -> r.mappings),
            RareWeaponExtractionRecipe::new
    );

    private final ResourceLocation id;
    private final List<SizedIngredient> inputs;
    private final Ingredient toolRequirement;
    private final List<ExtractionMapping> mappings;

    public RareWeaponExtractionRecipe(ResourceLocation id, List<SizedIngredient> inputs, Ingredient toolRequirement, List<ExtractionMapping> mappings) {
        this.id = id;
        this.inputs = inputs;
        this.toolRequirement = toolRequirement;
        this.mappings = mappings;
    }

    private ExtractionMapping findMatchingMapping(ToolStack tool) {
        for (ExtractionMapping mapping : mappings) {
            if (tool.getUpgrades().getLevel(mapping.modifier()) > 0) {
                return mapping;
            }
        }
        return null;
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
        ItemStack tinkerItem = inv.getTinkerableStack();
        if (tinkerItem.isEmpty() || !(tinkerItem.getItem() instanceof IModifiable)) return false;
        if (!this.toolRequirement.test(tinkerItem)) return false;

        ToolStack tool = ToolStack.from(tinkerItem);

        if (findMatchingMapping(tool) == null) return false;

        return ModifierRecipe.checkMatch(inv, this.inputs);
    }

    @Override
    public @NotNull RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, @NotNull RegistryAccess access) {
        ToolStack tool = ToolStack.from(inv.getTinkerableStack()).copy();
        ExtractionMapping matchedMapping = findMatchingMapping(tool);

        if (matchedMapping == null) return RecipeResult.pass();

        tool.removeModifier(matchedMapping.modifier(), 1);
        tool.rebuildStats();

        return LazyToolStack.successCopy(tool, 1, inv.getTinkerableStack());
    }

    @Override
    public int shrinkToolSlotBy() {
        return 1;
    }

    @Override
    public void updateInputs(@NotNull LazyToolStack result, @NotNull IMutableTinkerStationContainer inv, boolean isServer) {
        ModifierRecipe.updateInputs(inv, this.inputs);

        if (isServer) {
            ToolStack tool = ToolStack.from(inv.getTinkerableStack());
            ExtractionMapping matchedMapping = findMatchingMapping(tool);

            if (matchedMapping != null) {
                inv.giveItem(matchedMapping.returnItem().copy());
            }
        }
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.EXTRACTION_SERIALIZER.get();
    }
}
