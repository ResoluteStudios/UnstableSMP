package com.resolutestudios.unstablesmp.utils;

import java.awt.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class ProgressBar {

    public static Component create(float percentage) {
        int totalBars = 20;
        int filledBars = (int) (percentage * totalBars);
        
        StringBuilder barBuilder = new StringBuilder();
        barBuilder.append("§8[§a");
        
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                barBuilder.append("█");
            } else if (i == filledBars) {
                barBuilder.append("§8"); // Switch directly to gray for empty
                barBuilder.append("░");
            } else {
                barBuilder.append("░");
            }
        }
        barBuilder.append("§8] - ");

        // Gradient Percentage
        int percentInt = (int) (percentage * 100);
        String percentText = TextUtils.toSmallCaps(percentInt + "%");
        
        Component barComp = Component.text(barBuilder.toString());
        Component percentComp = getGradientText(percentText, percentage);

        return barComp.append(percentComp);
    }

    private static Component getGradientText(String text, float percentage) {
        // Red (0.0) -> Yellow (0.5) -> Green (1.0)
        // Or simple Red -> Green
        
        // Simple interpolation
        int r, g, b = 0;
        
        if (percentage < 0.5) {
             // Red -> Yellow
             // 0.0: 255, 0, 0
             // 0.5: 255, 255, 0
             float ratio = percentage * 2;
             r = 255;
             g = (int) (255 * ratio);
        } else {
             // Yellow -> Green
             // 0.5: 255, 255, 0
             // 1.0: 0, 255, 0
             float ratio = (percentage - 0.5f) * 2;
             r = (int) (255 * (1 - ratio));
             g = 255;
        }
        
        TextColor color = TextColor.color(r, g, b);
        return Component.text(text).color(color);
    }
}
