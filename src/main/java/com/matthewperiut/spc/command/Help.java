package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import java.util.ArrayList;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Help implements Command {
    static ArrayList<Page> pages = new ArrayList<>();

    public static void addHelpTip(String tip) {
        boolean added = false;
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            if (page.strings.size() < 6) {
                page.strings.add(tip);
                added = true;
                break;
            }
        }

        if (!added) {
            pages.add(new Page());
            pages.get(pages.size() - 1).strings.add(tip);
        }
    }

    @Override
    public void command(PlayerBase player, String[] parameters) {
        int pg = 1;
        if (parameters.length > 1) {
            try {
                pg = Integer.parseInt(parameters[1]);
                if (pg > pages.size() || pg < 1) {
                    sendMessage("Page out of bounds");
                    return;
                }
            } catch (NumberFormatException e) {
                sendMessage(parameters[1] + " is not a number");
            }
        }
        sendMessage("For these commands, use \"/help {command}\" for more info:");
        sendMessage("pg " + (pg) + "/" + pages.size());
        pages.get(pg - 1).send();
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /help {pg}");
        sendMessage("Info: gives the list of commands available");
    }

    private static class Page {
        public ArrayList<String> strings = new ArrayList<>();

        public Page() {

        }

        public void send() {
            for (String s : strings)
                sendMessage(s);
        }
    }
}
