package com.minhhjjj.epicfighttinkercompat.tool.item;

import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public final class EFTToolDefinitions {
    public static final ToolDefinition ODACHI;
    public static final ToolDefinition ODACHI_SHEATH;
    public static final ToolDefinition UNSHEATHED_ODACHI;

    static {
        ODACHI = ToolDefinition.create(ItemRegistry.ODACHI);
        ODACHI_SHEATH = ToolDefinition.create(ItemRegistry.ODACHI_SHEATH);
        UNSHEATHED_ODACHI = ToolDefinition.create(ItemRegistry.UNSHEATHED_ODACHI);
    }

}
