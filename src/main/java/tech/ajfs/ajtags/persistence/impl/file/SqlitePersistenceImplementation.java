package tech.ajfs.ajtags.persistence.impl.file;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.persistence.PersistenceOptions;
import tech.ajfs.ajtags.persistence.StatementProcessor;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;
import tech.ajfs.ajtags.tag.AJTagModifier;
import tech.ajfs.ajtags.tag.AJTagModifier.AJTagModifierBuilder;
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
      "REPLACE INTO {prefix}" + TAGS_TABLE_NAME + " (tag_name, tag_display) VALUES (?, ?)";
  private static final String LOAD_TAG_QUERY =
      "SELECT DISTINCT tag_display FROM {prefix}" + TAGS_TABLE_NAME + " WHERE tag_name = ?;";

  // Player queries
  private static final String LOAD_PLAYER_QUERY = "SELECT equipped_tag_name, "
      + "equipped_modification_id FROM {prefix}" + USER_TABLE_NAME + " WHERE uuid = ?;";
  private static final String GET_PLAYER_MODS_QUERY =
      "SELECT modification_id, tag_name FROM {prefix}" + MODIFICATIONS_TABLE_NAME + " WHERE "
          + "modification_owner = ?;";
  private static final String GET_MOD_DATA_QUERY =
      "SELECT character_index, color FROM {prefix}" + MODIFICATIONS_DATA_TABLE_NAME + " WHERE "
          + "modification_id = ?";
  private static final String SAVE_PLAYER_DATA_QUERY =
      "REPLACE INTO {prefix}" + USER_TABLE_NAME
          + " (uuid, equipped_tag_name, equipped_modification_id) VALUES (?, ?, ?);";

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
          .prepareStatement(connection, CREATE_MODIFICATION_DATA_TABLE_QUERY)) {
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
    String uuidString = uuid.toString();

    AJTagPlayer player = api.getTagPlayerController().createPlayer(uuid);

    try (Connection connection = getConnection()) {
      Map<Integer, AJTagModifier> modifiers = new HashMap<>();

      try (PreparedStatement playerModsStatement =
          this.statementProcessor.prepareStatement(connection, GET_PLAYER_MODS_QUERY)) {
        playerModsStatement.setString(1, uuidString);

        Map<Integer, AJTag> modificationIds = new HashMap<>();
        List<Integer> invalidatedIds = new ArrayList<>();

        ResultSet results = playerModsStatement.executeQuery();
        while (results.next()) {
          int id = results.getInt("modification_id");
          String tagName = results.getString("tag_name");
          AJTag tag = api.getTagController().getTagByName(tagName);
          if (tag == null) {
            invalidatedIds.add(id);
          } else {
            modificationIds.put(id, tag);
          }
        }

        for (Map.Entry<Integer, AJTag> entry : modificationIds.entrySet()) {
          try (PreparedStatement modDataStatement =
              this.statementProcessor.prepareStatement(connection, GET_MOD_DATA_QUERY)) {
            modDataStatement.setInt(1, entry.getKey());

            AJTagModifierBuilder builder = new AJTagModifierBuilder(entry.getValue());

            ResultSet dataResults = modDataStatement.executeQuery();
            while (dataResults.next()) {
              int index = dataResults.getInt("character_index");
              ChatColor color = ChatColor.valueOf(dataResults.getString("color"));
              builder.setColour(index, color);
            }

            if (builder.isEmpty()) {
              invalidatedIds.add(entry.getKey());
            } else {
              modifiers.put(entry.getKey(), builder.build(entry.getKey()));
            }
          }
        }
      }

      for (AJTagModifier modifier : modifiers.values()) {
        player.addModifier(modifier);
      }

      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          LOAD_PLAYER_QUERY)) {
        statement.setString(1, uuidString);

        ResultSet results = statement.executeQuery();
        if (results.next()) {
          String tagName = results.getString("equipped_tag_name");
          if (tagName != null) {
            AJTag tag = api.getTagController().getTagByName(tagName);
            if (tag != null) {
              player.setTag(tag);

              int modifierId = results.getInt("modification_id");
              if (modifierId > 0) { // Will be 0 if null
                AJTagModifier modifier = modifiers.get(modifierId);
                if (modifier != null && modifier.getApplicableTag().equals(tag)) {
                  player.setModifier(modifier);
                } else {
                  savePlayer(player); // Update the inactive modifier
                }
              }
            } else {
              savePlayer(player); // Remove the inactive tag
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
    try (Connection connection = getConnection()) {
      try (PreparedStatement statement = this.statementProcessor.prepareStatement(connection,
          SAVE_PLAYER_DATA_QUERY)) {
        statement.setString(1, player.getUuid().toString());
        if (player.getActiveTag() == null) {
          statement.setNull(2, Types.VARCHAR);
          statement.setNull(3, Types.VARCHAR);
        } else {
          statement.setString(2, player.getActiveTag().getName());
          if (player.getActiveModifier() != null) {
            statement.setInt(3, player.getActiveModifier().getId());
          } else {
            statement.setNull(3, Types.INTEGER);
          }
        }

        statement.execute();
      }
    } catch (SQLException err) {
      err.printStackTrace();
    }
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
