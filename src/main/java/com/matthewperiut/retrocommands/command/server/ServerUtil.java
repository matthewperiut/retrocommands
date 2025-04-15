package com.matthewperiut.retrocommands.command.server;

import com.matthewperiut.retrocommands.RetroCommands;
import com.matthewperiut.retrocommands.api.PosParse;
import com.matthewperiut.retrocommands.dimension.BareTravelAgent;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.networking.GlassPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;

import java.util.Optional;
import java.util.logging.Logger;

import static com.matthewperiut.retrocommands.command.Teleport.switchDimensions;
import static com.matthewperiut.retrocommands.command.Teleport.teleport;
import static com.matthewperiut.retrocommands.command.server.Tpa.requests;

public class ServerUtil {
    public static Logger LOGGER = Logger.getLogger("Minecraft");

    public static MinecraftServer getServer() {
        return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
    }

    public static PlayerManager getConnectionManager() {
        return getServer().playerManager;
    }

    public static void sendFeedbackAndLog(String user, String message) {
        String str = user + ": " + message;
        ServerUtil.getConnectionManager().broadcast("§7(" + str + ")");
        LOGGER.info(str);
    }

    public static String appendEnd(int start, String[] parameters) {
        StringBuilder joinedString = new StringBuilder();
        for (int i = start; i < parameters.length; i++) {
            joinedString.append(parameters[i]).append(" ");
        }

        return joinedString.toString();
    }

    public static boolean isOp(String name) {
        return getServer().playerManager.isOperator(name);
    }

    public static void serverTeleport(PlayerEntity p, double x, double y, double z) {
        ServerPlayerEntity sp = (ServerPlayerEntity) p;
        sp.networkHandler.teleport(x, y, z, p.yaw, p.pitch);
    }

    public static void teleportPlayerToPos(SharedCommandSource source, String targetName, PosParse pos, String dim) {
        PlayerEntity target = ServerUtil.getConnectionManager().getPlayer(targetName);
        if (target == null) {
            source.sendFeedback("Can't find user " + targetName + ". No tp.");
        } else {
            if (!dim.isEmpty()) {
                if (!switchDimensions(source, dim)) {
                    source.sendFeedback("Dimension " + dim + " does not exist. No tp.");
                    return;
                }
            }
            teleport(target, target.x, target.y, target.z);
            source.sendFeedback("Teleporting " + targetName + " to " + pos + ".");
        }
    }

    @Environment(EnvType.SERVER)
    public static void teleportToPlayer(SharedCommandSource source, PlayerEntity player, String targetName) {
        PlayerEntity target = ServerUtil.getConnectionManager().getPlayer(targetName);
        if (target == null) {
            source.sendFeedback("Can't find user " + targetName + ". No tp.");
        } else {
            switchDim(source, player, target);
            teleport(player, target.x, target.y, target.z);
            source.sendFeedback("Teleporting " + player.name + " to " + targetName + ".");
        }
    }

    @Environment(EnvType.SERVER)
    public static void teleportPlayerToPlayer(SharedCommandSource source, String playerName, String targetName) {
        PlayerEntity player = ServerUtil.getConnectionManager().getPlayer(playerName);
        PlayerEntity target = ServerUtil.getConnectionManager().getPlayer(targetName);
        if (player == null) {
            source.sendFeedback("Can't find user " + playerName + ". No tp.");
            return;
        }
        else if (target == null) {
            source.sendFeedback("Can't find user " + targetName + ". No tp.");
            return;
        } else {
            switchDim(source, player, target);
            teleport(player, target.x, target.y, target.z);
            source.sendFeedback("Teleporting " + player.name + " to " + targetName + ".");
        }
    }

    private static void switchDim(SharedCommandSource source, PlayerEntity player, PlayerEntity target) {
        if (player.dimensionId != target.dimensionId) {
            if (!FabricLoader.getInstance().isModLoaded("station-dimensions-v0")) {
                source.sendFeedback("User " + player.name + " and " + target.name + " are in different dimensions. No tp.");
                return;
            }
            Optional<Identifier> dim = DimensionRegistry.INSTANCE.getId(target.dimensionId);
            if (dim.isPresent()) {
                DimensionHelper.switchDimension(player, dim.get(), 1, new BareTravelAgent());
            } else {
                source.sendFeedback("User " + player.name + " and " + target.name + " are in different dimensions. No tp.");
                return;
            }
        }
    }

    public static void tpa(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            commandSource.sendFeedback("You must be a player to tpa.");
            return;
        }

        if (parameters.length > 1) {
            World world = ServerUtil.getServer().getWorld(0);

            if (parameters[1].equals("accept")) {
                for (int i = requests.size() - 1; i >= 0; i--) {
                    Tpa.Request request = requests.get(i);
                    if (request.to.equals(player.name)) {
                        if (world.getTime() - request.time > 2400) {
                            commandSource.sendFeedback("This tpa request has expired");
                            requests.remove(request);
                            return;
                        } else {
                            PlayerEntity from = ServerUtil.getConnectionManager().getPlayer(request.from);
                            if (from != null) {
                                ServerUtil.teleportPlayerToPlayer(commandSource, request.from, request.to);
                            } else {
                                commandSource.sendFeedback(request.from + " is no longer online.");
                            }
                            return;
                        }
                    }
                }
            }

            PlayerEntity target = ServerUtil.getConnectionManager().getPlayer(parameters[1]);
            if (target == null) {
                commandSource.sendFeedback("Can't find user " + parameters[1] + ". No tpa.");
                return;
            }

            target.sendMessage("§7" + player.name + " wants to tp to you.");
            target.sendMessage("§7type \"/tpa accept\" to allow them to. (expires in 2 min)");

            requests.add(new Tpa.Request(player.name, target.name, world.getTime()));

        } else {
            commandSource.sendFeedback("You must have a target to tpa to.");
        }
    }

    public static void informPlayerOpStatus(String playerName) {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean("op", ServerUtil.isOp(playerName));
        ServerUtil.getConnectionManager().sendPacket(playerName, new GlassPacket(RetroCommands.MOD_ID, "op", nbt));
    }
}
