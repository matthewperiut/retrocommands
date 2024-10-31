package com.matthewperiut.retrocommands.mixin.communicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.UpdateSignPacket;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Shadow public List players;

    @Shadow public abstract boolean sendPacket(String player, Packet packet);

    @Inject(method = "addPlayer", at = @At("TAIL"))
    public void sendPlayersToAll(ServerPlayerEntity par1, CallbackInfo ci) {

        String playerNames = "";
        for (Object object : players) {
            PlayerEntity player = (PlayerEntity) object;
            playerNames += player.name + ",";
        }

        playerNames = playerNames.substring(0, playerNames.length()-1);
        String[] contents = new String[]{"players", playerNames, "", ""};

        for (Object object : players) {
            PlayerEntity player = (PlayerEntity) object;
            sendPacket(player.name, new UpdateSignPacket(0, -1, 0, contents));
        }

    }
}
