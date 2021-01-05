package tech.ajfs.ajtags.placeholder.impl.papi;

import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderRegistry;

public class AJTagsPapiPlaceholderRegistry implements AJTagsPlaceholderRegistry {

  @Override
  public void register(@NotNull AJTags plugin, @NotNull AJTagsPlaceholderProvider provider) {
    new AJTagsPapiPlaceholder(provider).register();
  }
}
