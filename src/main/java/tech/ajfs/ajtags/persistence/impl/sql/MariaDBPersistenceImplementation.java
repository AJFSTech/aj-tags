package tech.ajfs.ajtags.persistence.impl.sql;

import com.zaxxer.hikari.HikariConfig;
import tech.ajfs.ajtags.persistence.PersistenceOptions;

public class MariaDBPersistenceImplementation extends HikariPersistenceImplementation {

  public MariaDBPersistenceImplementation(PersistenceOptions options) {
    super(options);
  }

  @Override
  protected boolean useDatabaseOptimizations() {
    return true;
  }

  @Override
  protected void setupHikariConfig(HikariConfig config, PersistenceOptions options) {
    config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
    config.addDataSourceProperty("serverName", options.getHost());
    config.addDataSourceProperty("port", options.getPort());
    config.addDataSourceProperty("databaseName", options.getDatabase());
    config.setUsername(options.getUsername());
    config.setPassword(options.getPassword());
  }
}
