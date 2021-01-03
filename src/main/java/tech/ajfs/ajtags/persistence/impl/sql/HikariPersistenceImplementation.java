package tech.ajfs.ajtags.persistence.impl.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.persistence.PersistenceOptions;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;

public abstract class HikariPersistenceImplementation implements PersistenceImplementation {

  private final PersistenceOptions options;
  private HikariDataSource dataSource;

  public HikariPersistenceImplementation(PersistenceOptions options) {
    this.options = options;
  }

  /**
   * Returns whether Aikar's HikariCP optimizations should be used. See:
   * https://github.com/aikar/db
   *
   * @return whether to use Aikar's database optimizations
   */
  protected abstract boolean useDatabaseOptimizations();

  /**
   * Allows any Hikari implementation to manually configure the HikariConifg beyond what this
   * implementation already sets up.
   *
   * @param config  is the hikari config
   * @param options are the persistence options
   */
  protected abstract void setupHikariConfig(HikariConfig config, PersistenceOptions options);

  @Override
  public final boolean init(@NotNull AJTags plugin) {
    HikariConfig config = new HikariConfig();

    config.setPoolName("ajtags-hikari");

    setupHikariConfig(config, this.options);

    if (useDatabaseOptimizations()) {
      config.addDataSourceProperty("cachePrepStmts", true);
      config.addDataSourceProperty("prepStmtCacheSize", 250);
      config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
      config.addDataSourceProperty("useServerPrepStmts", true);
      config.addDataSourceProperty("cacheCallableStmts", true);
      config.addDataSourceProperty("cacheResultSetMetadata", true);
      config.addDataSourceProperty("cacheServerConfiguration", true);
      config.addDataSourceProperty("useLocalSessionState", true);
      config.addDataSourceProperty("elideSetAutoCommits", true);
      config.addDataSourceProperty("alwaysSendSetIsolation", false);
    }

    config.addDataSourceProperty("serverTimezone", "UTC");

    config.setMaximumPoolSize(8);
    config.setMinimumIdle(2);
    config.setMaxLifetime(60000);
    config.setConnectionTimeout(30000);

    return true;
  }

  @Override
  public final void shutdown() {
    if (this.dataSource != null && !this.dataSource.isClosed()) {
      this.dataSource.close();
    }
  }

  @Override
  public @NotNull Connection getConnection() throws SQLException {
    if (this.dataSource == null) {
      throw new SQLException("Unable to get connection from Hikari Pool (data source is null)");
    }

    Connection connection = this.dataSource.getConnection();
    if (connection == null) {
      throw new SQLException("Unable to get connection from Hikari Pool (connection is null)");
    }

    return connection;
  }
}
