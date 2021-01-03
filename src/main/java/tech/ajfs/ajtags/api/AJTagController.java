package tech.ajfs.ajtags.api;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public interface AJTagController {

  /**
   * Returns an {@link AJTag} based on it's name
   *
   * @param name is the name of the tag
   * @return the {@link AJTag} if it exists
   */
  @Nullable
  AJTag getTagByName(@NotNull String name);

  /**
   * Creates and stores a new {@link AJTag}.
   *
   * @param name    is the name of the tag
   * @param display is the display of the tag
   * @param save    is whether to save to persistence (will block if true)
   * @return a new {@link AJTag} instance
   */
  AJTag createTag(@NotNull String name, @NotNull String display, boolean save);

  /**
   * Deletes an {@link AJTag} if it is loaded. Will not remove from persistence.
   *
   * @param tag is the tag to delete
   * @param persistent is whether to remove the tag from persistence (will block if true)
   */
  void deleteTag(@NotNull AJTag tag, boolean persistent);

  /**
   * Returns an {@link Collection} of the loaded {@link AJTag}s
   *
   * @return a collection of all {@link AJTag}s that are loaded
   */
  @NotNull
  Collection<@NotNull AJTag> getAllTags();
}
