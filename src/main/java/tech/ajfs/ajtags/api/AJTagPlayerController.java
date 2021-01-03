package tech.ajfs.ajtags.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface is responsible for {@link AJTagPlayer} instances that are currently being
 * tracked by the plugin. It will not make any calls to persistence upon retrieval if the player is
 * not loaded
 */
public interface AJTagPlayerController {

  /**
   * Returns the {@link AJTagPlayer} instance from a given UUID
   *
   * @param uuid is the UUID of the player to get the {@link AJTagPlayer} instance of
   * @return the {@link AJTagPlayer} instance if it is loaded
   */
  @Nullable
  AJTagPlayer getPlayer(@NotNull UUID uuid);

}
