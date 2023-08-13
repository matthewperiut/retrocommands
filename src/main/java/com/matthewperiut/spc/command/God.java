package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import java.util.HashMap;
import java.util.Map;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class God implements Command {

    public static Map<String, Boolean> isPlayerInvincible = new HashMap<>();

    @Override
    public void command(PlayerBase player, String[] parameters) {
        boolean god = false;
        if (isPlayerInvincible.get(player.name) != null) {
            god = !isPlayerInvincible.get(player.name);
            isPlayerInvincible.replace(player.name, god);
        } else {
            isPlayerInvincible.put(player.name, true);
            god = true;
        }

        sendMessage("God mode " + (god ? "activated" : "deactivated"));
    }

    @Override
    public String name() {
        return "god";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /god");
        sendMessage("Info: Activates or deactivates invincibility");
    }
}
