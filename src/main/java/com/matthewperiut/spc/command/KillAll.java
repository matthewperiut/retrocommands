package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;

import java.util.ArrayList;
import java.util.List;


public class KillAll implements Command {

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerBase player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        List<EntityBase> entities_to_kill = new ArrayList<>();
        for (int i = 0; i < player.level.entities.size(); i++) {
            EntityBase e = (EntityBase) player.level.entities.get(i);
            if (e instanceof PlayerBase) continue;
            entities_to_kill.add(e);
        }

        for (EntityBase e : entities_to_kill) {
            e.damage(null, 1000);
        }

        commandSource.sendFeedback("Killed " + entities_to_kill.size() + " entities");
    }

    @Override
    public String name() {
        return "killall";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /killall");
        commandSource.sendFeedback("Info: Kills all entities in the world");
    }
}
