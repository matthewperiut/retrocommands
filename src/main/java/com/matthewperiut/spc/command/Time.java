package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Time implements Command {
    public void command(PlayerBase player, String[] parameters) {
        if (parameters.length > 2) {
            switch (parameters[1]) {
                case "set":
                    long time = -1;
                    try {
                        time = Integer.parseInt(parameters[2]);
                    } catch (NumberFormatException e) {
                        switch (parameters[2]) {
                            case "day":
                                time = 1000;
                                break;
                            case "noon":
                                time = 6000;
                                break;
                            case "sunset":
                                time = 12000;
                                break;
                            case "night":
                                time = 13000;
                                break;
                            case "midnight":
                                time = 18000;
                                break;
                            case "sunrise":
                                time = 23000;
                                break;
                            default:
                                sendMessage("Time is not properly formatted");
                                return;
                        }
                    }
                    player.level.setLevelTime(time);
                    sendMessage("Time set to " + time);
                    break;
            }

            return;
        }

        manual();
    }

    @Override
    public String name() {
        return "time";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /time set {levelTime}");
        sendMessage("Info: sets the time of day");
        sendMessage("levelTime can be an integer usually between 0 and 24000 or keyword");
        sendMessage("preset keywords are day, noon, sunset, night, midnight, sunrise");
    }
}
