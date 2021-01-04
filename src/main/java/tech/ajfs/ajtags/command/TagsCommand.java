package tech.ajfs.ajtags.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import tech.ajfs.ajtags.AJTagsConstants;
import tech.ajfs.ajtags.AJTagsMessages;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.menu.AJTagsMenu;
import tech.ajfs.ajtags.persistence.Persistence;

@RequiredArgsConstructor
@CommandAlias("tags|tag")
public class TagsCommand extends BaseCommand {

  private final AJTagsApi api;
  private final AJTagsMenu menu;
  private final Persistence persistence;
  private final AJTagsMessages messages;

  @Subcommand("help")
  @CatchUnknown
  public void onDefault(Player sender) {
    if (sender.hasPermission(AJTagsConstants.USER_PERMISSION)) {
      this.messages.sendMessage(sender, "tags-help");
    } else {
      this.messages.sendMessage(sender, "no-permission");
    }
  }

  @Subcommand("menu")
  @Default
  public void onMenu(Player sender) {
    if (sender.hasPermission(AJTagsConstants.USER_PERMISSION)) {
      this.messages.sendMessage(sender, "menu-open");
      this.menu.open(sender);
    } else {
      this.messages.sendMessage(sender, "no-permission");
    }
  }

  @Subcommand("equip")
  @Default
  public void onEquip(Player sender, String[] args) {
    if (sender.hasPermission(AJTagsConstants.USER_PERMISSION)) {
      if (args.length != 1) {
        onDefault(sender);
        return;
      }

      String tagName = args[0];

      AJTag tag = this.api.getTagController().getTagByName(tagName);
      if (tag == null) {
        this.messages.sendMessage(sender, "tag-doesnt-exist", tagName);
        return;
      }

      AJTagPlayer tagPlayer = this.api.getTagPlayerController().getPlayer(sender.getUniqueId(), false);
      if (tagPlayer != null) {
        tagPlayer.setTag(tag);
        this.messages.sendMessage(sender, "tag-equipped", tag.getDisplay());
      } else {
        throw new RuntimeException("Tag player for player " + sender.getName() + " not loaded");
      }
    } else {
      messages.sendMessage(sender, "no-permission");
    }
  }

  @Subcommand("unequip")
  @Default
  public void onUnequip(Player sender) {
    if (sender.hasPermission(AJTagsConstants.USER_PERMISSION)) {
      AJTagPlayer tagPlayer = this.api.getTagPlayerController().getPlayer(sender.getUniqueId(), false);
      if (tagPlayer != null) {
        tagPlayer.removeTag();
        this.messages.sendMessage(sender, "tag-unequipped");
      } else {
        throw new RuntimeException("Tag player for player " + sender.getName() + " not loaded");
      }
    } else {
      this.messages.sendMessage(sender, "no-permission");
    }
  }


}
