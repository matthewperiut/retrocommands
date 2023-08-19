package com.matthewperiut.spc.util;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.command.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.command.CommandSource;

import java.util.ArrayList;

import static java.lang.Character.isDigit;

public class SPChatUtil {
    public static ArrayList<Command> commands = new ArrayList<>();
    public static CommandSource feedbackee;

    public static void sendMessage(String message) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.addChatMessage(message);
        } else {
            feedbackee.sendFeedback(message);
        }
    }

    public static void addDefaultCommands() {
        commands.add(new Clear());
        commands.add(new Gamemode());
        commands.add(new Give());
        commands.add(new God());
        commands.add(new Heal());
        commands.add(new Help());
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

        for (Command c : commands) {
            Help.addHelpTip("/" + c.name());
        }
    }

    public static void handleCommand(PlayerBase player, String command) {
        String[] segments = command.split(" ");
        boolean help = segments[0].equals("help") && segments.length > 1;
        boolean page = help && isDigit(segments[1].charAt(0));

        String wanted = help ? segments[1] : segments[0];

        for (Command c : commands) {

            if (c.name().equals(wanted)) {
                if (help) {
                    c.manual();
                } else {
                    try
                    {
                        c.command(player, segments);
                    }
                    catch (Exception e)
                    {
                        sendMessage("Error: " + e.getMessage());
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
                        c.command(player, segments);
                    }
                    catch (Exception e)
                    {
                        sendMessage("Error: " + e.getMessage());
                    }
                    return;
                }
            }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            sendMessage("Command '" + segments[0] + "' not found. Try /help");
    }
}
