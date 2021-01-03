package tech.ajfs.ajtags.tag;

import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayerController;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.persistence.Persistence;

public class AJTagsApiImpl implements AJTagsApi {

  private final AJTagController tagController;
  private final AJTagPlayerController playerController;

  public AJTagsApiImpl(Persistence persistence) {
    this.tagController = new AJTagControllerImpl(persistence);
    this.playerController = new AJTagPlayerControllerImpl(persistence);
  }

  @Override
  public AJTagController getTagController() {
    return this.tagController;
  }

  @Override
  public AJTagPlayerController getTagPlayerController() {
    return this.playerController;
  }

}
