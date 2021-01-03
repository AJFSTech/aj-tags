package tech.ajfs.ajtags.persistence.impl.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;

public class SqlitePersistenceImplementation implements PersistenceImplementation {

  private File file;
  private String url;


  @Override
  public void init(@NotNull AJTags plugin) {

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
  }

  @Override
  public void shutdown() {

  }

  @Override
  public @NotNull Connection getConnection() throws SQLException {
    if (this.url == null) {
      throw new SQLException("Unable to get connection from Sqlite (url is null)");
    }
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
      return null;
    }
    try {
      return DriverManager.getConnection(this.url);
    } catch (SQLException err) {
      err.printStackTrace();
      return null;
    }
  }
}
