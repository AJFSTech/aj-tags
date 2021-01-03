package tech.ajfs.ajtags;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class AJTagsMessages {

  private final Map<String, String> messages;

  private AJTagsMessages() {
    this.messages = Maps.newHashMap();
  }

  private void setMessage(String key, String value) {
    this.messages.put(key, color(value));
  }

  private void setMessage(String key, List<String> value) {
    StringBuilder messageBuilder = new StringBuilder();
    for (String line : value) {
      messageBuilder.append(line).append("\n");
    }
    setMessage(key, messageBuilder.toString());
  }

  public void sendMessage(Player player, String key, Object... args) {
    if (!this.messages.containsKey(key.toLowerCase(Locale.ROOT))) {
      throw new IllegalArgumentException("Message key " + key + " is not a message key");
    }

    String message = this.messages.get(key);
    if (!message.isEmpty()) {
      message = replacePlaceholders(message, args);
      player.sendMessage(message);
    }

  }

  public static AJTagsMessages fromMessages(ConfigurationSection messagesSection) {
    AJTagsMessages messages = new AJTagsMessages();

    for (String key : messagesSection.getKeys(false)) {
      if (messagesSection.isList(key)) {
        messages.setMessage(key, messagesSection.getStringList(key));
      } else if (messagesSection.isString(key)) {
        messages.setMessage(key, messagesSection.getString(key));
      }
    }

    return messages;
  }

  private String replacePlaceholders(String message, Object... args) {
    for (int i = 0; i < args.length; i++) {
      String replacement = args[i].toString();
      message = message.replace("{" + i + "}", replacement);
    }
    return message;
  }

  private static String color(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

}
