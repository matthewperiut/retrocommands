package com.matthewperiut.retrocommands.util;

import com.matthewperiut.retrocommands.RetroCommands;
import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.command.*;
import com.matthewperiut.retrocommands.command.server.*;
import com.periut.cryonicconfig.CryonicConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Character.isDigit;

public class RetroChatUtil {
    public static ArrayList<Command> commands = new ArrayList<>();

    public static void addDefaultCommands() {
        commands.add(new Help());
        commands.add(new Mods());

        commands.add(new Kick());
        commands.add(new Ban());
        commands.add(new Pardon());
        commands.add(new BanIp());
        commands.add(new PardonIp());
        commands.add(new Op());
        commands.add(new Deop());
        commands.add(new Stop());
        commands.add(new Save());
        commands.add(new List());
        commands.add(new Say());
        commands.add(new Whitelist());

        commands.add(new Clear());
        commands.add(new Gamemode());
        commands.add(new Give());
        commands.add(new God());
        commands.add(new Heal());
        commands.add(new Id());
        commands.add(new Mobs());
        commands.add(new Summon());
        commands.add(new Tpa());
        commands.add(new Teleport());
        commands.add(new Time());
        commands.add(new Clock());
        commands.add(new ToggleDownfall());
        commands.add(new Ride());
        commands.add(new Hat());
        commands.add(new KillAll());
        commands.add(new Kill());
        commands.add(new Warp());
        commands.add(new WhoAmI());

        if (RetroCommands.cc) {
            commands.add(new ReloadCryonicConfig());
        }

        for (Command c : commands) {
            if (!c.disableInSingleplayer() || FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
                Help.addHelpTip("/" + c.name(), c.needsPermissions());
        }
    }

    public static boolean handleCommand(SharedCommandSource commandSource, String command, boolean operator) {
        String[] segments = command.split(" ");
        boolean help = segments[0].equals("help") && segments.length > 1;
        boolean page = help && isDigit(segments[1].charAt(0));

        String wanted = help ? segments[1] : segments[0];

        if (RetroCommands.cc) {
            RetroCommands.disabled_commands = CryonicConfig.getConfig(RetroCommands.MOD_ID).getString("disabledCommands", "").split(",");
            if (Arrays.asList(RetroCommands.disabled_commands).contains(command)) {
                commandSource.sendFeedback("This command has been disabled.");
                return false;
            }
        }

        for (Command c : commands) {
            if (c.name() == null)
                continue;

            if (!operator && c.needsPermissions()) {
                continue;
            }

            if (c.name().equals(wanted)) {
                if (c.disableInSingleplayer() && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
                    continue;

                if (help) {
                    c.manual(commandSource);
                    return true;
                } else {
                    try {
                        c.command(commandSource, segments);
                        return true;
                    } catch (Exception e) {
                        commandSource.sendFeedback("Error: " + e.getMessage());
                        return false;
                    }
                }
            }
        }

        if (segments[0].equals("help"))
            for (Command c : commands) {
                if (c.name().equals("help")) {
                    try {
                        c.command(commandSource, segments);
                        return true;
                    } catch (Exception e) {
                        commandSource.sendFeedback("Error: " + e.getMessage());
                        return false;
                    }
                }
            }

        commandSource.sendFeedback("Command '" + segments[0] + "' not found. Try /help");
        return false;
    }
}
