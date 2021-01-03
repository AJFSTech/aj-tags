package tech.ajfs.ajtags;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.persistence.AJTagsDatabase;
import tech.ajfs.ajtags.persistence.DatabaseFactory;
import tech.ajfs.ajtags.persistence.DatabaseOptions;
import tech.ajfs.ajtags.placeholder.impl.AJTagsMvdwPlaceholder;
import tech.ajfs.ajtags.placeholder.impl.AJTagsPapiPlaceholder;
import tech.ajfs.ajtags.tag.AJTagControllerImpl;

public class AJTags extends JavaPlugin {

  private static final Logger LOGGER = Bukkit.getLogger();

  private AJTagsDatabase database;
  private AJTagController tagController;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    loadDatabase();

    if (database == null || !this.database.connect(this) || !this.database.createTables()) {
      LOGGER.warning("Could not connect to database.");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

    this.tagController = new AJTagControllerImpl(this.database);
    this.tagController.reloadTags();

    // Registering placeholders
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new AJTagsPapiPlaceholder(this.tagController).register();
    }

    if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
      be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(this, "ajtags_tag",
          new AJTagsMvdwPlaceholder(this.tagController));
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
