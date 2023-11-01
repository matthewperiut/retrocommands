package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Chat;
import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Chat.class, priority = 900)
public abstract class ChatScreenMixin extends ScreenBase {
    @Shadow protected String getText;

    @Unique private boolean autocomplete = false;
    @Unique private String[] suggestions = new String[0];
    @Unique private int chosen = 0;
    @Unique private int textWidthPixels = 0;
    @Unique private int textWidthPixelsBeforeCurrentWord = 0;
    @Unique private String currentWord = "";

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void keyPressedInit(char i, int par2, CallbackInfo ci) {
        if ((par2 == 15 || par2 == 42) && suggestions.length > 0) {
            autocomplete = true;
        }
    }

    @Inject(method = "keyPressed", at = @At("TAIL"), cancellable = true)
    private void processInput(char i, int par2, CallbackInfo ci) {
        resetValues();

        if (getText.isEmpty()) {
            return;
        }

        String[] sections = getText.split(" ");
        if (sections.length == 0) {
            return;
        }

        currentWord = sections[sections.length - 1];
        fetchSuggestionsForCurrentWord(sections);



        if (getText.endsWith(" "))
            currentWord = "";

        if (suggestions.length > 1) {
            calculateTextWidthPixelsBeforeCurrentWord(sections);
            if (adjustChosenSuggestion(par2))
                ci.cancel();
        }
    }

    private void resetValues() {
        currentWord = "";
        suggestions = new String[0];
    }

    private void fetchSuggestionsForCurrentWord(String[] sections) {
        Minecraft mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());

        if (sections.length == 1 && currentWord.length() > 1 && currentWord.charAt(0) == '/' && !getText.endsWith(" ")) {
            suggestions = SPChatUtil.commands.stream()
                    .filter(c -> c.name().startsWith(currentWord.substring(1)))
                    .filter(c -> (!c.isOnlyServer() || mc.level.isServerSide))
                    .map(c -> c.name().substring(getText.length() - 1))
                    .toArray(String[]::new);
        } else {
            Command command = SPChatUtil.commands.stream()
                    .filter(c -> c.name().equals(sections[0].substring(1)))
                    .findFirst().orElse(null);
            if (command != null && (!command.isOnlyServer() || mc.level.isServerSide)) {
                PlayerBase player = mc.player;
                SharedCommandSource source = new SharedCommandSource(player);
                suggestions = getText.endsWith(" ") ? command.suggestion(source, sections.length, "", getText) : command.suggestion(source, sections.length - 1, currentWord, getText);
            }
        }

        textWidthPixels = this.textManager.getTextWidth("> " + this.getText);
    }

    private boolean adjustChosenSuggestion(int par2) {
        int initial = chosen;
        if (par2 == 200) { chosen++; }
        if (par2 == 208) { chosen--; }
        if (chosen < 0) { chosen = suggestions.length - 1; }
        if (chosen >= suggestions.length) { chosen = 0; }
        return initial != chosen;
    }

    private void calculateTextWidthPixelsBeforeCurrentWord(String[] sections) {
        for (int j = 0; j < sections.length - 1; j++) {
            textWidthPixelsBeforeCurrentWord += textManager.getTextWidth(sections[j] + " ");
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/Chat;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
    public void renderSuggestions(int j, int f, float par3, CallbackInfo ci) {
        try {
            if (suggestions.length > 0) {
                ensureChosenIsInRange();
                renderChosenSuggestion();
                if (suggestions.length > 1) {
                    renderMultipleSuggestions();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Unique
    private void ensureChosenIsInRange() {
        if (chosen < 0 || chosen >= suggestions.length) {
            chosen = 0;
        }
    }

    private void renderChosenSuggestion() {
        this.drawTextWithShadow(this.textManager, suggestions[chosen], 4 + textWidthPixels, this.height - 12, 8362928);
        if (autocomplete) {
            this.getText += suggestions[chosen];
            autocomplete = false;
            suggestions = new String[0];
        }
    }

    private void renderMultipleSuggestions() {
        int textWidthCurrentWord = textManager.getTextWidth(currentWord);
        textWidthPixelsBeforeCurrentWord = textManager.getTextWidth("> " + getText) - textWidthCurrentWord;
        fill(textWidthPixelsBeforeCurrentWord + 4,
                this.height - 14 - (10 * suggestions.length),
                textWidthPixelsBeforeCurrentWord + textWidthCurrentWord + getMaxSuggestionWidth() + 4,
                this.height - 14,
                0xFF000000);

        for (int i = 0; i < suggestions.length; i++) {
            this.drawTextWithShadow(this.textManager, currentWord + suggestions[i], textWidthPixelsBeforeCurrentWord + 4, this.height - 12 - (10 * (i + 1)), i == chosen ? 15454772 : 14737632);
        }
    }

    private int getMaxSuggestionWidth() {
        int maxWidth = 0;
        for (String suggestion : suggestions) {
            int width = textManager.getTextWidth(suggestion);
            if (maxWidth < width) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }
}
