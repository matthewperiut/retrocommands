package com.matthewperiut.retrocommands.command.server;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.ParameterSuggestUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;

public class Tpa implements Command {
    public static class Request {
        long time;
        String from;
        String to;
        public Request(String from, String to, long time) {
            this.from = from;
            this.to = to;
            this.time = time;
        }
    }
    public static ArrayList<Request> requests = new ArrayList<>();

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ServerUtil.tpa(commandSource, parameters);
        } else {
            commandSource.sendFeedback("This can only be used on server.");
        }
    }

    @Override
    public String name() {
        return "tpa";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /tpa {playerName}");
        commandSource.sendFeedback("Info: request to teleport to a player");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }

    @Override
    public boolean needsPermissions() {
        return false;
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1) {
            return ParameterSuggestUtil.suggestPlayerName(currentInput, source.getName());
        }
        return new String[0];
    }
}
