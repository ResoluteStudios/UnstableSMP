package com.resolutestudios.unstablesmp.utils;

public class TextUtils {

    private static final String NORMAL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SMALL_CAPS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀѕᴛᴜᴠᴡхʏᴢᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀѕᴛᴜᴠᴡхʏᴢ";

    public static String toSmallCaps(String input) {
        // First translate color codes
        String colored = input.replaceAll("&([0-9a-fk-or])", "§$1");

        StringBuilder builder = new StringBuilder();
        boolean isColorCode = false;

        for (int i = 0; i < colored.length(); i++) {
            char c = colored.charAt(i);

            if (c == '§') {
                isColorCode = true;
                builder.append(c);
                continue;
            }
            if (isColorCode) {
                builder.append(c);
                isColorCode = false;
                continue;
            }

            int index = NORMAL.indexOf(c);
            if (index != -1) {
                builder.append(SMALL_CAPS.charAt(index));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
