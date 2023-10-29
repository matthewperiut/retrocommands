package com.matthewperiut.spc.api;

import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerBase;

public interface Command {
    void command(SharedCommandSource commandSource, String[] parameters);

    String name();

    void manual(SharedCommandSource commandSource);
}
