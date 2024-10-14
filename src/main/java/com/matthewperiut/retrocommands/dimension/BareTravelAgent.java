package com.matthewperiut.retrocommands.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.PortalForcer;

import java.util.Random;

public class BareTravelAgent extends PortalForcer {

    private final Random random = new Random();

    @Override
    public boolean teleportToValidPortal(World level, Entity entity) {
        return true;
    }

    @Override
    public boolean createPortal(World level, Entity entity) {
        return true;
    }

    public boolean blockIsGood(int block, int meta) {
        return true;
    }
}
