package com.matthewperiut.spc.util;

import com.matthewperiut.spc.api.SummonRegistry;
import com.matthewperiut.spc.mixin.EntityAccessor;
import net.minecraft.entity.PrimedTnt;
import net.minecraft.entity.animal.Pig;
import net.minecraft.entity.animal.Sheep;
import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.Slime;

public class VanillaMobs {
    public static void setupSummons() {
        SummonRegistry.add(Creeper.class, (level, pos, param) -> {
            Creeper creeper = new Creeper(level);

            if (param.length > 5)
                if (!param[5].isEmpty())
                    if (param[5].charAt(0) != '0')
                        ((EntityAccessor) creeper).getDataTracker().setInt(17, (byte) 1);

            return creeper;
        }, "{charged (0 or 1)}");

        SummonRegistry.add(Sheep.class, (level, pos, param) -> {
            int color = Integer.parseInt(param[5]);
            int has_wool = 1;
            if (param.length > 6)
                has_wool = Integer.parseInt(param[6]);
            Sheep sheep = new Sheep(level);
            sheep.setSheared(has_wool == 0);
            sheep.setColour(color);
            return sheep;
        }, "{wool color meta} {has wool (0/1)} ");

        SummonRegistry.add(Pig.class, (level, pos, param) -> {
            int meta = Integer.parseInt(param[5]);
            if (meta != 0)
                meta = 1;

            Pig pig = new Pig(level);
            ((EntityAccessor) pig).getDataTracker().setInt(16, (byte) meta);
            return pig;
        }, "{saddle (0 or 1)}");

        SummonRegistry.add(Slime.class, (level, pos, param) -> {
            int meta = Integer.parseInt(param[5]);
            Slime slime = new Slime(level);
            if (meta > 0) {
                ((EntityAccessor) slime).getDataTracker().setInt(16, (byte) meta);
            }
            return slime;
        }, "{size (int > 0)}");

        SummonRegistry.add(PrimedTnt.class, (level, pos, param) -> {
            int fuse = Integer.parseInt(param[5]);
            PrimedTnt tnt = new PrimedTnt(level);
            if (fuse > 0) {
                tnt.fuse = fuse;
            }
            return tnt;
        }, "{fuse ticks (int > 0)}");
    }
}
