package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PosParse;
import com.matthewperiut.spc.api.SummonRegistry;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerBase;

import java.util.HashMap;
import java.util.Map;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Summon implements Command {

    public static Map<Class<? extends EntityBase>, String> help = new HashMap<>();

    @Override
    public void command(PlayerBase player, String[] parameters) {

        // help
        if (parameters.length == 2) {
            try {
                Class<? extends EntityBase> entityClass = (Class<? extends EntityBase>) EntityRegistry.STRING_ID_TO_CLASS.get(parameters[1]);
                String msg = "Usage is /summon " + parameters[1] + " {x} {y} {z} ";
                if (help.containsKey(entityClass)) {
                    msg += help.get(entityClass);
                }
                sendMessage(msg);
                return;
            } catch (Exception e) {
                sendMessage("Failure to find entity (probably not registered)");
            }
        }

        if (parameters.length > 4) {
            try {

                PosParse pos = new PosParse(player, 2, parameters);

                if (!pos.valid) {
                    sendMessage("Non-number position");
                    return;
                }


                EntityBase entity;
                String extraMsg = "";
                if (parameters.length > 5) {
                    try {
                        Class<? extends EntityBase> entityClass = (Class<? extends EntityBase>) EntityRegistry.STRING_ID_TO_CLASS.get(parameters[1]);
                        entity = SummonRegistry.create(entityClass, player.level, pos, parameters);
                        if (entity == null) {
                            sendMessage("Parameters caused entity to be null");
                        }
                    } catch (Exception e) {
                        sendMessage("Failure to create entity (probably not registered)");
                        return;
                    }
                } else {
                    entity = EntityRegistry.create(parameters[1], player.level);
                }

                entity.setPosition(pos.x, pos.y, pos.z);

                player.level.spawnEntity(entity);
                sendMessage("Summoned " + parameters[1] + extraMsg + " at " + pos);
            } catch (Exception e) {
                System.out.println(e.getMessage());

                sendMessage("Invalid Entity");
            }
            return;
        }
        manual();
    }

    @Override
    public String name() {
        return "summon";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /summon {entity} {x} {y} {z}");
        sendMessage("Info: spawns a mob in the world at given position");
        sendMessage("entity: list of entities under /mobs");
        sendMessage("For a specific entity do /summon {entity} for parameters");
    }
}
