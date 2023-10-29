package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.level.LevelProperties;

import java.lang.reflect.Field;



public class ToggleDownfall implements Command
{
    // Thank you mine_diver for this code, it's beautiful

    private static final Field worldInfoField = getField(Level.class, new String[]{"properties", "field_220"});

    // from HMI, blame mine_diver
    //clean mine_diver code
    //Used for easy reflection with obfuscated or regular fields
    public static Field getField(Class<?> target, String[] names)
    {
        for (Field field : target.getDeclaredFields())
        {
            for (String name : names)
            {
                if (field.getName().equals(name))
                {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters)
    {
        PlayerBase player = commandSource.getPlayer();
        if (player == null)
        {
            return;
        }
        
        LevelProperties worldInfo = null;
        try
        {
            worldInfo = (LevelProperties) worldInfoField.get(player.level);
        }
        catch (Exception ignored)
        {

        }

        try
        {
            assert worldInfo != null;
            worldInfo.setThundering(!worldInfo.isThundering());
            worldInfo.setRaining(!worldInfo.isRaining());
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        commandSource.sendFeedback("Toggled downfall");
    }

    @Override
    public String name()
    {
        return "toggledownfall";
    }

    @Override
    public void manual(SharedCommandSource commandSource)
    {
        commandSource.sendFeedback("Usage: /toggledownfall");
        commandSource.sendFeedback("Info: Toggles the weather");
    }
}
