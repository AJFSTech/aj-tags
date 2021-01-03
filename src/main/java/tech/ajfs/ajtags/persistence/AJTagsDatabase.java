package tech.ajfs.ajtags.persistence;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.tag.Tag;

public interface AJTagsDatabase {

  /**
   * Initialize connection to the database
   *
   * @return whether the connection was successfully made
   */
  boolean connect(AJTags plugin);

  /**
   * Create the tables for the database
   *
   * @return whether the tables were created
   */
  boolean createTables();

  /**
   * Sets a player's tag in persistence
   *
   * @param player is the player to set the tag for
   * @param tag    is the tag to apply to the player (null if unsetting)
   */
 void setTag(@NotNull Player player, @Nullable Tag tag);

  /**
   * Removes a player's tag from persistence
   *
   * @param player is the player to remove the tag for
   */
  default void removeTag(@NotNull Player player) {
    setTag(player, null);
  }

  /**
   * Gets the Tag object of a player
   *
   * @return an @Tag instance of the player's tag
   */
  @Nullable
  Tag getTag(@NotNull Player player);
}
