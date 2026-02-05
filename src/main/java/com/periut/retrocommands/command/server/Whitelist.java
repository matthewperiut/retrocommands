package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Whitelist implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        processWhitelistCommand(commandSource.getName(), ServerUtil.appendEnd(0, parameters), commandSource);
    }

    @Override
    public String name() {
        return "whitelist";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /whitelist {on/off/list/add/remove/reload} {player}");
    }

    private void processWhitelistCommand(String string, String string2, SharedCommandSource arg) {
        String[] var4 = string2.split(" ");
        if (var4.length >= 2) {
            String var5 = var4[1].toLowerCase();
            if ("on".equals(var5)) {
                ServerUtil.sendFeedbackAndLog(string, "Turned on white-listing");
                ServerUtil.getServer().properties.setProperty("white-list", true);
            } else if ("off".equals(var5)) {
                ServerUtil.sendFeedbackAndLog(string, "Turned off white-listing");
                ServerUtil.getServer().properties.setProperty("white-list", false);
            } else if ("list".equals(var5)) {
                Set var6 = ServerUtil.getServer().playerManager.getWhitelist();
                String var7 = "";

                String var9;
                for (Iterator var8 = var6.iterator(); var8.hasNext(); var7 = var7 + var9 + " ") {
                    var9 = (String) var8.next();
                }

                arg.sendFeedback("White-listed players: " + var7);
            } else {
                String var10;
                if ("add".equals(var5) && var4.length == 3) {
                    var10 = var4[2].toLowerCase();
                    ServerUtil.getServer().playerManager.addToWhitelist(var10);
                    ServerUtil.sendFeedbackAndLog(string, "Added " + var10 + " to white-list");
                } else if ("remove".equals(var5) && var4.length == 3) {
                    var10 = var4[2].toLowerCase();
                    ServerUtil.getServer().playerManager.removeFromWhitelist(var10);
                    ServerUtil.sendFeedbackAndLog(string, "Removed " + var10 + " from white-list");
                } else if ("reload".equals(var5)) {
                    ServerUtil.getServer().playerManager.reloadWhitelist();
                    ServerUtil.sendFeedbackAndLog(string, "Reloaded white-list from file");
                }
            }
        } else {
            manual(arg);
        }
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1)
        {
            String[] options = {"add", "on", "list", "reload", "remove", "off"};
            ArrayList<String> output = new ArrayList<>();
            for (String option : options)
            {
                if (option.startsWith(currentInput))
                {
                    output.add(option.substring(currentInput.length()));
                }
            }
            return output.toArray(new String[0]);
        }
        return new String[0];
    }
}
