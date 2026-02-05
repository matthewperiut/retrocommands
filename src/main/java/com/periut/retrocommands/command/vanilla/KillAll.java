package com.periut.retrocommands.command.vanilla;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;


public class KillAll implements Command {

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        List<Entity> entities_to_kill = new ArrayList<>();
        for (int i = 0; i < player.world.entities.size(); i++) {
            Entity e = (Entity) player.world.entities.get(i);
            if (e instanceof PlayerEntity) continue;
            entities_to_kill.add(e);
        }

        for (Entity e : entities_to_kill) {
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
