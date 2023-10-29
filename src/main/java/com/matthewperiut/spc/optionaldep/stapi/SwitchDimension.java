package com.matthewperiut.spc.optionaldep.stapi;

import com.matthewperiut.spc.dimension.BareTravelAgent;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
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
