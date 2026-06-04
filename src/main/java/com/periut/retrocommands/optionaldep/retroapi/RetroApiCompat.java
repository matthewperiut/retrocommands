package com.periut.retrocommands.optionaldep.retroapi;

import com.periut.retroapi.registry.BlockRegistration;
import com.periut.retroapi.registry.ItemRegistration;
import com.periut.retroapi.registry.RetroRegistry;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * All direct RetroAPI references are isolated in this class so it is only class-loaded
 * behind {@code FabricLoader.getInstance().isModLoaded("retroapi")} checks — retrocommands
 * keeps working without RetroAPI installed.
 */
public final class RetroApiCompat {
    private RetroApiCompat() {
    }

    /** All registered RetroAPI item + block identifiers as "namespace:path" strings. */
    public static List<String> itemIdentifiers() {
        List<String> ids = new ArrayList<>();
        for (ItemRegistration reg : RetroRegistry.getItems()) {
            ids.add(reg.getId().toString());
        }
        for (BlockRegistration reg : RetroRegistry.getBlocks()) {
            if (reg.getBlockItem() != null) {
                ids.add(reg.getId().toString());
            }
        }
        return ids;
    }

    /** Resolves "namespace:path" to a numeric item id, or -1 if not a RetroAPI identifier. */
    public static int identifierToItemId(String identifier) {
        for (ItemRegistration reg : RetroRegistry.getItems()) {
            if (reg.getId().toString().equals(identifier)) {
                return reg.getItem().id;
            }
        }
        for (BlockRegistration reg : RetroRegistry.getBlocks()) {
            if (reg.getBlockItem() != null && reg.getId().toString().equals(identifier)) {
                return reg.getBlockItem().id;
            }
        }
        return -1;
    }

    /** Max stack size for the item behind a RetroAPI identifier, or -1 if unknown. */
    public static int maxCountOf(String identifier) {
        int id = identifierToItemId(identifier);
        if (id < 0 || id >= Item.ITEMS.length || Item.ITEMS[id] == null) {
            return -1;
        }
        return Item.ITEMS[id].getMaxCount();
    }
}
