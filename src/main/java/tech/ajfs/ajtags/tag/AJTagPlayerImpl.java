package tech.ajfs.ajtags.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  private AJTag tag;

  public AJTagPlayerImpl(UUID uuid, AJTagController tagController) {
    this.uuid = uuid;
    this.tagController = tagController;
  }

  @Override
  public @NotNull UUID getUuid() {
    return this.uuid;
  }

  @Override
  public AJTag getTag() {
    return this.tag;
  }

  @Override
  public void setTag(@Nullable AJTag tag) {
    this.tag = tag;
  }

  @Override
  public boolean canUseTag(@NotNull AJTag tag) {
    Player player = Bukkit.getPlayer(this.uuid);
    if (player == null) {
      return false;
    }

    return player.hasPermission("ajtags.tag." + tag.getName());
  }

  @Override
  public @NotNull List<@NotNull AJTag> getUseableTags() {
    Player player = Bukkit.getPlayer(this.uuid);
    if (player == null) {
      return new ArrayList<>();
    }

    if (player.hasPermission("ajtags.tag.*")) {
      return new ArrayList<>(this.tagController.getAllTags());
    }

    List<AJTag> tags = new ArrayList<>();
    for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
      if (attachmentInfo.getPermission().startsWith("ajtags.tag.")) {
        String tagName = attachmentInfo.getPermission().substring(11);
        AJTag tag = this.tagController.getTagByName(tagName);
        if (tag != null) {
          tags.add(tag);
        }
      }
    }

    return tags;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.uuid);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AJTagPlayer) {
      return this.uuid.equals(((AJTagPlayer) obj).getUuid());
    }

    return false;
  }
}
