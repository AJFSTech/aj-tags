package tech.ajfs.ajtags.placeholder.impl;

import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholder;

@RequiredArgsConstructor
public class AJTagsMvdwPlaceholder implements AJTagsPlaceholder,
    be.maximvdw.placeholderapi.PlaceholderReplacer {

  private final AJTagController tagController;

  @Override
  public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
    return getDisplayTag(event.getOfflinePlayer().getUniqueId());
  }

  @Override
  public @NotNull String getDisplayTag(UUID playerUuid) {
    AJTag tag = tagController.getTag(playerUuid, false);
    return tag == null ? "" : tag.getDisplay();
  }
}
