package com.matthewperiut.retrocommands.mixin.communicate;

import com.matthewperiut.retrocommands.RetroCommands;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.UpdateSignPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow public abstract void sendPacket(Packet arg);

    @Inject(method = "onHello", at = @At("TAIL"))
    void askIfOp(LoginHelloPacket par1, CallbackInfo ci) {
        String[] contents = new String[]{"", "", "", ""};
        contents[0] = "op?";
        sendPacket(new UpdateSignPacket(0,-1,0, contents));
        RetroCommands.mp_spc = false;
        RetroCommands.mp_op = false;
    }

    @Inject(method = "handleUpdateSign", at = @At("HEAD"))
    void customPackets(UpdateSignPacket par1, CallbackInfo ci) {
        if (par1.x == 0 && par1.y == -1 && par1.z == 0) {
            if (par1.size() > 0) {
                if (par1.size() == 1) {
                    RetroCommands.mp_spc = true;
                    RetroCommands.mp_op = par1.text[0].startsWith("1");
                } else if (par1.text[0].startsWith("players")) {
                    RetroCommands.player_names = par1.text[1].split(",");
                }
            }
        }
    }
}
