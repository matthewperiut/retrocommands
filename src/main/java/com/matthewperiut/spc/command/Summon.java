package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PosParse;
import com.matthewperiut.spc.api.SummonRegistry;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Summon implements Command {

    public static Map<Class<? extends EntityBase>, String> help = new HashMap<>();

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerBase player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        // help
        if (parameters.length == 2) {
            try {
                Class<? extends EntityBase> entityClass = (Class<? extends EntityBase>) EntityRegistry.STRING_ID_TO_CLASS.get(parameters[1]);
                String msg = "Usage is /summon " + parameters[1] + " {x} {y} {z} ";
                if (help.containsKey(entityClass)) {
                    msg += help.get(entityClass);
                }
                commandSource.sendFeedback(msg);
                return;
            } catch (Exception e) {
                commandSource.sendFeedback("Failure to find entity (probably not registered)");
            }
        }

        if (parameters.length > 4) {
            try {

                PosParse pos = new PosParse(player, 2, parameters);

                if (!pos.valid) {
                    commandSource.sendFeedback("Non-number position");
                    return;
                }


                EntityBase entity;
                String extraMsg = "";
                if (parameters.length > 5) {
                    try {
                        Class<? extends EntityBase> entityClass = (Class<? extends EntityBase>) EntityRegistry.STRING_ID_TO_CLASS.get(parameters[1]);
                        entity = SummonRegistry.create(entityClass, player.level, pos, parameters);
                        if (entity == null) {
                            commandSource.sendFeedback("Parameters caused entity to be null");
                        }
                    } catch (Exception e) {
                        commandSource.sendFeedback("Failure to create entity (probably not registered)");
                        return;
                    }
                } else {
                    entity = EntityRegistry.create(parameters[1], player.level);
                }

                entity.setPosition(pos.x, pos.y, pos.z);

                player.level.spawnEntity(entity);
                commandSource.sendFeedback("Summoned " + parameters[1] + extraMsg + " at " + pos);
            } catch (Exception e) {
                System.out.println(e.getMessage());

                commandSource.sendFeedback("Invalid Entity");
            }
            return;
        }
        manual(commandSource);
    }

    @Override
    public String name() {
        return "summon";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /summon {entity} {x} {y} {z}");
        commandSource.sendFeedback("Info: spawns a mob in the world at given position");
        commandSource.sendFeedback("entity: list of entities under /mobs");
        commandSource.sendFeedback("For a specific entity do /summon {entity} for parameters");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput)
    {
        if (parameterNum == 1)
        {
            Map<String, Class> map = Mobs.getMobSet();

            ArrayList<String> outputs = new ArrayList<>();
            String msg = "";
            for (Map.Entry<String, Class> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(currentInput)) {
                    outputs.add(key.substring(currentInput.length()));
                }
            }
            return outputs.toArray(new String[0]);
        }
        else if (parameterNum > 1 && parameterNum < 5)
        {
            if (currentInput.length() == 0)
            {
                return new String[]{"~"};
            }
        }

        return new String[0];
    }
}
