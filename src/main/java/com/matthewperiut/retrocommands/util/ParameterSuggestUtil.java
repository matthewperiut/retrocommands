package com.matthewperiut.retrocommands.util;

import com.matthewperiut.retrocommands.RetroCommands;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
        return suggestPlayerName(currentInput, "");
    }

    public static String[] suggestPlayerName(String currentInput, String remove) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (RetroCommands.player_names != null) {
                // Create a new ArrayList based on the array of player names to allow modification
                ArrayList<String> playerNames = new ArrayList<>(Arrays.asList(RetroCommands.player_names));

                // Remove the specified name if it exists
                if (!remove.isEmpty()) {
                    playerNames.removeIf(name -> name.equals(remove));
                }

                // Filter and return names that start with the current input
                return playerNames.stream()
                        .filter(a -> a.startsWith(currentInput))
                        .map(b -> b.substring(currentInput.length()))
                        .toArray(String[]::new);
            }
        }

        // Return an empty array if the client is not in the correct environment or player names are null
        return new String[0];
    }

}
