package tech.ajfs.ajtags.persistence.impl.file;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.persistence.PersistenceOptions;
import tech.ajfs.ajtags.persistence.StatementProcessor;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;
import tech.ajfs.ajtags.tag.impl.AJTagImpl;

public class SqlitePersistenceImplementation implements PersistenceImplementation {

  private final AJTags plugin;
  private final PersistenceOptions options;
  private final StatementProcessor statementProcessor;


  public SqlitePersistenceImplementation(AJTags plugin, PersistenceOptions options) {
    this.plugin = plugin;
    this.options = options;
    this.statementProcessor = new StatementProcessor().addReplacement("{prefix}",
        options::getTablePrefix);
  }

  private static final String TAGS_TABLE_NAME = "tags";
  private static final String USER_TABLE_NAME = "users";
  private static final String MODIFICATIONS_TABLE_NAME = "modifications";
  private static final String MODIFICATIONS_DATA_TABLE_NAME = "modifications_data";

  // TODO: Move to a schema file
  private static final String CREATE_TAGS_TABLE_QUERY =
      "CREATE TABLE IF NOT EXISTS {prefix}" + TAGS_TABLE_NAME + " ("
          + "tag_name    VARCHAR(32) NOT NULL,"
          + "tag_display VARCHAR(64) NOT NULL,"
          + "PRIMARY KEY(tag_name)"
          + ");";

  private static final String CREATE_USER_TABLE_QUERY =
      "CREATE TABLE IF NOT EXISTS {prefix}" + USER_TABLE_NAME + " ("
          + "uuid                     CHAR(36) NOT NULL,"
          + "equipped_tag_name        VARCHAR(32),"
          + "equipped_modification_id INTEGER,"
          + "PRIMARY KEY(uuid)"
          + ");";

  private static final String CREATE_MODIFICATIONS_TABLE_QUERY =
      "CREATE TABLE IF NOT EXISTS {prefix}" + MODIFICATIONS_TABLE_NAME + " ("
          + "modification_id    INTEGER PRIMARY KEY NOT NULL,"
          + "modification_owner CHAR(36) NOT NULL,"
          + "tag_name           VARCHAR(32) NOT NULL"
          + ");";

  private static final String CREATE_MODIFICATION_DATA_TABLE_QUERY =
      "CREATE TABLE IF NOT EXISTS {prefix}" + MODIFICATIONS_DATA_TABLE_NAME + " ("
          + "modification_id           UNSIGNED INTEGER NOT NULL,"
          + "character_index           UNSIGNED INTEGER NOT NULL,"
          + "color                     VARCHAR(13) NOT NULL,"
          + "PRIMARY KEY(modification_id, character_index)"
          + ");";

  // Tag queries
  private static final String LOAD_TAGS_QUERY =
      "SELECT DISTINCT tag_name, tag_display FROM {prefix}" + TAGS_TABLE_NAME + ";";
  private static final String DELETE_TAG_QUERY =
      "DELETE FROM {prefix}" + TAGS_TABLE_NAME + " WHERE tag_name = ?;";
  private static final String UPSERT_TAG_QUERY =
      "INSERT INTO {prefix}" + TAGS_TABLE_NAME + " (tag_name, tag_display) VALUES (?, ?) ON "
          + "CONFLICT(tag_name) DO UPDATE SET tag_display = ?;";
  private static final String LOAD_TAG_QUERY =
      "SELECT DISTINCT tag_display FROM {prefix}" + TAGS_TABLE_NAME + " WHERE tag_name = ?;";

  // Player queries
  private static final String LOAD_PLAYER_QUERY = "SELECT equipped_tag_name, "
      + "equipped_modification_id FROM {prefix}" + TAGS_TABLE_NAME + " WHERE uuid = ?;";

  private String url;

  @Override
  public AJTags getPlugin() {
    return this.plugin;
  }

  @Override
  public boolean init() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException err) {
      err.printStackTrace();
      return false;
    }

    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdirs();
    }

    File file = new File(plugin.getDataFolder(), "ajtags.db");

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException err) {
        err.printStackTrace();
      }
    }

    this.url = "jdbc:sqlite:" + file.getAbsolutePath();

    try (Connection connection = this.getConnection()) {
      try (PreparedStatement tagsTable = this.statementProcessor.prepareStatement(connection,
          CREATE_TAGS_TABLE_QUERY)) {
        tagsTable.execute();
      }

      try (PreparedStatement usersTable = this.statementProcessor.prepareStatement(connection,
          CREATE_USER_TABLE_QUERY)) {
        usersTable.execute();
      }

      try (PreparedStatement modificationTable =
          this.statementProcessor.prepareStatement(connection,
              CREATE_MODIFICATIONS_TABLE_QUERY)) {
        modificationTable.execute();
      }

      try (PreparedStatement modificationDataTable = this.statementProcessor
          .prepareStatement(connection, CREATE_TAGS_TABLE_QUERY)) {
        modificationDataTable.execute();
      }
    } catch (SQLException err) {
      err.printStackTrace();
      return false;
    }

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
  public StatementProcessor getProcessor() {
    return this.statementProcessor;
  }

  @Override
  public @Nullable AJTagPlayer loadPlayer(UUID uuid) {
    AJTagsApi api = Bukkit.getServicesManager().getRegistration(AJTagsApi.class).getProvider();

    AJTagPlayer player = api.getTagPlayerController().createPlayer(uuid);

    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          LOAD_PLAYER_QUERY)) {
        statement.setString(1, uuid.toString());

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
          String tagName = resultSet.getString("tag_name");
          if (tagName != null) {
            AJTag tag = api.getTagController().getTagByName(tagName);
            if (tag != null) {
              player.setTag(tag);
            }
          }
        }
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }

    return player;
  }

  @Override
  public void savePlayer(@NotNull AJTagPlayer player) {

  }

  @Override
  public AJTag loadTag(@NotNull String name) {
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          LOAD_TAG_QUERY)) {
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
          return new AJTagImpl(name, resultSet.getString("tag_display"));
        }
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }

    return null;
  }

  @Override
  public void saveTag(@NotNull AJTag tag) {
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          UPSERT_TAG_QUERY)) {
        statement.setString(1, tag.getName());
        statement.setString(2, tag.getDisplay());
        statement.setString(3, tag.getDisplay());
        statement.execute();
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }
  }

  @Override
  public void deleteTag(@NotNull AJTag tag) {
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          DELETE_TAG_QUERY)) {
        statement.setString(1, tag.getName());
        statement.execute();
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }
  }

  @Override
  public Set<AJTag> getAllTags() {
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          LOAD_TAGS_QUERY)) {
        Set<AJTag> tags = Sets.newHashSet();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
          String tagName = resultSet.getString("tag_name");
          String tagDisplay = resultSet.getString("tag_display");
          tags.add(new AJTagImpl(tagName, tagDisplay));
        }
        return tags;
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }

    return Sets.newHashSet();
  }
}
