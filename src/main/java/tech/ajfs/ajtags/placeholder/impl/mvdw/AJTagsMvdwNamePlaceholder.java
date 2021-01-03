package tech.ajfs.ajtags.placeholder.impl.mvdw;

import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderImplementation;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;

public class AJTagsMvdwNamePlaceholder implements AJTagsPlaceholderImplementation,
    be.maximvdw.placeholderapi.PlaceholderReplacer {

  private final AJTagsPlaceholderProvider provider;

  public AJTagsMvdwNamePlaceholder(AJTagsPlaceholderProvider provider) {
    this.provider = provider;
  }

  @Override
  public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
    return getProvider().getTagName(event.getOfflinePlayer().getUniqueId());
  }

  @Override
  public @NotNull AJTagsPlaceholderProvider getProvider() {
    return this.provider;
  }
}
