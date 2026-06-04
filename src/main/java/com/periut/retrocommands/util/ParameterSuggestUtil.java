package com.periut.retrocommands.util;

import com.periut.retrocommands.RetroCommands;
import com.periut.retrocommands.optionaldep.retroapi.RetroApiCompat;
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
        HashSet<String> identifiers = new HashSet<>();
        if (FabricLoader.getInstance().isModLoaded("station-registry-api-v0"))
        {
            collectStationIdentifiers(identifiers);
        }
        if (FabricLoader.getInstance().isModLoaded("retroapi"))
        {
            identifiers.addAll(RetroApiCompat.itemIdentifiers());
        }
        if (identifiers.isEmpty())
            return new String[0];

        boolean autofillingModId = !currentInput.contains(":");
        String modId = "";
        if (!autofillingModId)
        {
            modId = currentInput.split(":")[0];
        }

        HashSet<String> outputs = new HashSet<>();
        for (String id : identifiers)
        {
            int sep = id.indexOf(':');
            if (sep < 0) continue;
            String namespace = id.substring(0, sep);
            String path = id.substring(sep + 1);

            if (autofillingModId)
            {
                if (namespace.startsWith(currentInput))
                {
                    outputs.add(namespace.substring(currentInput.length()) + ":");
                }
            }
            else
            {
                if (namespace.equals(modId))
                {
                    String[] segments = currentInput.split(":");
                    String typedPath = segments.length > 1 ? segments[1] : "";
                    if (path.startsWith(typedPath))
                    {
                        outputs.add(path.substring(typedPath.length()));
                    }
                }
            }
        }
        return outputs.toArray(new String[0]);
    }

    /**
     * StationAPI references stay inside this method so its classes are only resolved
     * when station-registry-api-v0 is actually present.
     */
    private static void collectStationIdentifiers(HashSet<String> identifiers)
    {
        for (Identifier id : ItemRegistry.INSTANCE.getIds())
        {
            identifiers.add(id.namespace.toString() + ":" + id.path);
        }
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
