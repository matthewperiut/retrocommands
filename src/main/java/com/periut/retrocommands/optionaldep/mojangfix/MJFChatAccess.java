package com.periut.retrocommands.optionaldep.mojangfix;

// TODO: MJF
//import pl.telvarost.mojangfixstationapi.client.text.chat.ChatScreenVariables;

import static com.periut.retrocommands.RetroCommands.mojangFix;

public class MJFChatAccess {
    public static Integer getCursorPosition() {
        try {
            return 0;
//            return ChatScreenVariables.chatCursorPosition;
        } catch (Exception e) {
            mojangFix = false;
            return 0;
        }
    }

    public static String getText() {
        try {
            return "";
//            return ChatScreenVariables.textField.getText();
        } catch (Exception e) {
            mojangFix = false;
            return "";
        }
    }

    public static void setText(String s) {
//        if (ChatScreenVariables.textField != null)
//            ChatScreenVariables.textField.setText(s);
    }
}
