package com.matthewperiut.retrocommands.command.extra;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.CustomValue;

import java.util.ArrayList;

public class Mods implements Command {

    private boolean apiRequested(String[] parameters) {
        boolean wantAPI = false;
        if (parameters.length > 2) {
            try {
                wantAPI = Integer.parseInt(parameters[2]) == 1;
            } catch (NumberFormatException ignored) {

            }
        }
        return wantAPI;
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        final boolean wantAPI = apiRequested(parameters);

        ArrayList<String> mods = new ArrayList<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            if (!modContainer.getMetadata().getType().equals("fabric"))
                return;

            boolean isAPI = false;
            CustomValue api = modContainer.getMetadata().getCustomValue("modmenu:api");
            if (api != null) {
                if (api.getAsBoolean()) {
                    isAPI = true;
                }
            }

            if (wantAPI && isAPI) {
                mods.add(modContainer.getMetadata().getName());
            } else {
                if (!isAPI) {
                    mods.add(modContainer.getMetadata().getName());
                }
            }
        });

        int pages = (int) Math.ceil(mods.size() / 5.0);
        int page = 1;
        if (parameters.length > 1) {
            try {
                page = Integer.parseInt(parameters[1]);
                if (page > pages || page < 1) {
                    commandSource.sendFeedback("Page out of bounds");
                    return;
                }
            } catch (NumberFormatException e) {
                commandSource.sendFeedback(parameters[1] + " is not a number");
                return;
            }
        }
        commandSource.sendFeedback("Installed mods (" + page + "/" + pages + "):");

        for (int i = (page - 1) * 5; i < Math.min((((page - 1) * 5) + 5), mods.size()); i++) {
            commandSource.sendFeedback(mods.get(i));
        }
    }

    @Override
    public String name() {
        return "mods";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /mods {pg} {api mods 0/1}");
        commandSource.sendFeedback("Info: gives the list of mods");
    }

    public boolean needsPermissions() {
        return false;
    }
}
