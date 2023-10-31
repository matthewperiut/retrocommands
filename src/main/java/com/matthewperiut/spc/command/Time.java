package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerBase;

import java.util.ArrayList;


public class Time implements Command {
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerBase player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

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
                                commandSource.sendFeedback("Time is not properly formatted");
                                return;
                        }
                    }
                    player.level.setLevelTime(time);
                    commandSource.sendFeedback("Time set to " + time);
                    break;
            }

            return;
        }

        manual(commandSource);
    }

    @Override
    public String name() {
        return "time";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /time set {levelTime}");
        commandSource.sendFeedback("Info: sets the time of day");
        commandSource.sendFeedback("levelTime can be an integer usually between 0 and 24000 or keyword");
        commandSource.sendFeedback("preset keywords are day, noon, sunset, night, midnight, sunrise");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1)
        {
            String[] options = {"set"};
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
        if (parameterNum == 2 && totalInput.contains("set"))
        {
            String[] options = {"day", "noon", "sunset", "night", "midnight", "sunrise"};
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
