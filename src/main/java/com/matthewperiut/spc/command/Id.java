package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;

import static com.matthewperiut.spc.util.ParameterSuggestUtil.suggestItemIdentifier;


public class Id implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length > 1) {
            int i = FabricLoader.getInstance().isModLoaded("station-registry-api-v0") ?
                    Give.identifierToItemId(parameters[1]) :
                    Give.nameToItemId(parameters[1]);
            if (i > -1) {
                commandSource.sendFeedback(parameters[1] + " has an id of " + i);
            }
            return;
        }
        manual(commandSource);
    }

    @Override
    public String name() {
        return "id";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /id {item name/id}");
        commandSource.sendFeedback("Info: get the ID number of a given item");
        commandSource.sendFeedback("item name: use the translated item name without spaces, or with '_' instead");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1)
        {
            return suggestItemIdentifier(currentInput);
        }
        return new String[0];
    }

    public boolean needsPermissions() {
        return false;
    }
}
