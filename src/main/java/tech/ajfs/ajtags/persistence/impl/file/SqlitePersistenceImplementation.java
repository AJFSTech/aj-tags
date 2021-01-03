package tech.ajfs.ajtags.persistence.impl.file;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;

public class SqlitePersistenceImplementation implements PersistenceImplementation {

  private File file;
  private String url;


  @Override
  public boolean init(@NotNull AJTags plugin) {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
      return false;
    }

    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdirs();
    }

    this.file = new File(plugin.getDataFolder(), "ajtags.db");

    if (!this.file.exists()) {
      try {
        this.file.createNewFile();
      } catch (IOException err) {
        err.printStackTrace();
      }
    }

    this.url = "jdbc:sqlite:" + this.file.getAbsolutePath();
    return true;
  }

  @Override
  public void shutdown() {

  }

  @Override
  public @NotNull Connection getConnection() throws SQLException {
    if (this.url == null) {
      throw new SQLException("Unable to get connection from Sqlite (url is null)");
    }

    return DriverManager.getConnection(this.url);
  }

  @Override
  public @Nullable AJTagPlayer loadPlayer(UUID uuid) {
    return null;
  }

  @Override
  public void savePlayer(@NotNull AJTagPlayer player) {

  }

  @Override
  public AJTag loadTag(@NotNull String name) {
    return null;
  }

  @Override
  public void saveTag(@NotNull AJTag tag) {

  }

  @Override
  public Set<AJTag> getAllTags() {
    return null;
  }
}
