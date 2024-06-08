package space.ngrix.config.backend.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftUtils {
    public static @NotNull String color(String text) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}