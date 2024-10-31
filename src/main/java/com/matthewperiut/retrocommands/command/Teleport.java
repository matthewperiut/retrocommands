package com.matthewperiut.retrocommands.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.api.PosParse;
import com.matthewperiut.retrocommands.optionaldep.stapi.SwitchDimension;
import com.matthewperiut.retrocommands.util.ParameterSuggestUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;

import static com.matthewperiut.retrocommands.command.server.ServerUtil.*;


public class Teleport implements Command {

    public static void teleport(PlayerEntity p, double x, double y, double z) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            p.setPosition(x, y, z);
            p.setVelocityClient(0, 0, 0);
        } else {
            serverTeleport(p, x, y, z);
        }
    }

    public static boolean switchDimensions(SharedCommandSource commandSource, String id) {
        if (FabricLoader.getInstance().isModLoaded("station-dimensions-v0")) {
            SwitchDimension.go(commandSource, id);
            return true;
        } else {
            commandSource.sendFeedback("Cannot switch dimensions without Station API Dimensions");
            return false;
        }
    }

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            commandSource.sendFeedback("You must be a player");
            return;
        }

        if (parameters.length == 2) {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                // teleport to player
                System.out.printf(parameters[1]);
                teleportToPlayer(commandSource, player, parameters[1]);
                return;
            } else {
                commandSource.sendFeedback("You can only teleport to other players on server");
                return;
            }
        }

        if (parameters.length == 3) {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                // teleport player to player
                System.out.printf(parameters[1]);
                teleportPlayerToPlayer(commandSource, parameters[1], parameters[2]);
                return;
            } else {
                commandSource.sendFeedback("You can only teleport to other players on server");
                return;
            }
        }

        if (parameters.length > 3 && parameters.length < 7) {

            if (!PosParse.canPosParse(parameters[1])) {
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
                    PosParse pos = new PosParse(player, 2, parameters);
                    if (parameters.length == 6) {
                        teleportPlayerToPos(commandSource, parameters[1], pos, parameters[5]);
                    } else {
                        teleportPlayerToPos(commandSource, parameters[1], pos, "");
                    }
                    return;
                } else {
                    commandSource.sendFeedback("This use of tp is reserved for server use");
                    return;
                }
            }

            PosParse pos = new PosParse(player, 1, parameters);

            if (!pos.valid) {
                commandSource.sendFeedback("Non-number position");
                return;
            }

            if (parameters.length > 4) {
                if (!switchDimensions(commandSource, parameters[4])) {
                    commandSource.sendFeedback("Dimension " + parameters[4] + " does not exist. No tp.");
                    return;
                }
            }

            commandSource.sendFeedback("Teleporting to " + pos);
            teleport(player, pos.x, pos.y, pos.z);
            return;
        }

        manual(commandSource);
    }

    @Override
    public String name() {
        return "tp";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /tp {x} {y} {z}");
        commandSource.sendFeedback("Usage: /tp {playerName}");
        commandSource.sendFeedback("Usage: /tp {teleport playerName} {to PlayerName}");
        commandSource.sendFeedback("Usage: /tp {playerName} {x} {y} {z}");
        commandSource.sendFeedback("Info: moves a player to a desired coordinate and/or dimension");
        commandSource.sendFeedback("Note: put dimension after other parameters to go to a dimension");
        commandSource.sendFeedback("dimension identifier: e.g. \"minecraft:overworld\"");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (!PosParse.canPosParse(currentInput)) {
            if (parameterNum == 1 || parameterNum == 2) {
                return ParameterSuggestUtil.suggestPlayerName(currentInput);
            }
        }

        String[] segments = totalInput.split(" ");

        int offset = 0;
        if (segments.length > 1) {
            if (!PosParse.canPosParse(segments[1])) {
                offset++;
                if (segments.length > 2 && !PosParse.canPosParse(segments[2])) {
                    offset++;
                }
            }
        }

        if (parameterNum > offset && parameterNum < 4 + offset)
        {
            if (currentInput.isEmpty()) {
                return new String[]{"~"};
            }
        }

        return new String[0];
    }
}
