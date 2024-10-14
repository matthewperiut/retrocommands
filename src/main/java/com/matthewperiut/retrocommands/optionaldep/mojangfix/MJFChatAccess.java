package com.matthewperiut.retrocommands.optionaldep.mojangfix;

import pl.telvarost.mojangfixstationapi.client.text.chat.ChatScreenVariables;

import static com.matthewperiut.retrocommands.RetroCommands.mjf;

public class MJFChatAccess {
    public static String getText() {
        try {
            return ChatScreenVariables.textField.getText();
        } catch (Exception e) {
            mjf = false;
            return "";
        }
    }

    public static void setText(String s) {
        if (ChatScreenVariables.textField != null)
            ChatScreenVariables.textField.setText(s);
    }
}
