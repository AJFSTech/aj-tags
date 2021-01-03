package tech.ajfs.ajtags.tag;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.persistence.Persistence;

public class AJTagControllerImpl implements AJTagController {

  private final Persistence persistence;

  private final Map<UUID, AJTagPlayer> tagPlayers;

  private final Map<String, AJTag> tagsByName;

  public AJTagControllerImpl(Persistence persistence) {
    this.persistence = persistence;
    this.tagPlayers = Maps.newConcurrentMap();
    this.tagsByName = Maps.newConcurrentMap();
  }

  @Override
  public void reloadTags() {
    Set<AJTag> tagSet = this.persistence.getAllTags(this);
    this.tagsByName.clear();
    for (AJTag tag : tagSet) {
      this.tagsByName.put(tag.getName(), tag);
    }
  }

  @Override
  public @Nullable AJTag getTag(String name) {
    return null;
  }

  @Override
  public @NotNull AJTag createTag(String name, String display) {
    if (tagsByName.containsKey(name.toLowerCase(Locale.ROOT))) {
      return null;
    }

    this.database.createTag(name, display);

    return new AJTagImpl(name, display);
  }

  @Override
  public void setTag(@NotNull UUID uuid, @Nullable AJTag tag, boolean persist) {
    AJTagPlayer tagPlayer = this.tagPlayers.get(uuid);
    tagPlayer.setTag(tag);
    if (persist) {
      this.database.setPlayerTag(uuid, tag);
    }
  }

  @Override
  public @Nullable AJTag getTag(@NotNull UUID uuid, boolean lookup) {
    AJTagPlayer tagPlayer = this.tagPlayers.get(uuid);
    if (tagPlayer == null) {
      // Check the player is online first - if they are online and no tag is set then they don't
      // have one
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        String tagName = this.database.getEquippedTagName(uuid);
        if (tagName != null) {
          return this.tagsByName.get(tagName);
        }
      }
    } else {
      return tagPlayer.getTag();
    }

    return null;
  }

  @Override
  public Collection<AJTag> getAllTags() {
    return new ArrayList<>(this.tagsByName.values());
  }
}
