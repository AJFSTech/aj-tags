package tech.ajfs.ajtags.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface is responsible for {@link AJTagPlayer} instances
 */
public interface AJTagPlayerController {

  /**
   * Creates and internally stores a new {@link AJTagPlayer}
   *
   * @param uuid the uuid of the player
   * @return a new (and empty) {@link AJTagPlayer} instance
   */
  AJTagPlayer createPlayer(@NotNull UUID uuid);

  /**
   * Returns the {@link AJTagPlayer} instance from a given UUID
   *
   * @param uuid   is the UUID of the player to get the {@link AJTagPlayer} instance of
   * @param lookup is whether to lookup the player if they are not currently loaded. Note that
   *               setting this to true may block the main thread
   * @return the {@link AJTagPlayer} instance if it is loaded
   */
  @Nullable
  AJTagPlayer getPlayer(@NotNull UUID uuid, boolean lookup);

  /**
   * Saves a player to persistence. Will block the main thread
   *
   * @param player is the player to save
   */
  void savePlayer(@NotNull AJTagPlayer player);
}
