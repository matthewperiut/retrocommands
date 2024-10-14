package com.matthewperiut.retrocommands.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@FunctionalInterface
public interface SummonCommand {
    Entity create(World level, PosParse pos, String[] parameters); // Modify the parameters as needed
}
