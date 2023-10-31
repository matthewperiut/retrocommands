package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SPChatUtil;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Chat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chat.class)
abstract public class ChatScreenMixin extends ScreenBase {
    @Shadow protected String getText;

    @Unique boolean autocomplete;

    @Inject(method = "keyPressed", at = @At("HEAD"))
    protected void keyPressed(char i, int par2, CallbackInfo ci) {
        if (par2 == 15)
        {
            autocomplete = true;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/Chat;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
    public void render(int j, int f, float par3, CallbackInfo ci) {
        String[] sections = this.getText.split(" ");
        if (this.getText.length() > 0)
            if (sections.length > 0)
            {
                String currentWord = sections[sections.length-1];
                Command command = null;
                for (Command c : SPChatUtil.commands) {
                    if (c.name().equals(sections[0].substring(1)))
                    {
                        command = c;
                    }
                }
                if (command == null)
                {
                    return;
                }

                String suggestion = "";
                if (this.getText.charAt(this.getText.length() - 1) == ' ')
                    suggestion = command.suggestion(sections.length, "", getText);
                else
                    suggestion = command.suggestion(sections.length - 1, currentWord, getText);


                int width = this.textManager.getTextWidth("> " + this.getText);
                this.drawTextWithShadow(this.textManager, suggestion, 4 + width, this.height - 12, 8362928);

                if (autocomplete)
                {
                    this.getText += suggestion;
                    autocomplete = false;
                }
            }
    }
}
