package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.EntityRegistry;

import java.util.Map;


public class Mobs implements Command {
    public static Map<String, Class> getMobSet()
    {
        return EntityRegistry.STRING_ID_TO_CLASS;
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        commandSource.sendFeedback("Mobs that can be spawned, Case-Sensitive usage!");
        Map<String, Class> map = getMobSet();

        String msg = "";
        for (Map.Entry<String, Class> entry : map.entrySet()) {
            msg += "\"" + entry.getKey() + "\", ";
            if (msg.length() > 50) {
                commandSource.sendFeedback(msg);
                msg = "";
            }
        }
        commandSource.sendFeedback(msg.substring(0, msg.length() - 2)); // removes last comma then sends
    }

    @Override
    public String name() {
        return "mobs";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /mobs");
        commandSource.sendFeedback("Info: gives the list of mobs");
    }
}
