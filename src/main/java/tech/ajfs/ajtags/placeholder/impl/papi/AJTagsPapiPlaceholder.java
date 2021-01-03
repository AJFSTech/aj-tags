package tech.ajfs.ajtags.placeholder.impl.papi;

import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderImplementation;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;

@RequiredArgsConstructor
public class AJTagsPapiPlaceholder
    extends me.clip.placeholderapi.expansion.PlaceholderExpansion
    implements AJTagsPlaceholderImplementation {

  private final AJTagsPlaceholderProvider provider;

  public AJTagsPapiPlaceholder(AJTagsPlaceholderProvider provider) {
    this.provider = provider;
  }

  @Override
  public String getIdentifier() {
    return "ajtags";
  }

  @Override
  public String getAuthor() {
    return "AJFS Tech";
  }

  @Override
  public String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public String onRequest(OfflinePlayer player, String identifier) {
    if (identifier.equals("tag")) {
      return getProvider().getDisplayTag(player.getUniqueId());
    } else if (identifier.equals("name")) {
      return getProvider().getTagName(player.getUniqueId());
    }

    return null;
  }

  @Override
  public @NotNull AJTagsPlaceholderProvider getProvider() {
    return this.provider;
  }

}
