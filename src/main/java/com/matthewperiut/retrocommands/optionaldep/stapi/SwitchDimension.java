package com.matthewperiut.retrocommands.optionaldep.stapi;

import com.matthewperiut.retrocommands.dimension.BareTravelAgent;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;


public class SwitchDimension {
    public static void go(SharedCommandSource commandSource, String id) {
        Identifier dim = Identifier.tryParse(id);
        if (DimensionRegistry.INSTANCE.get(dim) == null) {
            commandSource.sendFeedback("Unknown dimension: " + id);
        } else {
            DimensionHelper.switchDimension(commandSource.getPlayer(), dim, 1, new BareTravelAgent());
        }
    }
}
