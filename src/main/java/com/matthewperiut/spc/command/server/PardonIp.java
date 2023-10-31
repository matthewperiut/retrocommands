package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

public class PardonIp implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        String ip = ServerUtil.appendEnd(1, parameters).trim();
        ServerUtil.getConnectionManager().removeIpBan(ip);
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
    public boolean isOnlyServer() {
        return true;
    }
}
