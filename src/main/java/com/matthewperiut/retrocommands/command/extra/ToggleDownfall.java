package com.matthewperiut.retrocommands.command.extra;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

import java.lang.reflect.Field;


public class ToggleDownfall implements Command {
    // Thank you mine_diver for this code, it's beautiful

    private static final Field worldInfoField = getField(World.class, new String[]{"properties", "field_220"});

    // from HMI, blame mine_diver
    //clean mine_diver code
    //Used for easy reflection with obfuscated or regular fields
    public static Field getField(Class<?> target, String[] names) {
        for (Field field : target.getDeclaredFields()) {
            for (String name : names) {
                if (field.getName().equals(name)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        WorldProperties worldInfo = null;
        try {
            worldInfo = (WorldProperties) worldInfoField.get(player.world);
        } catch (Exception ignored) {

        }

        try {
            assert worldInfo != null;
            worldInfo.setThundering(!worldInfo.getThundering());
            worldInfo.setRaining(!worldInfo.getRaining());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        commandSource.sendFeedback("Toggled downfall");
    }

    @Override
    public String name() {
        return "toggledownfall";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /toggledownfall");
        commandSource.sendFeedback("Info: Toggles the weather");
    }
}
