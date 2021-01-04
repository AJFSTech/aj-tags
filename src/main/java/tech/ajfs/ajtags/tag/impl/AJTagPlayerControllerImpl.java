package tech.ajfs.ajtags.tag.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagPlayerController;
import tech.ajfs.ajtags.persistence.Persistence;

public class AJTagPlayerControllerImpl implements AJTagPlayerController {

  private final Persistence persistence;
  private final AJTagController tagController;
  private final Map<UUID, AJTagPlayer> tagPlayers;

  public AJTagPlayerControllerImpl(Persistence persistence, AJTagController tagController) {
    this.persistence = persistence;
    this.tagController = tagController;
    this.tagPlayers = Maps.newConcurrentMap();
  }

  @Override
  public AJTagPlayer createPlayer(@NotNull UUID uuid) {
    return this.tagPlayers.put(uuid, new AJTagPlayerImpl(uuid, this.tagController));
  }

  @Override
  public @Nullable AJTagPlayer getPlayer(@NotNull UUID uuid, boolean lookup) {
    AJTagPlayer player = this.tagPlayers.get(uuid);
    if (player != null) {
      return player;
    } else if (!lookup) {
      return null;
    }

    return this.persistence.getImplementation().loadPlayer(uuid);
  }

  @Override
  public void savePlayer(@NotNull AJTagPlayer player) {
    this.persistence.getImplementation().savePlayer(player);
  }

  public void setTagPlayer(@NotNull UUID uuid, @NotNull AJTagPlayer tagPlayer) {
    this.tagPlayers.put(uuid, tagPlayer);
  }

  public void unloadTagPlayer(@NotNull UUID uuid) {
    this.tagPlayers.remove(uuid);
  }
}
