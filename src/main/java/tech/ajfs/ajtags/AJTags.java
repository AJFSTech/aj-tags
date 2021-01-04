package tech.ajfs.ajtags;

import co.aikar.commands.BukkitCommandManager;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.command.AJTagsCommand;
import tech.ajfs.ajtags.command.TagsCommand;
import tech.ajfs.ajtags.listener.PlayerConnectionListener;
import tech.ajfs.ajtags.menu.AJTagsMenu;
import tech.ajfs.ajtags.persistence.Persistence;
import tech.ajfs.ajtags.persistence.PersistenceFactory;
import tech.ajfs.ajtags.persistence.PersistenceOptions;
import tech.ajfs.ajtags.placeholder.AJTagsPlaceholderProvider;
import tech.ajfs.ajtags.placeholder.impl.papi.AJTagsPapiPlaceholder;
import tech.ajfs.ajtags.tag.impl.AJTagControllerImpl;
import tech.ajfs.ajtags.tag.impl.AJTagPlayerControllerImpl;
import tech.ajfs.ajtags.tag.impl.AJTagsApiImpl;

public class AJTags extends JavaPlugin {

  private static final Logger LOGGER = Bukkit.getLogger();

  private Persistence persistence;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    loadDatabase();
    if (persistence == null) {
      LOGGER.warning("Could not connect to database. Disabling.");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }

    AJTagsApi tagApi = new AJTagsApiImpl(this.persistence);
    Bukkit.getServicesManager()
        .register(AJTagsApi.class, tagApi, this, ServicePriority.Highest);

    // Registering placeholders
    AJTagsPlaceholderProvider provider = new AJTagsPlaceholderProvider(tagApi);
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new AJTagsPapiPlaceholder(provider).register();
    }

    /*
    if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
      be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(this, "ajtags_tag",
          new AJTagsMvdwDisplayPlaceholder(provider));
      be.maximvdw.placeholderapi.PlaceholderAPI.registerPlaceholder(this, "ajtags_name",
          new AJTagsMvdwNamePlaceholder(provider));
    }

     */

    // Expose the tags API thorough the services provider

    AJTagsMessages messages =
        AJTagsMessages.fromMessages(getConfig().getConfigurationSection("messages"));

    // Registering plugin listeners
    PluginManager pluginManager = Bukkit.getPluginManager();
    pluginManager.registerEvents(
        new PlayerConnectionListener((AJTagPlayerControllerImpl) tagApi.getTagPlayerController(),
            this.persistence), this);

    // Registering plugin commands
    BukkitCommandManager commandManager = new BukkitCommandManager(this);
    commandManager.registerCommand(new AJTagsCommand(tagApi, this.persistence, messages));
    commandManager.registerCommand(new TagsCommand(tagApi, new AJTagsMenu(), this.persistence,
        messages));

    // Once plugin is fully done with its initialization, load the data

    AJTagControllerImpl controllerImpl = (AJTagControllerImpl) tagApi.getTagController();

    if (!controllerImpl.init()) {
      LOGGER.warning("Could not load tags. Disabling");
      Bukkit.getPluginManager().disablePlugin(this);
    }

    // Will run when the server is fully loaded
    Bukkit.getScheduler().runTask(this, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        this.persistence.loadPlayer(player.getUniqueId());
      }
    });
  }

  @Override
  public void onDisable() {
    // Unregister API service
    Bukkit.getServicesManager().unregisterAll(this);
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
