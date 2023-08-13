package com.matthewperiut.spc.dimension;

import net.minecraft.class_467;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;

import java.util.Random;

public class BareTravelAgent extends class_467 {

    private final Random random = new Random();

    @Override
    public boolean method_1531(Level level, EntityBase entity) {
        return true;
    }

    @Override
    public boolean method_1532(Level level, EntityBase entity) {
        return true;
    }

    public boolean blockIsGood(int block, int meta) {
        return true;
    }
}
