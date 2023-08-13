package com.matthewperiut.spc.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayer.class)
public class ClientPlayerMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendMessageInMP(String par1, CallbackInfo ci) {
        if (par1.startsWith("/clear")) {
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.clearChat();
            ci.cancel();
        }
    }
}
