package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;

import java.util.ArrayList;
import java.util.List;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class KillAll implements Command
{

    @Override
    public void command(PlayerBase player, String[] parameters)
    {
        System.out.println("killall");
        List<EntityBase> entities_to_kill = new ArrayList<>();
        for (int i = 0; i < player.level.entities.size(); i++)
        {
            EntityBase e = (EntityBase) player.level.entities.get(i);
            if (e instanceof PlayerBase) continue;
            entities_to_kill.add(e);
        }

        for (EntityBase e : entities_to_kill)
        {
            e.damage(null, 1000);
        }

        sendMessage("Killed " + entities_to_kill.size() + " entities");
    }

    @Override
    public String name()
    {
        return "killall";
    }

    @Override
    public void manual()
    {
        sendMessage("Usage: /killall");
        sendMessage("Info: Kills all entities in the world");
    }
}
