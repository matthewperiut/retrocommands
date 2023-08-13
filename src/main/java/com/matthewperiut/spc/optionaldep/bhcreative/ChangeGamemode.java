package com.matthewperiut.spc.optionaldep.bhcreative;

import net.minecraft.entity.player.PlayerBase;
import paulevs.bhcreative.interfaces.CreativePlayer;

public class ChangeGamemode {
    public static void set(PlayerBase player, boolean creative) {
        ((CreativePlayer) (player)).creative_setCreative(creative);
    }
}
