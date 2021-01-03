package tech.ajfs.ajtags.api;

import org.jetbrains.annotations.NotNull;

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
   * Saves the tag to persistence (blocking call)
   */
  void save();
}
