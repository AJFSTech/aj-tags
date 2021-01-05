package tech.ajfs.ajtags.placeholder;

import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;

public interface AJTagsPlaceholderRegistry {

  /**
   * Registers the placeholders
   *
   * @param plugin   is the {@link AJTags} instance to register the placeholder with
   * @param provider is the {@link AJTagsPlaceholderProvider} for the placeholder
   */
  void register(@NotNull AJTags plugin, @NotNull AJTagsPlaceholderProvider provider);

}
