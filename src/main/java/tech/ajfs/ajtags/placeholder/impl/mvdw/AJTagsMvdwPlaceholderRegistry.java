package tech.ajfs.ajtags.placeholder.impl.mvdw;

import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderRegistry;

public class AJTagsMvdwPlaceholderRegistry implements AJTagsPlaceholderRegistry {

  @Override
  public void register(@NotNull AJTags plugin, @NotNull AJTagsPlaceholderProvider provider) {
    be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "ajtags_tag",
        new AJTagsMvdwDisplayPlaceholder(provider));
    be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(plugin, "ajtags_name",
        new AJTagsMvdwNamePlaceholder(provider));
  }
}
