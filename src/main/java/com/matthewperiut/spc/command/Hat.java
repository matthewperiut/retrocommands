package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;


public class Hat implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerBase player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        ItemInstance hand = player.inventory.getHeldItem() == null ? null : player.inventory.getHeldItem().copy();
        ItemInstance hat = player.inventory.armour[3] == null ? null : player.inventory.armour[3].copy();

        player.inventory.armour[3] = hand;
        player.inventory.main[player.inventory.selectedHotbarSlot] = hat;

        commandSource.sendFeedback("Swapped hat with hand");
    }

    @Override
    public String name() {
        return "hat";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /hat");
        commandSource.sendFeedback("Info: Puts the item in your hand on your head");
    }

    @Override
    public boolean needsPermissions() {
        return false;
    }
}
