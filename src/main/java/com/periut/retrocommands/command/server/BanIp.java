package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;

public class BanIp implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        String ip = ServerUtil.appendEnd(1, parameters).trim();
        ServerUtil.getConnectionManager().banIp(ip);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Banning ip " + ip);
    }

    @Override
    public String name() {
        return "ban-ip";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /ban-ip {ip}");
        commandSource.sendFeedback("Info: bans an IP address from the server");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
