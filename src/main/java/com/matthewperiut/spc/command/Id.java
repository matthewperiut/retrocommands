package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;


public class Id implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length > 1) {
            int i = Give.nameToItemId(parameters[1]);
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
        commandSource.sendFeedback("Usage: /id {item name}");
        commandSource.sendFeedback("Info: get the ID number of a given item");
        commandSource.sendFeedback("item name: use the translated item name without spaces, or with '_' instead");
    }
}
