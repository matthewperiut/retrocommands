package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Kill implements Command
{

    @Override
    public void command(PlayerBase player, String[] parameters)
    {
        player.damage(null, 1000);
        sendMessage("Killed player");
    }

    @Override
    public String name()
    {
        return "kill";
    }

    @Override
    public void manual()
    {
        sendMessage("Usage: /kill");
        sendMessage("Info: Kills the current player");
    }
}
