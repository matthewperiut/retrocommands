package com.matthewperiut.spc.mixin;

import com.matthewperiut.spc.SPC;
import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.optionaldep.mojangfix.MJFChatAccess;
import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Chat;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.matthewperiut.spc.SPC.mjf;
import static com.matthewperiut.spc.SPC.mp_op;

@Mixin(value = Chat.class, priority = 1100)
public abstract class ChatScreenMixin extends ScreenBase {
    @Shadow protected String getText;

    @Shadow private int ticksRan;
    @Unique private boolean autocomplete = false;
    @Unique private String[] suggestions = new String[0];
    @Unique private int chosen = 0;
    @Unique private int textWidthPixels = 0;
    @Unique private int textWidthPixelsBeforeCurrentWord = 0;
    @Unique private String currentWord = "";

    @Unique void setText(String s) {
        if (mjf) {
            MJFChatAccess.setText(s);
        }

        if (getText != null)
            getText = s;
    }

    @Unique String getText() {
        String result;
        if (mjf) {
            result = MJFChatAccess.getText();
        } else {
            result = getText;
        }

        if (result == null)
            result = "";

        return result;
    }

    @Unique void appendText(String s) {
        setText(getText + s);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressedInit(char i, int par2, CallbackInfo ci) {
        if (par2 == 15 && suggestions.length > 0) {
            autocomplete = true;
        }

        if (suggestions.length > 1) {
            if (adjustChosenSuggestion(par2))
                ci.cancel();
        }
    }

    @Inject(method = "keyPressed", at = @At("TAIL"))
    private void processInput(char i, int par2, CallbackInfo ci) {
        resetValues();

        if (getText().isEmpty()) {
            return;
        }

        String[] sections = getText().split(" ");
        if (sections.length == 0) {
            return;
        }

        currentWord = sections[sections.length - 1];
        fetchSuggestionsForCurrentWord(sections);

        if (getText().endsWith(" "))
            currentWord = "";

        if (suggestions.length > 1) {
            calculateTextWidthPixelsBeforeCurrentWord(sections);
        }
    }

    private void resetValues() {
        currentWord = "";
        suggestions = new String[0];
    }

    private static final List<String> list = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("me");
                add("kill");
                add("tell");
            }});

    private void fetchSuggestionsForCurrentWord(String[] sections) {
        Minecraft mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());

        if (sections.length == 1 && currentWord.length() > 1 && currentWord.charAt(0) == '/' && !getText().endsWith(" ")) {

            if (mc.level.isServerSide && !SPC.mp_spc) {
                suggestions = list.stream()
                        .filter(s -> s.startsWith(currentWord.substring(1)))
                        .map(s -> s.substring(getText().length() - 1))
                        .toArray(String[]::new);
            } else {
                suggestions = SPChatUtil.commands.stream()
                        .filter(c -> c.name().startsWith(currentWord.substring(1)))
                        .filter(c -> (!c.disableInSingleplayer() || mc.level.isServerSide))
                        .filter(c -> (SPC.mp_op || !c.needsPermissions() || !mc.level.isServerSide))
                        .map(c -> c.name().substring(getText().length() - 1))
                        .toArray(String[]::new);
            }
        } else {
            Command command = SPChatUtil.commands.stream()
                    .filter(c -> c.name().equals(sections[0].substring(1)))
                    .filter(c -> (!c.disableInSingleplayer() || mc.level.isServerSide))
                    .filter(c -> (SPC.mp_op || !c.needsPermissions() || !mc.level.isServerSide))
                    .findFirst().orElse(null);
            if (command != null && (!command.disableInSingleplayer() || mc.level.isServerSide)) {
                PlayerBase player = mc.player;
                SharedCommandSource source = new SharedCommandSource(player);
                suggestions = getText().endsWith(" ") ? command.suggestion(source, sections.length, "", getText()) : command.suggestion(source, sections.length - 1, currentWord, getText());
            }
        }

        textWidthPixels = this.textManager.getTextWidth("> " + this.getText());
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

    @Unique boolean tryMatch(String s) {

        try {
            AtomicBoolean valid = new AtomicBoolean(false);
            SPChatUtil.commands.stream().forEach(a -> {
                if (a.name().equals(s)) {
                    if (!minecraft.level.isServerSide) {
                        valid.set(true);
                    } else {
                        if (mp_op) {
                            valid.set(true);
                        } else {
                            if (!a.needsPermissions()) {
                                valid.set(true);
                            }
                        }
                    }
                }
            });
            if (valid.get()) {
                 return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void replace(int i, int j, float f, CallbackInfo ci) {
        this.fill(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        renderSuggestions(i,j,f);
        String alreadyRendered = "";
        drawTextWithShadow(this.textManager, "> ", 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 14737632);
        alreadyRendered += "> ";
        if (getText().startsWith("/") && !getText().contains(" ") && !tryMatch(getText().substring(1))) {
            drawTextWithShadow(this.textManager,  "/", 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 14737632);
            alreadyRendered += "/";
            drawTextWithShadow(this.textManager,  this.getText().substring(1), 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 0xFC5454);
            alreadyRendered += this.getText().substring(1);
        } else if (getText().startsWith("/")){
            if (!tryMatch(getText().split(" ")[0].substring(1))) {
                drawTextWithShadow(this.textManager,  this.getText(), 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 0xFC5454);
            } else {
                drawTextWithShadow(this.textManager,  this.getText(), 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 14737632);
            }
            alreadyRendered += this.getText();
        } else {
            drawTextWithShadow(this.textManager,  this.getText(), 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 14737632);
            alreadyRendered += this.getText();
        }
        drawTextWithShadow(this.textManager,  (ticksRan / 6 % 2 == 0 ? "_" : ""), 4 + textManager.getTextWidth(alreadyRendered), this.height - 12, 14737632);
        alreadyRendered += (ticksRan / 6 % 2 == 0 ? "_" : "");
        super.render(i, j, f);
        ci.cancel();
    }

    public void renderSuggestions(int j, int f, float par3) {
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
        this.drawTextWithShadow(this.textManager, suggestions[chosen], 4 + textWidthPixels, this.height - 12, 0xAAAAAA);
        if (autocomplete) {
            appendText(suggestions[chosen]);
            autocomplete = false;
            suggestions = new String[0];
        }
    }

    private void renderMultipleSuggestions() {
        int textWidthCurrentWord = textManager.getTextWidth(currentWord);
        textWidthPixelsBeforeCurrentWord = textManager.getTextWidth("> " + getText()) - textWidthCurrentWord;
        fill(textWidthPixelsBeforeCurrentWord + 4,
                this.height - 14 - (10 * suggestions.length),
                textWidthPixelsBeforeCurrentWord + textWidthCurrentWord + getMaxSuggestionWidth() + 4,
                this.height - 14,
                0xFF000000);

        for (int i = 0; i < suggestions.length; i++) {
            this.drawTextWithShadow(this.textManager, currentWord + suggestions[i], textWidthPixelsBeforeCurrentWord + 4, this.height - 12 - (10 * (i + 1)), i == chosen ? 0xfcfc00 : 0xFFFFFF);
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
