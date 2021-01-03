package tech.ajfs.ajtags.api;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AJTagPlayer {

  /**
   * Returns the player's UUID
   *
   * @return the player's UUID
   */
  @NotNull
  UUID getUuid();

  /**
   * Returns a players equipped tag
   *
   * @return the player's equipped {@link AJTag}
   */
  @Nullable
  AJTag getTag();

  /**
   * Sets a player's tag
   *
   * @param tag is the tag to set (null if none)
   */
  void setTag(@Nullable AJTag tag);

  /**
   * Removes a player's tag
   */
  default void removeTag() {
    setTag(null);
  }

  /**
   * Returns whether a player can use a given tag
   *
   * @param tag is the tag
   * @return whether the player can use the tag
   */
  boolean canUseTag(@NotNull AJTag tag);

  /**
   * Returns a list of tags a player can use
   *
   * @return a list of tags the player can use
   */
  @NotNull
  List<@NotNull AJTag> getUseableTags();
}
