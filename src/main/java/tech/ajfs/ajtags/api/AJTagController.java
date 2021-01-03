package tech.ajfs.ajtags.api;

import java.util.Collection;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public interface AJTagController {

  /**
   * Reloads tags from persistence
   */
  void reloadTags();

  /**
   * Removes a player's tag
   *
   * @param uuid    is the uuid of the player to set the tag for
   * @param tag     is the tag to set
   * @param persist is whether the tag is to persistence
   */
  void setTag(@NotNull UUID uuid, @Nullable AJTag tag, boolean persist);

  /**
   * Removes a player's tag
   *
   * @param uuid    is the uuid of the player to remove the tag from
   * @param persist is whether the tag removal is written to persistence
   */
  default void removeTag(@NotNull UUID uuid, boolean persist) {
    setTag(uuid, null, persist);
  }

  /**
   * @param uuid   is the uuid of the player to get the tag for
   * @param lookup is whether to query persistence if they are not online
   * @return the player's {@link AJTag}
   */
  @Nullable
  AJTag getTag(@NotNull UUID uuid, boolean lookup);

  /**
   * Gets a tag by its name
   *
   * @param name is the name of the tag
   * @return an {@link AJTag} if it exists
   */
  @Nullable
  AJTag getTag(String name);

  /**
   * Creates a new {@link AJTag} and saves it to persistence
   *
   * @param name    is the unique name of the tag
   * @param display is the display of the tag
   * @return a new {@link AJTag}
   */
  @NotNull
  AJTag createTag(String name, String display);

  /**
   * Get's a collection of all possible tags
   *
   * @return a collection of all possible tags
   */
  Collection<AJTag> getAllTags();
}
