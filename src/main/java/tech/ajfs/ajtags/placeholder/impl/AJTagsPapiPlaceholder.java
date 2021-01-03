package tech.ajfs.ajtags.placeholder.impl;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholder;

@RequiredArgsConstructor
public class AJTagsPapiPlaceholder extends PlaceholderExpansion implements AJTagsPlaceholder {

  private final AJTagController tagController;

  @Override
  public String getIdentifier() {
    return "ajtags";
  }

  @Override
  public String getAuthor() {
    return "AJFS Tech";
  }

  @Override
  public String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public String onRequest(OfflinePlayer player, String identifier) {
    if (identifier.equals("tag")) {
      return getDisplayTag(player.getUniqueId());
    }

    return null;
  }

  @Override
  public @NotNull String getDisplayTag(UUID playerUuid) {
    AJTag tag = tagController.getTag(playerUuid, false);
    return tag == null ? "" : tag.getDisplay();
  }

}
