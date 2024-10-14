package com.matthewperiut.spc.mixin.communicate;

import com.matthewperiut.spc.command.server.ServerUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.UpdateSignPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayPacketHandlerMixin {
    @Shadow public abstract void sendPacket(Packet arg);

    @Shadow private ServerPlayerEntity player;

    @Inject(method = "handleUpdateSign", at = @At("HEAD"), cancellable = true)
    void customPacket(UpdateSignPacket par1, CallbackInfo ci) {
        if (par1.x == 0 && par1.y == -1 && par1.z == 0) {
            if (par1.text[0].equals("op?")) {
                String[] contents = new String[]{"", "", "", ""};
                contents[0] = ServerUtil.isOp(player.name) ? "1" : "0";
                sendPacket(new UpdateSignPacket(0, -1, 0, contents));
                ci.cancel();
            }
        }
    }
}
