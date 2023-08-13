package com.matthewperiut.spc.optionaldep.stapi;

import com.matthewperiut.spc.dimension.BareTravelAgent;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class SwitchDimension {
    public static void go(PlayerBase player, String id) {
        Identifier dim = Identifier.tryParse(id);
        if (DimensionRegistry.INSTANCE.get(dim) == null) {
            sendMessage("Unknown dimension: " + id);
        } else {
            DimensionHelper.switchDimension(player, dim, 1, new BareTravelAgent());
        }
    }
}
