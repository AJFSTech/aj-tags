package tech.ajfs.ajtags.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface AJTagApi {

  /**
   * Returns the {@link AJTagController} instance
   *
   * @return the {@link AJTagController} instance
   */
  AJTagController getTagController();

  /**
   * Returns the {@link AJTagPlayerController} instance
   *
   * @return the {@link AJTagPlayerController} instance
   */
  AJTagPlayerController getTagPlayerController();


  /**
   * Returns a tag player from a given UUID. Will create a new {@link AJTagPlayer} instance if it is
   * not loaded (note this instance will not be cached or used again by the plugin)
   *
   * @param uuid is the uuid of the player to get
   * @return an {@link AJTagPlayer} instance of the player (will be from the {@link
   * AJTagPlayerController} if it is loaded, and if not will be an instance created by persistence
   */
  AJTagPlayer getTagPlayer(@NotNull UUID uuid);

}
