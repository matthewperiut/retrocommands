package com.matthewperiut.spc.api;

import com.matthewperiut.spc.util.SharedCommandSource;

public interface Command {
    void command(SharedCommandSource commandSource, String[] parameters);

    String name();

    default String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput)
    {
        return new String[0];
    }

    void manual(SharedCommandSource commandSource);

    default boolean isOnlyServer()
    {
        return false;
    }
}
