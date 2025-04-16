package com.matthewperiut.retrocommands.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixinChatEnabler {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isWorldRemote()Z", ordinal = 0))
    public boolean openChat(Minecraft instance) {
        return true;
    }
}
