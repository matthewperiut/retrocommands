package com.matthewperiut.retrocommands.mixin.intercept;

import com.matthewperiut.retrocommands.RetroCommands;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(MultiplayerClientPlayerEntity.class)
public class ClientPlayerMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendFeedbackInMP(String par1, CallbackInfo ci) {
        if (par1.startsWith("/clear")) {
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.clearChat();
            ci.cancel();
        }
        if (par1.startsWith("/perm")) {
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.addChatMessage("mp_op: " + RetroCommands.mp_op);
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.addChatMessage("mp_rc: " + RetroCommands.mp_rc);
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.addChatMessage("player_names: " + Arrays.toString(RetroCommands.player_names));
            ci.cancel();
        }
    }
}
