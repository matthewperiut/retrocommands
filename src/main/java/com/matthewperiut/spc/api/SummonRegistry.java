package com.matthewperiut.spc.api;

import com.matthewperiut.spc.command.Summon;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SummonRegistry {
    private static Map<Class<? extends Entity>, SummonCommand> entityFactories = new HashMap<>();

    public static void add(Class<? extends Entity> entity, SummonCommand cmd, String param_desc) {
        SummonCommand c = entityFactories.put(entity, cmd);
        if (c != null)
            System.out.println("[SPC] Overwrote " + c + " with " + cmd + " for summoning " + entity);


        Summon.help.put(entity, param_desc);
    }

    public static Entity create(Class<? extends Entity> entityClass, World level, PosParse pos, String[] param) {
        SummonCommand entityConstructor = entityFactories.get(entityClass);
        if (entityConstructor != null) {
            return entityConstructor.create(level, pos, param);
        }
        return null;
    }
}
