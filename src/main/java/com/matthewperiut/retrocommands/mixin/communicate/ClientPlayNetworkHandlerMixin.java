package com.matthewperiut.retrocommands.mixin.communicate;

import com.matthewperiut.retrocommands.RetroCommands;
import net.glasslauncher.mods.networking.GlassPacket;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.UpdateSignPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow public abstract void sendPacket(Packet packet);

    @Inject(method = "onHello", at = @At("TAIL"))
    void askIfOp(LoginHelloPacket par1, CallbackInfo ci) {
        RetroCommands.mp_spc = false;
        RetroCommands.mp_op = false;
        sendPacket(new GlassPacket(RetroCommands.MOD_ID, "op", new NbtCompound()));
    }
}
