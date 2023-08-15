package com.matthewperiut.spc.api;

import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;

@FunctionalInterface
public interface SummonCommand {
    EntityBase create(Level level, PosParse pos, String[] parameters); // Modify the parameters as needed
}
