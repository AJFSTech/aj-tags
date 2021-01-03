package tech.ajfs.ajtags.persistence.impl.sql;

import com.zaxxer.hikari.HikariConfig;
import tech.ajfs.ajtags.persistence.PersistenceOptions;

public class MySQLPersistenceImplementation extends HikariPersistenceImplementation {

  public MySQLPersistenceImplementation(PersistenceOptions options) {
    super(options);
  }

  @Override
  protected boolean useDatabaseOptimizations() {
    return true;
  }

  @Override
  protected void setupHikariConfig(HikariConfig config, PersistenceOptions options) {
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    config.setJdbcUrl("jdbc:mysql://" + options.getHost() + ":" + options.getPort() + "/" + options.getDatabase());
    config.setUsername(options.getUsername());
    config.setPassword(options.getPassword());
  }
}
