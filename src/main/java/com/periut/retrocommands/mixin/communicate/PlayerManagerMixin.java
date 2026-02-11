package com.periut.retrocommands.mixin.communicate;

import com.periut.retrocommands.RetroCommandsNetworking;
import com.periut.retrocommands.util.ServerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow public List players;

    @Inject(method = "addPlayer", at = @At("TAIL"))
    public void sendPlayersToAll(ServerPlayerEntity par1, CallbackInfo ci) {

        ServerPlayNetworkHandler serverPlayNetworkHandler = (ServerPlayNetworkHandler) par1.networkHandler;
        ServerUtil.informPlayerOpStatus(serverPlayNetworkHandler.getName());
        ServerUtil.informPlayerDisabledCommands(serverPlayNetworkHandler.getName());

        String playerNames = "";
        for (Object object : players) {
            PlayerEntity player = (PlayerEntity) object;
            playerNames += player.name + ",";
        }

        playerNames = playerNames.substring(0, playerNames.length()-1);

        String finalPlayerNames = playerNames;
        for (Object object : players) {
            ServerPlayerEntity player = (ServerPlayerEntity) object;
            ServerPlayNetworking.send(player, RetroCommandsNetworking.PLAYERS_CHANNEL, buffer -> {
                buffer.writeString(finalPlayerNames);
            });
        }
    }
}
