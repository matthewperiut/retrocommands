package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PosParse;
import com.matthewperiut.spc.optionaldep.stapi.SwitchDimension;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;


public class Teleport implements Command {

    public static void teleport(PlayerEntity p, double x, double y, double z) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            p.setPosition(x, y, z);
            p.setVelocityClient(0, 0, 0);
        } else {
            ServerPlayerEntity sp = (ServerPlayerEntity) p;
            sp.networkHandler.teleport(x, y, z, p.yaw, p.pitch);
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
            return;
        }

        if (parameters.length == 4 || parameters.length == 5) {
            PosParse pos = new PosParse(player, 1, parameters);

            if (!pos.valid) {
                commandSource.sendFeedback("Non-number position");
                return;
            }

            if (parameters.length > 4) {
                if (!switchDimensions(commandSource, parameters[4])) return;
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
        commandSource.sendFeedback("Usage: /tp {x} {y} {z} {optional: dimension identifier}");
        commandSource.sendFeedback("Info: moves a player to a desired coordinate and/or dimension");
        commandSource.sendFeedback("dimension identifier: e.g. \"minecraft:overworld\"");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1) {

        }

        if (parameterNum > 0 && parameterNum < 4 && currentInput.isEmpty())
        {
            return new String[]{"~"};
        }

        return new String[0];
    }
}
