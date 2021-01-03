package tech.ajfs.ajtags.placeholder;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;

public final class AJTagsPlaceholderProvider {

  private final AJTagController tagController;

  public AJTagsPlaceholderProvider(AJTagController tagController) {
    this.tagController = tagController;
  }

  /**
   * Gets a player's tag's display. Will return "" if they don't have.
   *
   * @param playerUuid is the uuid of the player to get a tag for
   * @return the display tag (if it exists) as a string
   */
  public @NotNull String getDisplayTag(@NotNull UUID playerUuid) {
    AJTag tag = tagController.getTag(playerUuid, false);
    return tag == null ? "" : tag.getDisplay();
  }

  public @NotNull String getTagName(@NotNull UUID playerUuid) {
    AJTag tag = tagController.getTag(playerUuid, false);
    return tag == null ? "" : tag.getName();
  }


}
