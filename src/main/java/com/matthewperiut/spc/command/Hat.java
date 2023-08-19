package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Hat implements Command
{
    @Override
    public void command(PlayerBase player, String[] parameters)
    {
        ItemInstance hand = player.inventory.getHeldItem() == null ? null : player.inventory.getHeldItem().copy();
        ItemInstance hat = player.inventory.armour[3] == null ? null : player.inventory.armour[3].copy();

        player.inventory.armour[3] = hand;
        player.inventory.main[player.inventory.selectedHotbarSlot] = hat;

        sendMessage("Swapped hat with hand");
    }

    @Override
    public String name()
    {
        return "hat";
    }

    @Override
    public void manual()
    {
        sendMessage("Usage: /hat");
        sendMessage("Info: Puts the item in your hand on your head");
    }
}
