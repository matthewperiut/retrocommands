package com.matthewperiut.retrocommands.util;

import com.matthewperiut.retrocommands.api.SummonRegistry;
import com.matthewperiut.retrocommands.mixin.access.EntityAccessor;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;

public class VanillaMobs {
    public static void setupSummons() {
        SummonRegistry.add(CreeperEntity.class, (level, pos, param) -> {
            CreeperEntity creeper = new CreeperEntity(level);

            if (param.length > 5)
                if (!param[5].isEmpty())
                    if (param[5].charAt(0) != '0')
                        ((EntityAccessor) creeper).getDataTracker().set(17, (byte) 1);

            return creeper;
        }, "{charged (0 or 1)}");

        SummonRegistry.add(SheepEntity.class, (level, pos, param) -> {
            int color = Integer.parseInt(param[5]);
            int has_wool = 1;
            if (param.length > 6)
                has_wool = Integer.parseInt(param[6]);
            SheepEntity sheep = new SheepEntity(level);
            sheep.setSheared(has_wool == 0);
            sheep.setColor(color);
            return sheep;
        }, "{wool color meta} {has wool (0/1)} ");

        SummonRegistry.add(PigEntity.class, (level, pos, param) -> {
            int meta = Integer.parseInt(param[5]);
            if (meta != 0)
                meta = 1;

            PigEntity pig = new PigEntity(level);
            ((EntityAccessor) pig).getDataTracker().set(16, (byte) meta);
            return pig;
        }, "{saddle (0 or 1)}");

        SummonRegistry.add(SlimeEntity.class, (level, pos, param) -> {
            int meta = Integer.parseInt(param[5]);
            SlimeEntity slime = new SlimeEntity(level);
            if (meta > 0) {
                ((EntityAccessor) slime).getDataTracker().set(16, (byte) meta);
            }
            return slime;
        }, "{size (int > 0)}");

        SummonRegistry.add(TntEntity.class, (level, pos, param) -> {
            int fuse = Integer.parseInt(param[5]);
            TntEntity tnt = new TntEntity(level);
            if (fuse > 0) {
                tnt.fuse = fuse;
            }
            return tnt;
        }, "{fuse ticks (int > 0)}");
    }
}
