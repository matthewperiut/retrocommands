package com.matthewperiut.retrocommands.command.vanilla;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.ServerUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;


public class Help implements Command {
    static ArrayList<Page> admin_pages = new ArrayList<>();
    static ArrayList<Page> regular_pages = new ArrayList<>();

    private static void addHelpTip(ArrayList<Page> pages, String tip, boolean forAdmin, boolean isAdmin) {
        boolean added = false;
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            if (page.strings.size() < 6) {
                if (isAdmin) {
                    page.strings.add(tip);
                } else {
                    if (!forAdmin) {
                        page.strings.add(tip);
                    }
                }
                added = true;
                break;
            }
        }

        if (!added) {
            pages.add(new Page());

            if (isAdmin) {
                pages.get(pages.size() - 1).strings.add(tip);
            } else {
                if (!forAdmin) {
                    pages.get(pages.size() - 1).strings.add(tip);
                }
            }
        }
    }

    public static void addHelpTip(String tip, boolean needsPermissions) {
        addHelpTip(admin_pages, tip, needsPermissions, true);
        addHelpTip(regular_pages, tip, needsPermissions, false);
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        int pg = 1;
        if (parameters.length > 1) {
            try {
                pg = Integer.parseInt(parameters[1]);
                if (pg > admin_pages.size() || pg < 1) {
                    commandSource.sendFeedback("Page out of bounds");
                    return;
                }
            } catch (NumberFormatException e) {
                commandSource.sendFeedback(parameters[1] + " is not a number");
            }
        }
        commandSource.sendFeedback("For these commands, use \"/help {command}\" for more info:");


        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            commandSource.sendFeedback("pg " + (pg) + "/" + admin_pages.size());
            admin_pages.get(pg - 1).send(commandSource);
        } else {
            if (ServerUtil.isOp(commandSource.getName()) || commandSource.getPlayer() == null) {
                commandSource.sendFeedback("pg " + (pg) + "/" + admin_pages.size());
                admin_pages.get(pg - 1).send(commandSource);
            } else {
                commandSource.sendFeedback("pg " + (pg) + "/" + regular_pages.size());
                regular_pages.get(pg - 1).send(commandSource);
            }
        }
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /help {pg}");
        commandSource.sendFeedback("Info: gives the list of commands available");
    }

    private static class Page {
        public ArrayList<String> strings = new ArrayList<>();

        public Page() {

        }

        public void send(SharedCommandSource commandSource) {
            for (String s : strings)
                commandSource.sendFeedback(s);
        }
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1 && currentInput.isEmpty())
        {
            String[] output = new String[admin_pages.size()];
            for (int i = 1; i < admin_pages.size()+1; i++)
            {
                output[i-1] = String.valueOf(i);
            }
            return output;
        }
        return new String[0];
    }

    public boolean needsPermissions() {
        return false;
    }
}
