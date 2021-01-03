package tech.ajfs.ajtags.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.ajfs.ajtags.AJTagsConstants;
import tech.ajfs.ajtags.AJTagsMessages;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.api.AJTagsApi;
import tech.ajfs.ajtags.persistence.Persistence;

@RequiredArgsConstructor
@CommandAlias("ajtags")
public class AJTagsCommand extends BaseCommand {

  private final AJTagsApi api;
  private final Persistence persistence;
  private final AJTagsMessages messages;

  @Subcommand("help")
  @CatchUnknown
  @Default
  public void onDefault(Player sender) {
    if (sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "admin-help");
    } else if (sender.hasPermission(AJTagsConstants.USER_PERMISSION)) {
      messages.sendMessage(sender, "tags-help");
    } else {
      messages.sendMessage(sender, "no-permission");
    }
  }

  @Subcommand("createtag")
  public void onCreateTag(Player sender, String[] args) {
    if (!sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "no-permission");
      return;
    } else if (args.length != 2) {
      onDefault(sender);
      return;
    }

    String name = args[0];
    String display = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 1,
        args.length));

    AJTag tag = api.getTagController().getTagByName(name);
    if (tag != null) {
      messages.sendMessage(sender, "admin-tag-already-exists", name);
      return;
    }

    tag = api.getTagController().createTag(name, display, false);
    this.persistence.saveTag(tag).thenAccept(v ->
        messages.sendMessage(sender, "admin-tag-created", name, display)
    );
  }

  @Subcommand("deletetag")
  public void onDeleteTag(Player sender, String[] args) {
    if (!sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "no-permission");
      return;
    } else if (args.length != 1) {
      onDefault(sender);
      return;
    }

    String name = args[0];

    AJTag tag = api.getTagController().getTagByName(name);
    if (tag == null) {
      messages.sendMessage(sender, "tag-doesnt-exist", name);
      return;
    }

    this.persistence.deleteTag(tag).thenAccept(v ->
        messages.sendMessage(sender, "admin-tag-deleted", name)
    );
  }

  @Subcommand("modifytag")
  public void onModifyTag(Player sender, String[] args) {
    if (!sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "no-permission");
      return;
    } else if (args.length != 2) {
      onDefault(sender);
      return;
    }

    String name = args[0];
    String display = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 1,
        args.length));

    AJTag tag = api.getTagController().getTagByName(name);
    if (tag == null) {
      messages.sendMessage(sender, "tag-doesnt-exist", name);
      return;
    }

    tag.setDisplay(display);
    this.persistence.saveTag(tag).thenAccept(v ->
      this.messages.sendMessage(sender, "admin-tag-modified", name, display)
    );
  }

  @Subcommand("settag")
  public void onSetTag(Player sender, String[] args) {
    if (!sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "no-permission");
      return;
    } else if (args.length != 2) {
      onDefault(sender);
      return;
    }
    
    String senderName = args[0];
    String tagName = args[1];
    
    Player target = Bukkit.getPlayer(senderName);
    if (target == null) {
      this.messages.sendMessage(sender, "admin-player-not-found", sender);
      return;
    }

    AJTagPlayer tagPlayer = this.api.getTagPlayerController().getPlayer(target.getUniqueId(),
        false);
    if (tagPlayer == null) {
      this.messages.sendMessage(sender, "admin-player-not-found", sender);
      return;
    }

    AJTag tag = this.api.getTagController().getTagByName(tagName);
    if (tag == null) {
      this.messages.sendMessage(sender, "tag-doesnt-exist", tagName);
      return;
    }

    tagPlayer.setTag(tag);
    this.messages.sendMessage(sender, "admin-tag-set", target.getName(), tag.getName());
  }

  @Subcommand("removetag")
  public void onRemoveTag(Player sender, String[] args) {
    if (!sender.hasPermission(AJTagsConstants.ADMIN_PERMISSION)) {
      messages.sendMessage(sender, "no-permission");
      return;
    } else if (args.length != 1) {
      onDefault(sender);
      return;
    }

    String senderName = args[0];


    Player target = Bukkit.getPlayer(senderName);
    if (target == null) {
      this.messages.sendMessage(sender, "admin-player-not-found", sender);
      return;
    }

    AJTagPlayer tagPlayer = this.api.getTagPlayerController().getPlayer(target.getUniqueId(),
        false);
    if (tagPlayer == null) {
      this.messages.sendMessage(sender, "admin-player-not-found", sender);
      return;
    }

    tagPlayer.removeTag();
    this.messages.sendMessage(sender, "admin-tag-removed", target.getName());
  }

}
