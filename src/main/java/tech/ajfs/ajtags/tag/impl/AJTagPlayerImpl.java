package tech.ajfs.ajtags.tag.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.AJTagsConstants;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.tag.AJTagModifier;

public class AJTagPlayerImpl implements AJTagPlayer {

  private final UUID uuid;
  private final AJTagController tagController;

  private AJTag tag;
  private AJTagModifier modifier;


  public AJTagPlayerImpl(UUID uuid, AJTagController tagController) {
    this.uuid = uuid;
    this.tagController = tagController;
  }

  @Override
  public @NotNull UUID getUuid() {
    return this.uuid;
  }

  @Override
  public AJTag getActiveTag() {
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

    return player.hasPermission(AJTagsConstants.TAG_PERMISSION_PREFIX + tag.getName());
  }

  @Override
  public String getEffectiveDisplay() {
    return this.tag.getDisplay(this.modifier);
  }

  @Override
  public AJTagModifier getModifier() {
    return this.modifier;
  }

  @Override
  public void setModifier(@Nullable AJTagModifier modifier) {
    this.modifier = modifier;
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
