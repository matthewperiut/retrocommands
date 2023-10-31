package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

import java.util.ArrayList;


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
    public void command(SharedCommandSource commandSource, String[] parameters) {
        int pg = 1;
        if (parameters.length > 1) {
            try {
                pg = Integer.parseInt(parameters[1]);
                if (pg > pages.size() || pg < 1) {
                    commandSource.sendFeedback("Page out of bounds");
                    return;
                }
            } catch (NumberFormatException e) {
                commandSource.sendFeedback(parameters[1] + " is not a number");
            }
        }
        commandSource.sendFeedback("For these commands, use \"/help {command}\" for more info:");
        commandSource.sendFeedback("pg " + (pg) + "/" + pages.size());
        pages.get(pg - 1).send(commandSource);
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
            String[] output = new String[pages.size()];
            for (int i = 1; i < pages.size()+1; i++)
            {
                output[i-1] = String.valueOf(i);
            }
            return output;
        }
        return new String[0];
    }
}
