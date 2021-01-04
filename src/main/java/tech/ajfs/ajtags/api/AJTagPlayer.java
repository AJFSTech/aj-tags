package tech.ajfs.ajtags.api;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.tag.AJTagModifier;

public interface AJTagPlayer {

  /**
   * Returns the player's UUID
   *
   * @return the player's UUID
   */
  @NotNull
  UUID getUuid();


  /**
   * Returns a players equipped @link AJTag}
   *
   * @return the player's equipped {@link AJTag}
   */
  @Nullable
  AJTag getActiveTag();

  /**
   * Returns the player's tags effective display (including {@link AJTagModifier} modifications)
   *
   * @return the player's effective display
   */
  String getEffectiveDisplay();

  /**
   * Sets a player's @link AJTag}
   *
   * @param tag is the @link AJTag} to set (null if none)
   */
  void setTag(@Nullable AJTag tag);

  /**
   * Removes a player's {@link AJTag}
   */
  default void removeTag() {
    setTag(null);
  }

  /**
   * Adds a modifier to the player (will save when the player is saved)
   *
   * @param modifier is the modifier to give to the player
   */
  void addModifier(@NotNull AJTagModifier modifier);

  /**
   * Returns a list of tag modifiers a player has from a given tag
   *
   * @param tag is the {@link AJTag} to get {@link AJTagModifier} for
   * @return a list of {@link AJTagModifier} that the player has for a given {@link AJTag}
   */
  @NotNull
  List<@NotNull AJTagModifier> getModifiers(@NotNull AJTag tag);

  /**
   * Returns the player's current {@link AJTagModifier}
   *
   * @return the current {@link AJTagModifier}
   */
  @Nullable
  AJTagModifier getActiveModifier();

  /**
   * Sets a player's current {@link AJTagModifier}
   *
   * @param modifier is the modifier to set
   */
  void setModifier(@Nullable AJTagModifier modifier);

  /**
   * Removes the player's current {@link AJTagModifier}
   */
  default void removeModifier() {
    setModifier(null);
  }

  /**
   * Returns whether a player can use a given @link AJTag}
   *
   * @param tag is the tag
   * @return whether the player can use the @link AJTag}
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
