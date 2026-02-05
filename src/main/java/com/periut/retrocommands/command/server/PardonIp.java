package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;

public class PardonIp implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        String ip = ServerUtil.appendEnd(1, parameters).trim();
        ServerUtil.getConnectionManager().unbanIp(ip);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Pardoning ip " + ip);
    }

    @Override
    public String name() {
        return "pardon-ip";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /pardon-ip {ip}");
        commandSource.sendFeedback("Info: pardons a banned IP address so that they can connect again");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
