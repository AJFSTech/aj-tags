package tech.ajfs.ajtags.api;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   * Creates and stores a new {@link AJTag}. Will not save it to persistence, but will be useable as
   * it if was loaded from persistence.
   *
   * @param name    is the name of the tag
   * @param display is the display of the tag
   * @return a new {@link AJTag} instance
   */
  AJTag createTag(@NotNull String name, @NotNull String display);

  /**
   * Deletes an {@link AJTag} if it is loaded. Will not remove from persistence.
   *
   * @param tag is the tag to delete
   */
  void deleteTag(@NotNull AJTag tag);

  /**
   * Returns an {@link Collection} of the loaded {@link AJTag}s
   *
   * @return a collection of all tags
   */
  @NotNull
  Collection<@NotNull AJTag> getAllTags();
}
