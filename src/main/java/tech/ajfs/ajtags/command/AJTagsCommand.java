package tech.ajfs.ajtags.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import tech.ajfs.ajtags.AJTagsMessages;
import tech.ajfs.ajtags.api.AJTagsApi;

@RequiredArgsConstructor
@CommandAlias("ajtags")
public class AJTagsCommand extends BaseCommand {

  private final AJTagsApi api;
  private final AJTagsMessages messages;


  @Subcommand("help")
  @CatchUnknown
  @Default
  public void onDefault(Player player) {
    messages.sendMessage(player, "help");
  }
}
