package tech.ajfs.ajtags;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.ajfs.ajtags.persistence.AJTagsDatabase;
import tech.ajfs.ajtags.persistence.DatabaseFactory;
import tech.ajfs.ajtags.persistence.DatabaseOptions;

public class AJTags extends JavaPlugin {

  private static final Logger LOGGER = Bukkit.getLogger();

  private AJTagsDatabase database;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    loadDatabase();

    if (database == null || !this.database.connect(this) || !this.database.createTables()) {
      LOGGER.warning("Could not connect to database.");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

  }

  @Override
  public void onDisable() {

  }

  private void loadDatabase() {
    DatabaseOptions options = DatabaseOptions.fromSection(
        getConfig().getConfigurationSection("database"));

    if (options == null) {
      LOGGER.warning(
          "Invalid database type given. Please use 'sqlite', 'mariadb' or 'mysql' as the database.type.");
      return;
    }

    this.database = new DatabaseFactory(options).createDatabase();
  }

}
