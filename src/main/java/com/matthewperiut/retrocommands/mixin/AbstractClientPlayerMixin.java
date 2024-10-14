package com.matthewperiut.retrocommands.mixin;

import com.matthewperiut.retrocommands.util.SPChatUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class AbstractClientPlayerMixin {
    @Shadow
    protected Minecraft minecraft;

    @Inject(method = "sendChatMessage", at = @At("HEAD"))
    public void sendFeedbackInSP(String par1, CallbackInfo ci) {
        if (!((Minecraft) FabricLoader.getInstance().getGameInstance()).isWorldRemote()) // sp check
        {
            PlayerEntity player = ((PlayerEntity) (Object) this);
            if (par1.charAt(0) == '/') {
                // handle commands
                SPChatUtil.handleCommand(new SharedCommandSource(player), par1.substring(1), true);
            } else {
                ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.addChatMessage("<" + player.name + "> " + par1);
            }
        }
    }
}
