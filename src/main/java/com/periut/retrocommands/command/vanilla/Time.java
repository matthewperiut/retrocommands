package com.periut.retrocommands.command.vanilla;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;


public class Time implements Command {
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        if (parameters.length > 1) {
            if (parameters[1].equals("set")) {
                long additional_time = -1;
                try {
                    additional_time = Integer.parseInt(parameters[2]);
                } catch (NumberFormatException e) {
                    switch (parameters[2]) {
                        case "day":
                            additional_time = 1000;
                            break;
                        case "noon":
                            additional_time = 6000;
                            break;
                        case "sunset":
                            additional_time = 12000;
                            break;
                        case "night":
                            additional_time = 13000;
                            break;
                        case "midnight":
                            additional_time = 18000;
                            break;
                        case "sunrise":
                            additional_time = 23000;
                            break;
                        default:
                            commandSource.sendFeedback("Time is not properly formatted");
                            return;
                    }
                }
                long time = player.world.getTime();
                long left_over = time % 24000;
                player.world.setTime(time + (additional_time - left_over));
                commandSource.sendFeedback("Time set to " + additional_time);
                return;
            }
            if (parameters[1].equals("get")) {
                commandSource.sendFeedback("Time is " + String.valueOf(player.world.getTime()));
                commandSource.sendFeedback("Days: " + String.valueOf(player.world.getTime()));
                return;
            }
            if (parameters[1].equals("add")) {
                try {
                    long additional_time = Integer.parseInt(parameters[2]);
                    player.world.setTime(player.world.getTime() + additional_time);
                    commandSource.sendFeedback("You added " + additional_time + " to the time");
                } catch (NumberFormatException e) {
                    commandSource.sendFeedback("Cannot add a non-number amount of time");
                }
                return;
            }
        }

        manual(commandSource);
    }

    @Override
    public String name() {
        return "time";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /time set {worldTime}");
        commandSource.sendFeedback("Usage: /time add {time}");
        commandSource.sendFeedback("Info: sets the time of day");
        commandSource.sendFeedback("worldTime can be an integer usually between 0 and 24000 or keyword");
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
