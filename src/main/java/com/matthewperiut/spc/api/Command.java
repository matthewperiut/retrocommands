package com.matthewperiut.spc.api;

import com.matthewperiut.spc.util.SharedCommandSource;

public interface Command {
    void command(SharedCommandSource commandSource, String[] parameters);

    String name();

    default String suggestion(int parameterNum, String currentInput, String totalInput)
    {
        return "";
    }

    void manual(SharedCommandSource commandSource);
}
