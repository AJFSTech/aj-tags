package tech.ajfs.ajtags.placeholder;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface AJTagsPlaceholder {

  /**
   * Gets a player's tag's display. Will return "" if they don't have.
   *
   * @param playerUuid is the uuid of the player to get a tag for
   * @return the display tag (if it exists) as a string
   */
  @NotNull
  String getDisplayTag(UUID playerUuid);

}
