package com.matthewperiut.retrocommands.optionaldep.bhcreative;

import net.minecraft.entity.player.PlayerEntity;
import paulevs.bhcreative.interfaces.CreativePlayer;

public class ChangeGamemode {
    public static void set(PlayerEntity player, boolean creative) {
        ((CreativePlayer) (player)).creative_setCreative(creative);
    }
}
