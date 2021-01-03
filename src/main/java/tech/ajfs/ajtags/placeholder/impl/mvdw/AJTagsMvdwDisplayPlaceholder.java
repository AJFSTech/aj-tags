package tech.ajfs.ajtags.placeholder.impl.mvdw;

import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderImplementation;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;

public class AJTagsMvdwDisplayPlaceholder implements AJTagsPlaceholderImplementation,
    be.maximvdw.placeholderapi.PlaceholderReplacer {

  private final AJTagsPlaceholderProvider provider;

  public AJTagsMvdwDisplayPlaceholder(AJTagsPlaceholderProvider provider) {
    this.provider = provider;
  }

  @Override
  public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
    return getProvider().getDisplayTag(event.getOfflinePlayer().getUniqueId());
  }

  @Override
  public @NotNull AJTagsPlaceholderProvider getProvider() {
    return this.provider;
  }
}
