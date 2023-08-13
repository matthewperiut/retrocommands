package com.matthewperiut.spc.api;

import net.minecraft.entity.player.PlayerBase;

public interface Command {
    void command(PlayerBase player, String[] parameters);

    String name();

    void manual();
}
