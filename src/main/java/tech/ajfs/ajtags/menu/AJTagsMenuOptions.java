package tech.ajfs.ajtags.menu;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.ajfs.ajtags.util.ItemBuilder;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AJTagsMenuOptions {

  ItemStack fillerItem;
  ItemStack backPageItem;
  ItemStack nextPageItem;
  ItemStack pageInfoItem;
  ItemStack backMenuItem;

  String mainMenuTitle;
  ItemStack tagsItem;
  ItemStack presetsItem;

  public static AJTagsMenuOptions fromSection(ConfigurationSection menuSection) {
    return new AJTagsMenuOptions(
        ItemBuilder.fromSection(menuSection.getConfigurationSection("filler-item")).build(),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("back-page-item")).build(),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("next-page-item")).build(),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("page-info-item")).build(),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("back-menu-item")).build(),
        ChatColor.translateAlternateColorCodes('&', menuSection.getString("main.title")),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("main.tags-item")).build(),
        ItemBuilder.fromSection(menuSection.getConfigurationSection("main.presets-item")).build()
    );
  }

  public ItemStack getPageInfoItem(int page) {
    String stringPage = String.valueOf(page);
    ItemStack returning = this.pageInfoItem.clone();
    ItemMeta returningMeta = returning.getItemMeta();

    if (returningMeta.hasDisplayName()) {
      returningMeta.setDisplayName(returningMeta.getDisplayName().replace("{page}", stringPage));
    }
    if (returningMeta.hasLore()) {
      returningMeta.setLore(returningMeta.getLore().stream().map(line -> line.replace("{page}",
          stringPage)).collect(Collectors.toList()));
    }

    returning.setItemMeta(returningMeta);
    return returning;
  }

}
