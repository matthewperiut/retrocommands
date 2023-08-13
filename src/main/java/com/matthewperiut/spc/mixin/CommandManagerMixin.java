package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.util.SPChatUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(method = "processCommand", at = @At(value = "HEAD"), cancellable = true)
    private void processSPCommands(Command par1, CallbackInfo ci) {

        List<PlayerBase> players = ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.players;
        String command = par1.commandString;
        if (command.startsWith("spchelp")) {
            command = command.replace("spchelp", "help");
        }

        for (PlayerBase p : players) {
            if (p.name == par1.source.getName()) {
                SPChatUtil.feedbackee = par1.source;
                SPChatUtil.handleCommand(p, command);
            }
        }

        String[] cancels = {"give", "time", "tp"};

        for (String cancel : cancels) {
            if (command.startsWith(cancel))
                ci.cancel();
        }
    }
}
