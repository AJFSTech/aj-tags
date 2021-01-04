package tech.ajfs.ajtags.placeholder;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagsApi;

public final class AJTagsPlaceholderProvider {

  private final AJTagsApi api;

  public AJTagsPlaceholderProvider(AJTagsApi api) {
    this.api = api;
  }

  /**
   * Gets a player's tag's display. Will return "" if they don't have.
   *
   * @param playerUuid is the uuid of the player to get a tag for
   * @return the display tag (if it exists) as a string
   */
  public @NotNull String getDisplayTag(@NotNull UUID playerUuid) {
    AJTagPlayer tagPlayer = api.getTagPlayerController().getPlayer(playerUuid, false);
    if (tagPlayer == null) {
      return "";
    }

    AJTag tag = tagPlayer.getActiveTag();
    return tag != null ? tag.getDisplay() : "";
  }

  public @NotNull String getTagName(@NotNull UUID playerUuid) {
    AJTagPlayer tagPlayer = api.getTagPlayerController().getPlayer(playerUuid, false);
    if (tagPlayer == null) {
      return "";
    }

    AJTag tag = tagPlayer.getActiveTag();
    return tag != null ? tag.getName() : "";
  }


}
