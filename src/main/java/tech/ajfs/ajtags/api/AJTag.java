package tech.ajfs.ajtags.api;

import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.tag.AJTagModifier;

public interface AJTag {

  /**
   * Gets the tag's name
   *
   * @return the tag's name
   */
  @NotNull
  String getName();

  /**
   * Gets a tag's display
   *
   * @return the formatted display
   */
  @NotNull
  String getDisplay();

  /**
   * Sets a tag's display (does not save)
   *
   * @param display is the display of the tag
   */
  void setDisplay(@NotNull String display);

  /**
   * Returns a tag's display with a given {@link AJTagModifier}
   *
   * @param modifier is the modifier to apply
   * @return the display with the modifier applied
   */
  String getDisplay(@NotNull AJTagModifier modifier);
}
