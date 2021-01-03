package tech.ajfs.ajtags.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;

public interface PersistenceImplementation {

  /**
   * Initializes the database connection
   *
   * @param plugin is the {@link AJTags} plugin instance
   */
  void init(@NotNull AJTags plugin);

  /**
   * Shuts down the database
   */
  void shutdown();

  /**
   * Gets a database connection
   *
   * @return a useable database connection
   * @throws SQLException if a connection cannot be made
   */
  @NotNull
  Connection getConnection() throws SQLException;

  /*
  AJ Tags Methods
   */

  /**
   * Save an {@link AJTagPlayer} to persistent storage
   *
   * @param player is the player to save
   */
  void saveUser(@NotNull AJTagPlayer player);

  /**
   * Loads an {@link AJTagPlayer} from persistent storage
   *
   * @param uuid is the uuid of the player to load
   * @return the {@link AJTagPlayer} instance
   */
  @Nullable
  AJTagPlayer loadPlayer(UUID uuid);

  /**
   * Loads an {@link AJTag} from persistent storage
   *
   * @param name is the name of the tag to load
   * @return an {@link AJTag} instance
   */
  AJTag loadTag(@NotNull String name);

  /**
   * Saves an {@link AJTag} to persistent storage
   *
   * @param tag is the {@link AJTag} to save
   */
  void saveTag(@NotNull AJTag tag);
}
