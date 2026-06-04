package com.periut.retrocommands.optionaldep.retroapi;

import com.periut.retroapi.registry.RetroRegistry;
import net.minecraft.item.Item;

import java.util.List;

/**
 * All direct RetroAPI references are isolated in this class so it is only class-loaded
 * behind {@code FabricLoader.getInstance().isModLoaded("retroapi")} checks — retrocommands
 * keeps working without RetroAPI installed.
 */
public final class RetroApiCompat {
    private RetroApiCompat() {
    }

    /**
     * All item identifiers RetroAPI can resolve, as "namespace:path" strings:
     * modded items, modded block-items, and the vanilla "minecraft:..." set.
     */
    public static List<String> itemIdentifiers() {
        return RetroRegistry.getItemIdentifierStrings();
    }

    /** Resolves "namespace:path" (modded or vanilla) to a numeric item id, or -1. */
    public static int identifierToItemId(String identifier) {
        Item item = RetroRegistry.getItemByStringId(identifier);
        return item == null ? -1 : item.id;
    }

    /** Max stack size for the item behind an identifier, or -1 if unknown. */
    public static int maxCountOf(String identifier) {
        Item item = RetroRegistry.getItemByStringId(identifier);
        return item == null ? -1 : item.getMaxCount();
    }
}
