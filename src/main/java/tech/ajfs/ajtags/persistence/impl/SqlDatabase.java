package tech.ajfs.ajtags.persistence.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.persistence.AJTagsDatabase;

public abstract class SqlDatabase implements AJTagsDatabase {

  protected HikariDataSource dataSource;

  /**
   * Gets a new {@link HikariConfig} for the database
   *
   * @param dataFolder is the plugins data folder
   * @return the {@link HikariConfig} for the SqlDatabase
   */
  @NotNull
  protected abstract HikariConfig getHikariConfig(@NotNull File dataFolder);

  @Override
  public final boolean connect(AJTags plugin) {
    HikariConfig config = getHikariConfig(plugin.getDataFolder());
    this.dataSource = new HikariDataSource(config);
    return true;
  }

}
