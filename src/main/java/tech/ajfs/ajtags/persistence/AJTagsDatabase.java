package tech.ajfs.ajtags.persistence;

import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.tag.AJTagControllerImpl;

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
   * @param uuid is the uuid of the player to set the tag for
   * @param tag  is the tag to apply to the player (null if unsetting)
   */
  void setPlayerTag(@NotNull UUID uuid, @Nullable AJTag tag);

  /**
   * Removes a player's tag from persistence
   *
   * @param uuid is the uuid of the player to remove the tag from
   */
  default void removePlayerTag(@NotNull UUID uuid) {
    setPlayerTag(uuid, null);
  }

  /**
   * Get's the player's equipped tag name
   *
   * @param uuid is the uuid of the player
   */
  @Nullable
  String getEquippedTagName(@NotNull UUID uuid);

  /**
   * Creates a tag in persistence
   *
   * @param name    is the name of the tag to create
   * @param display is the tag's default display
   */
  void createTag(String name, String display);

  /**
   * Deletes a tag from persistence
   *
   * @param name is the name of the tag to delete
   */
  void deleteTag(String name);

  /**
   * @param controller a controller to create the tags with
   * @return a set of all tags
   */
  Set<AJTag> getAllTags(AJTagControllerImpl controller);

}
