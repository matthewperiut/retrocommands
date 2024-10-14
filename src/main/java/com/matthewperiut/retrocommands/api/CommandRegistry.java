package com.matthewperiut.retrocommands.api;

import com.matthewperiut.retrocommands.util.SPChatUtil;

public class CommandRegistry {
    public static void add(Command command) {
        SPChatUtil.commands.add(command);
    }
}
