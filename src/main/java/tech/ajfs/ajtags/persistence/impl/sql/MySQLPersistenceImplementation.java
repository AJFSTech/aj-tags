package tech.ajfs.ajtags.persistence.impl.sql;

import com.zaxxer.hikari.HikariConfig;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
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
    config.setJdbcUrl("jdbc:mysql://" + options.getHost() + ":" + options.getPort() + "/" + options
        .getDatabase());
    config.setUsername(options.getUsername());
    config.setPassword(options.getPassword());
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
  public void deleteTag(@NotNull AJTag tag) {

  }

  @Override
  public Set<AJTag> getAllTags() {
    return null;
  }
}
