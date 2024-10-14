package com.matthewperiut.retrocommands.api;

import com.matthewperiut.retrocommands.util.SharedCommandSource;

public interface Command {
    void command(SharedCommandSource commandSource, String[] parameters);

    String name();

    default String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput)
    {
        return new String[0];
    }

    void manual(SharedCommandSource commandSource);

    default boolean disableInSingleplayer()
    {
        return false;
    }

    default boolean
    needsPermissions() {
        return true;
    }
}
