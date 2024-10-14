package com.matthewperiut.retrocommands.optionaldep.stapi.block;

import com.matthewperiut.retrocommands.optionaldep.stapi.item.MobSpawnerBlockItem;
import net.minecraft.block.SpawnerBlock;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;

@HasCustomBlockItemFactory(MobSpawnerBlockItem.class)
public class SpecialMobSpawnerBlock extends SpawnerBlock {
    public SpecialMobSpawnerBlock(int i, int j) {
        super(i, j);
    }
}
