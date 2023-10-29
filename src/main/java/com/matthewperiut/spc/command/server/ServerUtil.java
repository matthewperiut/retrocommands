package com.matthewperiut.spc.command.server;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerConnectionManager;

import java.util.logging.Logger;

public class ServerUtil {
    public static Logger LOGGER = Logger.getLogger("Minecraft");

    public static MinecraftServer getServer() {
        return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
    }

    public static ServerPlayerConnectionManager getConnectionManager() {
        return getServer().serverPlayerConnectionManager;
    }

    public static void sendFeedbackAndLog(String user, String message) {
        String str = user + ": " + message;
        ServerUtil.getConnectionManager().sendChatMessage("§7(" + str + ")");
        LOGGER.info(str);
    }

    public static String appendEnd(int start, String[] parameters) {
        StringBuilder joinedString = new StringBuilder();
        for (int i = start; i < parameters.length; i++) {
            joinedString.append(parameters[i]).append(" ");
        }

        return joinedString.toString();
    }
}
