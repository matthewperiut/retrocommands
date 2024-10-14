package com.matthewperiut.retrocommands.api;

import com.matthewperiut.retrocommands.util.RetroChatUtil;

public class CommandRegistry {
    public static void add(Command command) {
        RetroChatUtil.commands.add(command);
    }
}
