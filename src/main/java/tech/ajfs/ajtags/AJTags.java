package tech.ajfs.ajtags;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import tech.ajfs.ajtags.api.AJTagApi;
import tech.ajfs.ajtags.persistence.Persistence;
import tech.ajfs.ajtags.persistence.PersistenceFactory;
import tech.ajfs.ajtags.persistence.PersistenceOptions;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;
import tech.ajfs.ajtags.placeholder.impl.mvdw.AJTagsMvdwDisplayPlaceholder;
import tech.ajfs.ajtags.placeholder.impl.mvdw.AJTagsMvdwNamePlaceholder;
import tech.ajfs.ajtags.placeholder.impl.papi.AJTagsPapiPlaceholder;
import tech.ajfs.ajtags.tag.AJTagApiImpl;

public class AJTags extends JavaPlugin {

  private static final Logger LOGGER = Bukkit.getLogger();

  private Persistence persistence;
  private AJTagApi tagApi;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    loadDatabase();
    if (persistence == null) {
      LOGGER.warning("Could not connect to database.");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

    this.tagApi = new AJTagApiImpl(this.persistence);

    // Registering placeholders
    AJTagsPlaceholderProvider provider = new AJTagsPlaceholderProvider(this.tagApi);
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new AJTagsPapiPlaceholder(provider).register();
    }

    if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
      be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(this, "ajtags_tag",
          new AJTagsMvdwDisplayPlaceholder(provider));
      be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(this, "ajtags_name",
          new AJTagsMvdwNamePlaceholder(provider));
    }

    // Expose the tags API thorough the services provider
    Bukkit.getServicesManager()
        .register(AJTagApi.class, this.tagApi, this, ServicePriority.Highest);
  }

  @Override
  public void onDisable() {
    this.persistence.getImplementation().shutdown();
  }

  private void loadDatabase() {
    PersistenceOptions options = PersistenceOptions
        .fromSection(getConfig().getConfigurationSection("database"));

    if (options == null) {
      LOGGER.warning(
          "Invalid database type given. Please use 'sqlite', 'mariadb' or 'mysql' as the database.type.");
      return;
    }

    this.persistence = new PersistenceFactory(this).getInstance(options);
  }

}
