package com.matthewperiut.retrocommands.command.vanilla;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.api.PosParse;
import com.matthewperiut.retrocommands.api.SummonRegistry;
import com.matthewperiut.retrocommands.command.extra.Mobs;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Summon implements Command {

    public static Map<Class<? extends Entity>, String> help = new HashMap<>();

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        if (parameters.length == 2) {
            try {
                Entity entity;
                PosParse pos = new PosParse(player);

                entity = EntityRegistry.create(parameters[1], player.world);
                entity.setPosition(pos.x, pos.y, pos.z);
                player.world.spawnEntity(entity);

                commandSource.sendFeedback("Summoned " + parameters[1] + " at " + pos);
                return;
            } catch (Exception e) {
                commandSource.sendFeedback("Failure to find entity (probably not registered)");
            }
        }

        if (parameters.length == 3) {
            try {
                int spawnAmount = Integer.parseInt(parameters[2]);

                try {
                    PosParse pos = new PosParse(player);

                    for (int spawnIndex = 0; spawnIndex < spawnAmount; spawnIndex++) {
                        Entity entity;
                        entity = EntityRegistry.create(parameters[1], player.world);

                        double degreeInterval = 360 / spawnAmount;
                        double degreeOffset = degreeInterval * spawnIndex;
                        double xOffset = Math.sin(degreeOffset) * ((double)spawnAmount / 4);
                        double zOffset = Math.cos(degreeOffset) * ((double)spawnAmount / 4);

                        entity.setPosition(pos.x + xOffset, pos.y, pos.z + zOffset);

                        player.world.spawnEntity(entity);
                    }

                    commandSource.sendFeedback("Summoned " + spawnAmount + " " + parameters[1] + "s around " + pos);
                    return;
                } catch (Exception e) {
                    commandSource.sendFeedback("Failure to find entity (probably not registered)");
                }
            } catch (NumberFormatException e) {
                commandSource.sendFeedback("Non-number amount");
                return;
            }
        }

        if (parameters.length > 4) {
            try {
                PosParse pos = new PosParse(player, 2, parameters);

                if (!pos.valid) {
                    commandSource.sendFeedback("Non-number position");
                    return;
                }

                Entity entity;
                String extraMsg = "";
                if (parameters.length > 5) {
                    try {
                        Class<? extends Entity> entityClass = (Class<? extends Entity>) EntityRegistry.idToClass.get(parameters[1]);
                        entity = SummonRegistry.create(entityClass, player.world, pos, parameters);
                        if (entity == null) {
                            commandSource.sendFeedback("Parameters caused entity to be null");
                        }
                    } catch (Exception e) {
                        commandSource.sendFeedback("Failure to create entity (probably not registered)");
                        return;
                    }
                } else {
                    entity = EntityRegistry.create(parameters[1], player.world);
                }
                entity.setPosition(pos.x, pos.y, pos.z);
                player.world.spawnEntity(entity);

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
        commandSource.sendFeedback("Usage: /summon {entity}");
        commandSource.sendFeedback("Usage: /summon {entity} {amount}");
        commandSource.sendFeedback("Usage: /summon {entity} {x} {y} {z} {optional:parameters}");
        commandSource.sendFeedback("Info: spawns a mob or mobs into the world");
        commandSource.sendFeedback("entity: list of entities under /mobs");
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
