package com.matthewperiut.spc.util;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.command.*;
import com.matthewperiut.spc.command.server.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.command.CommandSource;

import java.util.ArrayList;

import static java.lang.Character.isDigit;

public class SPChatUtil {
    public static ArrayList<Command> commands = new ArrayList<>();

    public static void addDefaultCommands() {
        commands.add(new Help());

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
        {
            commands.add(new Kick());
            commands.add(new Ban());
            commands.add(new Pardon());
            commands.add(new BanIp());
            commands.add(new PardonIp());
            commands.add(new Stop());
            commands.add(new List());
            commands.add(new Say());
        }

        commands.add(new Clear());
        commands.add(new Gamemode());
        commands.add(new Give());
        commands.add(new God());
        commands.add(new Heal());
        commands.add(new Id());
        commands.add(new Mobs());
        commands.add(new Summon());
        commands.add(new Teleport());
        commands.add(new Time());
        commands.add(new ToggleDownfall());
        commands.add(new Ride());
        commands.add(new Hat());
        commands.add(new KillAll());
        commands.add(new Kill());
        commands.add(new Warp());
        commands.add(new WhoAmI());

        for (Command c : commands) {
            Help.addHelpTip("/" + c.name());
        }
    }

    public static void handleCommand(SharedCommandSource commandSource, String command) {
        String[] segments = command.split(" ");
        boolean help = segments[0].equals("help") && segments.length > 1;
        boolean page = help && isDigit(segments[1].charAt(0));

        String wanted = help ? segments[1] : segments[0];

        for (Command c : commands) {

            if (c.name().equals(wanted)) {
                if (help) {
                    c.manual(commandSource);
                } else {
                    try
                    {
                        c.command(commandSource, segments);
                    }
                    catch (Exception e)
                    {
                        commandSource.sendFeedback("Error: " + e.getMessage());
                    }
                }
                return;
            }
        }

        if (segments[0].equals("help"))
            for (Command c : commands) {
                if (c.name().equals("help")) {
                    try
                    {
                        c.command(commandSource, segments);
                    }
                    catch (Exception e)
                    {
                        commandSource.sendFeedback("Error: " + e.getMessage());
                    }
                    return;
                }
            }

        commandSource.sendFeedback("Command '" + segments[0] + "' not found. Try /help");
    }
}
