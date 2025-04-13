package com.matthewperiut.retrocommands.mixin;

import com.matthewperiut.retrocommands.RetroCommands;
import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.optionaldep.mojangfix.MJFChatAccess;
import com.matthewperiut.retrocommands.util.RetroChatUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.matthewperiut.retrocommands.RetroCommands.mjf;
import static com.matthewperiut.retrocommands.RetroCommands.mp_op;

@Mixin(value = ChatScreen.class, priority = 1100)
public abstract class ChatScreenMixin extends Screen {
    @Shadow protected String text;

    @Shadow private int focusedTicks;
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

        if (text != null)
            text = s;
    }

    @Unique String getText() {
        String result;
        if (mjf) {
            result = MJFChatAccess.getText();
        } else {
            result = text;
        }

        if (result == null)
            result = "";

        return result;
    }

    @Unique void appendText(String s) {
        setText(text + s);
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
        try {
            Minecraft mc = ((Minecraft) FabricLoader.getInstance().getGameInstance());

            if (getText().startsWith(" ")) {
                return;
            }

            if (sections.length == 1 && currentWord.length() > 1 && getText().charAt(0) == '/' && !getText().endsWith(" ")) {

                if (mc.world.isRemote && !RetroCommands.mp_spc) {
                    suggestions = list.stream()
                            .filter(s -> s.startsWith(currentWord.substring(1)))
                            .map(s -> s.substring(getText().length() - 1))
                            .toArray(String[]::new);
                } else {
                    suggestions = RetroChatUtil.commands.stream()
                            .filter(c -> c.name().startsWith(currentWord.substring(1)))
                            .filter(c -> (!c.disableInSingleplayer() || mc.world.isRemote))
                            .filter(c -> (RetroCommands.mp_op || !c.needsPermissions() || !mc.world.isRemote))
                            .map(c -> c.name().substring(getText().length() - 1))
                            .toArray(String[]::new);
                }
            } else {
                Command command = RetroChatUtil.commands.stream()
                        .filter(c -> c.name().equals(sections[0].substring(1)))
                        .filter(c -> (!c.disableInSingleplayer() || mc.world.isRemote))
                        .filter(c -> (RetroCommands.mp_op || !c.needsPermissions() || !mc.world.isRemote))
                        .findFirst().orElse(null);
                if (command != null && (!command.disableInSingleplayer() || mc.world.isRemote)) {
                    PlayerEntity player = mc.player;
                    SharedCommandSource source = new SharedCommandSource(player);
                    suggestions = getText().endsWith(" ") ? command.suggestion(source, sections.length, "", getText()) : command.suggestion(source, sections.length - 1, currentWord, getText());
                }
            }

            textWidthPixels = this.textRenderer.getWidth("> " + this.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            textWidthPixelsBeforeCurrentWord += textRenderer.getWidth(sections[j] + " ");
        }
    }

    @Unique boolean tryMatch(String s) {

        try {
            AtomicBoolean valid = new AtomicBoolean(false);
            RetroChatUtil.commands.stream().forEach(a -> {
                if (a.name().equals(s)) {
                    if (!minecraft.world.isRemote) {
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
        //drawTextWithShadow(this.textRenderer, "> ", 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 14737632);
        alreadyRendered += "> ";
        if (getText().startsWith("/") && !getText().contains(" ") && !tryMatch(getText().substring(1))) {
            //drawTextWithShadow(this.textRenderer,  "/", 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 14737632);
            alreadyRendered += "/";
            //drawTextWithShadow(this.textRenderer,  this.getText().substring(1), 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 0xFC5454);
            alreadyRendered += this.getText().substring(1);
        } else if (getText().startsWith("/")){
            if (!tryMatch(getText().split(" ")[0].substring(1))) {
                //drawTextWithShadow(this.textRenderer,  this.getText(), 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 0xFC5454);
            } else {
                //drawTextWithShadow(this.textRenderer,  this.getText(), 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 14737632);
            }
            alreadyRendered += this.getText();
        } else {
            //drawTextWithShadow(this.textRenderer,  this.getText(), 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 14737632);
            alreadyRendered += this.getText();
        }
        //drawTextWithShadow(this.textRenderer,  (focusedTicks / 6 % 2 == 0 ? "_" : ""), 4 + textRenderer.getWidth(alreadyRendered), this.height - 12, 14737632);
        boolean caretVisible = focusedTicks / 6 % 2 == 0;
        if (mjf) {
            int cursorPosition = MJFChatAccess.getCursorPosition();
            alreadyRendered = (new StringBuilder(alreadyRendered)).insert(alreadyRendered.length() + cursorPosition, caretVisible ? "_" : "").toString();
        } else {
            alreadyRendered += (caretVisible ? "_" : "");
        }
        drawTextWithShadow(this.textRenderer,  alreadyRendered, 4, this.height - 12, 14737632);
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
        this.drawTextWithShadow(this.textRenderer, suggestions[chosen], 4 + textWidthPixels, this.height - 12, 0xAAAAAA);
        if (autocomplete) {
            appendText(suggestions[chosen]);
            autocomplete = false;
            suggestions = new String[0];
        }
    }

    private void renderMultipleSuggestions() {
        int textWidthCurrentWord = textRenderer.getWidth(currentWord);
        textWidthPixelsBeforeCurrentWord = textRenderer.getWidth("> " + getText()) - textWidthCurrentWord;
        fill(textWidthPixelsBeforeCurrentWord + 4,
                this.height - 14 - (10 * suggestions.length),
                textWidthPixelsBeforeCurrentWord + textWidthCurrentWord + getMaxSuggestionWidth() + 4,
                this.height - 14,
                0xFF000000);

        for (int i = 0; i < suggestions.length; i++) {
            this.drawTextWithShadow(this.textRenderer, currentWord + suggestions[i], textWidthPixelsBeforeCurrentWord + 4, this.height - 12 - (10 * (i + 1)), i == chosen ? 0xfcfc00 : 0xFFFFFF);
        }
    }

    private int getMaxSuggestionWidth() {
        int maxWidth = 0;
        for (String suggestion : suggestions) {
            int width = textRenderer.getWidth(suggestion);
            if (maxWidth < width) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }
}
