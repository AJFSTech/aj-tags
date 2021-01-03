package tech.ajfs.ajtags.tag;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagPlayerController;

public class AJTagPlayerControllerImpl implements AJTagPlayerController {

  private final Map<UUID, AJTagPlayer> tagPlayers;

  public AJTagPlayerControllerImpl() {
    this.tagPlayers = Maps.newConcurrentMap();
  }

  @Override
  public @Nullable AJTagPlayer getPlayer(@NotNull UUID uuid) {
    return this.tagPlayers.get(uuid);
  }

  public void setTagPlayer(@NotNull UUID uuid, @NotNull AJTagPlayer tagPlayer) {
    this.tagPlayers.put(uuid, tagPlayer);
  }

  public void unloadTagPlayer(@NotNull UUID uuid) {
    this.tagPlayers.remove(uuid);
  }
}
