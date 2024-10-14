package com.matthewperiut.retrocommands.mixin;

import com.matthewperiut.retrocommands.command.server.ServerUtil;
import com.matthewperiut.retrocommands.util.SPChatUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayerPacketHandlerMixin {
    @Shadow private ServerPlayerEntity player;

    @Shadow public abstract void sendPacket(Packet arg);

    @Inject(method = "handleCommand", at = @At(value = "HEAD"), cancellable = true)
    private void parseRegularCommand(String string, CallbackInfo ci) {
        if (!ServerUtil.getConnectionManager().isOperator(player.name))
            if (SPChatUtil.handleCommand(new SharedCommandSource(this), string.substring(1), false))
                ci.cancel();
    }
}
