package tech.ajfs.ajtags.placeholder;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AJTagsPlaceholderImplementation {

  /**
   * Gets the placeholder provider instance
   *
   * @return the {@link AJTagsPlaceholderProvider} provider instance
   */
  @NotNull
  AJTagsPlaceholderProvider getProvider();

}
