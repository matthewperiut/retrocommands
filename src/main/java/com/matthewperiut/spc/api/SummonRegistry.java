package com.matthewperiut.spc.api;

import com.matthewperiut.spc.command.Summon;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;

import java.util.HashMap;
import java.util.Map;

public class SummonRegistry {
    private static Map<Class<? extends EntityBase>, SummonCommand> entityFactories = new HashMap<>();

    public static void add(Class<? extends EntityBase> entity, SummonCommand cmd, String param_desc) {
        SummonCommand c = entityFactories.put(entity, cmd);
        if (c != null)
            System.out.println("[SPC] Overwrote " + c + " with " + cmd + " for summoning " + entity);


        Summon.help.put(entity, param_desc);
    }

    public static EntityBase create(Class<? extends EntityBase> entityClass, Level level, PosParse pos, String[] param) {
        SummonCommand entityConstructor = entityFactories.get(entityClass);
        if (entityConstructor != null) {
            return entityConstructor.create(level, pos, param);
        }
        return null;
    }
}
