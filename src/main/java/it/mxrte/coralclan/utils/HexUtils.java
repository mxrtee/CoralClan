package it.mxrte.coralclan.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class HexUtils {
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public static String translateColorCodes(final String text) {
        final String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        final StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; ++i) {
            if (texts[i].equalsIgnoreCase("&")) {
                ++i;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                }
                else {
                    finalText.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            }
            else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

    public static TextComponent translateColorCodesToTextComponent(final String text) {
        final String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        final ComponentBuilder builder = new ComponentBuilder();
        for (int i = 0; i < texts.length; ++i) {
            final TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")) {
                ++i;
                if (texts[i].charAt(0) == '#') {
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(ChatColor.of(texts[i].substring(0, 7)));
                    builder.append((BaseComponent)subComponent);
                }
                else {
                    if (texts[i].length() > 1) {
                        subComponent.setText(texts[i].substring(1));
                    }
                    else {
                        subComponent.setText(" ");
                    }
                    switch (texts[i].charAt(0)) {
                        case '0': {
                            subComponent.setColor(org.bukkit.ChatColor.BLACK.asBungee());
                            break;
                        }
                        case '1': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_BLUE.asBungee());
                            break;
                        }
                        case '2': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_GREEN.asBungee());
                            break;
                        }
                        case '3': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_AQUA.asBungee());
                            break;
                        }
                        case '4': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_RED.asBungee());
                            break;
                        }
                        case '5': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_PURPLE.asBungee());
                            break;
                        }
                        case '6': {
                            subComponent.setColor(org.bukkit.ChatColor.GOLD.asBungee());
                            break;
                        }
                        case '7': {
                            subComponent.setColor(org.bukkit.ChatColor.GRAY.asBungee());
                            break;
                        }
                        case '8': {
                            subComponent.setColor(org.bukkit.ChatColor.DARK_GRAY.asBungee());
                            break;
                        }
                        case '9': {
                            subComponent.setColor(org.bukkit.ChatColor.BLUE.asBungee());
                            break;
                        }
                        case 'a': {
                            subComponent.setColor(org.bukkit.ChatColor.GREEN.asBungee());
                            break;
                        }
                        case 'b': {
                            subComponent.setColor(org.bukkit.ChatColor.AQUA.asBungee());
                            break;
                        }
                        case 'c': {
                            subComponent.setColor(org.bukkit.ChatColor.RED.asBungee());
                            break;
                        }
                        case 'd': {
                            subComponent.setColor(org.bukkit.ChatColor.LIGHT_PURPLE.asBungee());
                            break;
                        }
                        case 'e': {
                            subComponent.setColor(org.bukkit.ChatColor.YELLOW.asBungee());
                            break;
                        }
                        case 'f': {
                            subComponent.setColor(org.bukkit.ChatColor.WHITE.asBungee());
                            break;
                        }
                        case 'k': {
                            subComponent.setObfuscated(Boolean.valueOf(true));
                            break;
                        }
                        case 'l': {
                            subComponent.setBold(Boolean.valueOf(true));
                            break;
                        }
                        case 'm': {
                            subComponent.setStrikethrough(Boolean.valueOf(true));
                            break;
                        }
                        case 'n': {
                            subComponent.setUnderlined(Boolean.valueOf(true));
                            break;
                        }
                        case 'o': {
                            subComponent.setItalic(Boolean.valueOf(true));
                            break;
                        }
                        case 'r': {
                            subComponent.setColor(org.bukkit.ChatColor.RESET.asBungee());
                            break;
                        }
                    }
                    builder.append((BaseComponent)subComponent);
                }
            }
            else {
                builder.append(texts[i]);
            }
        }
        return new TextComponent(builder.create());
    }
}
