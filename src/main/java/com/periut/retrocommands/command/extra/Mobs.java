package com.periut.retrocommands.command.extra;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;

import java.util.Map;


public class Mobs implements Command {
    public static Map<String, Class<? extends Entity>> getMobSet()
    {
        return EntityRegistry.idToClass;
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        commandSource.sendFeedback("Mobs that can be spawned, Case-Sensitive usage!");
        Map<String, Class<? extends Entity>> map = getMobSet();

        String msg = "";
        for (Map.Entry<String, Class<? extends Entity>> entry : map.entrySet()) {
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

    public boolean needsPermissions() {
        return false;
    }
}
