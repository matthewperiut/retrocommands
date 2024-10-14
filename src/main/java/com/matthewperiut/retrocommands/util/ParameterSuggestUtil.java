package com.matthewperiut.retrocommands.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParameterSuggestUtil {
    public static String[] suggestItemIdentifier(String currentInput)
    {
        if (!FabricLoader.getInstance().isModLoaded("station-registry-api-v0"))
            return new String[0];

        boolean autofillingModId = !currentInput.contains(":");
        String modId = "";
        if (!autofillingModId)
        {
            modId = currentInput.split(":")[0];
        }

        HashSet<String> outputs = new HashSet<>();
        for (Identifier id : ItemRegistry.INSTANCE.getIds())
        {
            if (autofillingModId)
            {
                if (id.namespace.toString().startsWith(currentInput))
                {
                    outputs.add(id.namespace.toString().substring(currentInput.length()) + ":");
                }
            }
            else
            {
                if (id.namespace.toString().equals(modId))
                {
                    String[] segments = currentInput.split(":");
                    if (segments.length > 1)
                    {
                        if (id.path.startsWith(segments[1]))
                        {
                            outputs.add(id.path.substring(segments[1].length()));
                        }
                    }
                }
            }
        }
        return outputs.toArray(new String[0]);
    }

    public static String[] suggestPlayerName(String currentInput) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            List players = ((Minecraft) FabricLoader.getInstance().getGameInstance()).world.players;
            ArrayList<String> playerNames = new ArrayList<>();
            players.forEach(player -> playerNames.add(((PlayerEntity) player).name));
            playerNames.stream().filter(a -> a.startsWith(currentInput)).map(b -> b.substring(currentInput.length()));
        }

        return new String[0];
    }
}
