package tech.ajfs.ajtags.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.persistence.Persistence;
import tech.ajfs.ajtags.tag.impl.AJTagPlayerControllerImpl;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

  private final AJTagPlayerControllerImpl controllerImpl;
  private final Persistence persistence;

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent event) {
    persistence.loadPlayer(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    AJTagPlayer player = this.controllerImpl.unloadTagPlayer(event.getPlayer().getUniqueId());
    if (player != null) {
      persistence.savePlayer(player);
    }
  }

}
