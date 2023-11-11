package com.matthewperiut.spc.optionaldep.mojangfix;

import pl.js6pak.mojangfix.client.text.chat.ChatScreenVariables;

import static com.matthewperiut.spc.SPC.mjf;

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
