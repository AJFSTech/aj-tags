package tech.ajfs.ajtags.tag;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTagApi;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagPlayerController;
import tech.ajfs.ajtags.persistence.Persistence;

public class AJTagApiImpl implements AJTagApi {

  private final Persistence persistence;
  private final AJTagController tagController;
  private final AJTagPlayerController playerController;

  public AJTagApiImpl(Persistence persistence) {
    this.persistence = persistence;
    this.tagController = new AJTagControllerImpl();
    this.playerController = new AJTagPlayerControllerImpl();
  }

  @Override
  public AJTagController getTagController() {
    return this.tagController;
  }

  @Override
  public AJTagPlayerController getTagPlayerController() {
    return this.playerController;
  }

  @Override
  public AJTagPlayer getTagPlayer(@NotNull UUID uuid) {
    return this.playerController.getPlayer(uuid);
  }
}
