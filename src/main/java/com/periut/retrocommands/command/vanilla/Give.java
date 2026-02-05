package com.periut.retrocommands.command.vanilla;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.api.ItemInstanceStr;
import com.periut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

import static com.periut.retrocommands.util.ParameterSuggestUtil.suggestItemIdentifier;


public class Give implements Command {

    public static boolean givePlayerItemInstance(SharedCommandSource commandSource, PlayerEntity player, ItemStack instance) {
        ItemStack[] inventory = player.inventory.main;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = instance;
                return true;
            }
        }

        commandSource.sendFeedback("Cannot give " + instance.getItem().getTranslatedName() + " because inventory is full");
        return false;
    }

    public static int nameToItemId(String n) {
        String name = n.replace("_", "");
        for (int i = 0; i < Item.ITEMS.length; i++) {
            if (Item.ITEMS[i] == null)
                continue;
            String translatedName = Item.ITEMS[i].getTranslatedName().replace(" ", ""); // Remove spaces
            if (translatedName.equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public static int identifierToItemId(String n) {
        Optional<Item> item = ItemRegistry.INSTANCE.getOrEmpty(Identifier.of(n));
        return item.map(itemBase -> itemBase.id).orElse(-1);
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length > 1 && !parameters[1].equals("help")) {
            int itemNumber;
            int meta = 0;

            String setSPCStr = "";

            try {
                itemNumber = Integer.parseInt(parameters[1]);
            } catch (NumberFormatException e) {
                itemNumber =
                        FabricLoader.getInstance().isModLoaded("station-registry-api-v0") ?
                                identifierToItemId(parameters[1]) :
                                nameToItemId(parameters[1]);
            }

            if (itemNumber == -1) {
                commandSource.sendFeedback("Item not found");
                return;
            }

            Item itemType = Item.ITEMS[itemNumber];
            if (itemType == null) {
                commandSource.sendFeedback("Item not found");
                return;
            }
            int stackSize = itemType.getMaxCount();


            if (parameters.length > 2) {
                int requestedCount;
                try {
                    requestedCount = Integer.parseInt(parameters[2]);
                } catch (NumberFormatException e) {
                    commandSource.sendFeedback("Requested number of items '" + parameters[2] + "' is not a number");
                    return;
                }
                stackSize = requestedCount;

                if (parameters.length > 3) {
                    if (itemType.hasSubtypes() || itemType.getTranslatedName().equals("Monster Spawner")) {
                        try {
                            meta = Integer.parseInt(parameters[3]);
                        } catch (NumberFormatException e) {
                            if (itemType.getTranslatedName().equals("Monster Spawner")) {
                                Entity entity = EntityRegistry.create(parameters[3], commandSource.getPlayer().world);
                                if (entity instanceof LivingEntity l) {
                                    setSPCStr = l.getRegistryEntry();
                                    commandSource.sendFeedback(l.getRegistryEntry() + " inside");
                                } else if (entity != null) {
                                    commandSource.sendFeedback("Entity must be living in spawners");
                                } else {
                                    commandSource.sendFeedback("Invalid entity parameter");
                                }
                            } else {
                                commandSource.sendFeedback("Metadata not a number");
                            }
                        }
                    } else {
                        commandSource.sendFeedback("Metadata not applicable");
                    }
                }
            }

            ItemStack item = new ItemStack(itemType, stackSize, meta);

            if (!setSPCStr.isEmpty()) {
                ((ItemInstanceStr) ((Object) item)).spc$setStr(setSPCStr);
            }

            if (givePlayerItemInstance(commandSource, commandSource.getPlayer(), item)) {
                commandSource.sendFeedback("Gave " + item.count + " " + itemType.getTranslatedName());
            }
            return;
        }
        manual(commandSource);
    }

    @Override
    public String name() {
        return "give";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /give {id} {count} {optional:meta/mob_id}");
        commandSource.sendFeedback("Info: gives the player items");
        commandSource.sendFeedback("id: can be number or name");
        commandSource.sendFeedback("count: number of item");
        commandSource.sendFeedback("meta: sub-item selection");
        commandSource.sendFeedback("list mobs with /mobs");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput)
    {
        if (!FabricLoader.getInstance().isModLoaded("station-registry-api-v0"))
            return new String[0];

        if (parameterNum == 1)
        {
            return suggestItemIdentifier(currentInput);
        }

        if (parameterNum == 2 && currentInput.isEmpty())
        {
            String itemId = totalInput.split(" ")[1];
            boolean hasModId = itemId.contains(":");

            if (hasModId)
            {
                Optional<Item> id = ItemRegistry.INSTANCE.getOrEmpty(Identifier.of(itemId));
                if (id.isPresent())
                {
                    return new String[]{ String.valueOf(id.get().getMaxCount()) };
                }
            }
        }
        return new String[0];
    }
}
