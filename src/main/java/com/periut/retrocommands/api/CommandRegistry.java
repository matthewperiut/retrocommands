package com.periut.retrocommands.api;

import com.periut.retrocommands.util.RetroChatUtil;

public class CommandRegistry {
    public static void add(Command command) {
        RetroChatUtil.commands.add(command);
    }
}
