package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PosParse;
import com.matthewperiut.spc.mixin.EntityAccessor;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.PrimedTnt;
import net.minecraft.entity.animal.Pig;
import net.minecraft.entity.animal.Sheep;
import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.Slime;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Summon implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {

        if (parameters.length > 4) {
            try {

                PosParse pos = new PosParse(player, 2, parameters);

                if (!pos.valid) {
                    sendMessage("Non-number position");
                    return;
                }

                EntityBase e = EntityRegistry.create(parameters[1], player.level);
                e.setPosition(pos.x, pos.y, pos.z);

                String extraMsg = "";
                if (parameters.length > 5) {
                    try {
                        extraMsg = EntityMetadataHandler(e, parameters);
                    } catch (Exception ignored) {
                    }

                    if (extraMsg.isEmpty()) {
                        sendMessage("Invalid meta");
                    }
                }

                player.level.spawnEntity(e);
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
    }


    private String EntityMetadataHandler(EntityBase e, String[] parameters) {
        String extraMsg = "";
        if (e instanceof Sheep sheep) {
            int meta = Integer.parseInt(parameters[5]);
            sheep.setColour(meta);
            extraMsg = ":" + meta;
        }
        if (e instanceof Creeper creeper) {
            int meta = Integer.parseInt(parameters[5]);
            if (meta != 0) {
                extraMsg = " with Lightning";
                meta = 1;
            } else {
                extraMsg = " without Lightning";
            }

            ((EntityAccessor) creeper).getDataTracker().setInt(17, (byte) meta);
        }
        if (e instanceof Pig pig) {
            int meta = Integer.parseInt(parameters[5]);
            if (meta != 0) {
                meta = 1;
                extraMsg = " with Saddle";
            } else {
                extraMsg = " without Saddle";
            }
            ((EntityAccessor) pig).getDataTracker().setInt(16, (byte) meta);
        }
        if (e instanceof Slime slime) {
            int meta = Integer.parseInt(parameters[5]);
            if (meta > 0) {
                extraMsg = " of size " + meta;
                ((EntityAccessor) slime).getDataTracker().setInt(16, (byte) meta);
            }
        }
        if (e instanceof PrimedTnt tnt) {
            int meta = Integer.parseInt(parameters[5]);
            if (meta > 0) {
                extraMsg = " with a fuse of " + meta + " ticks";
                tnt.fuse = meta;
            }
        }

        return extraMsg;
    }
}
