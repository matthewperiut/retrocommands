package com.matthewperiut.spc.api;

import com.matthewperiut.spc.util.SPChatUtil;

public class CommandRegistry {
    public static void add(Command command) {
        SPChatUtil.commands.add(command);
    }
}
