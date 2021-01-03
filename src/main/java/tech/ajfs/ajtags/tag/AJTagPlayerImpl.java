package tech.ajfs.ajtags.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayer;

public class AJTagPlayerImpl implements AJTagPlayer {

  private final UUID uuid;
  private final AJTagController tagController;

  private String equippedTagName;

  public AJTagPlayerImpl(UUID uuid, AJTagController tagController) {
    this.uuid = uuid;
    this.tagController = tagController;

    AJTag equipped = this.tagController.getTag(uuid, true);
    setTag(equipped);
  }

  @Override
  public UUID getUuid() {
    return this.uuid;
  }

  @Override
  public AJTag getTag() {
    return this.tagController.getTag(equippedTagName);
  }

  @Override
  public void setTag(@Nullable AJTag tag) {
    this.equippedTagName = tag == null ? null : tag.getName();
  }

  @Override
  public boolean canUseTag(AJTag tag) {
    Player player = getPlayer();
    return player.hasPermission("ajtags.tag." + tag.getName());
  }

  @Override
  public List<AJTag> getUseableTags() {
    Player player = getPlayer();
    List<AJTag> tagList = new ArrayList<>();

    if (player.hasPermission("ajtags.tag.*")) {
      return new ArrayList<>(this.tagController.getAllTags());
    }

    for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
      String permission = attachmentInfo.getPermission();
      if (!permission.startsWith("ajtags.tag")) {
        continue;
      }

      String tagName = permission.substring(10);
      AJTag tag = this.tagController.getTag(tagName);
      if (tag != null) {
        tagList.add(tag);
      }
    }

    return tagList;
  }

  @NotNull
  private Player getPlayer() {
    Player player = Bukkit.getPlayer(this.uuid);
    if (player == null) {
      throw new RuntimeException("Player is not online");
    }
    return player;
  }
}
