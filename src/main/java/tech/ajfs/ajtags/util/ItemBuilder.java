package tech.ajfs.ajtags.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Item Builder class to build items. Do not keep references to this class, it only serves as a
 * convenience to creating a new ItemStack.
 */
public final class ItemBuilder {

  private final ItemStack item;
  private final ItemMeta meta;

  /**
   * Creates an ItemBuilder instance of a given type with amount 1
   *
   * @param material is the material of the item
   */
  public ItemBuilder(Material material) {
    this(material, 1);
  }

  /**
   * Creates an ItemBuilder instance of a given type with a given amount
   *
   * @param material is the material of the item
   * @param amount   is the amount of the item
   */
  public ItemBuilder(Material material, int amount) {
    this(material, amount, (short) 0);
  }

  public ItemBuilder(Material material, int amount, int damage) {
    this(material, amount, (short) damage);
  }

  public ItemBuilder(Material material, int amount, short damage) {
    this.item = new ItemStack(material, amount, damage);
    this.meta = this.item.getItemMeta();
  }


  /**
   * Sets the name of the item
   *
   * @param name is the name to set
   * @return the item builder
   */
  public ItemBuilder withName(@NotNull String name) {
    this.meta.setDisplayName(color(name));
    return this;
  }

  /**
   * Sets the lore of the item
   *
   * @param lore is the lore to set
   * @return the item builder
   */
  public ItemBuilder withLore(@NotNull String... lore) {
    return withLore(Arrays.asList(lore));
  }

  /**
   * Sets the lore of the item
   *
   * @param lore is the lore to set
   * @return the item builder
   */
  public ItemBuilder withLore(@NotNull List<String> lore) {
    this.meta.setLore(lore.stream().map(ItemBuilder::color).collect(Collectors.toList()));
    return this;
  }

  /**
   * Sets the amount of the item
   *
   * @param amount is the amount to set
   * @return the item builder
   */
  public ItemBuilder withAmount(int amount) {
    Validate.isTrue(amount >= 0, "Amount cannot be negative");
    this.item.setAmount(amount);
    return this;
  }

  /**
   * Sets the type of the item
   *
   * @param material is the material to set
   * @return the item builder
   */
  public ItemBuilder withMaterial(@NotNull Material material) {
    this.item.setType(material);
    return this;
  }

  /**
   * Adds an enchantment to the item
   *
   * @param enchantment is the enchantment
   * @param level       is the level to set
   * @return the item builder
   */
  public ItemBuilder withEnchantment(@NotNull Enchantment enchantment, int level) {
    this.item.addEnchantment(enchantment, level);
    return this;
  }

  /**
   * Adds enchantments to the item
   *
   * @param enchantments is the map of enchantment/level to add
   * @return the item builder
   */
  public ItemBuilder withEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
    this.item.addEnchantments(enchantments);
    return this;
  }

  /**
   * Adds flags to the item
   *
   * @param flags are the flags to add
   * @return the item builder
   */
  public ItemBuilder withFlags(ItemFlag... flags) {
    this.meta.addItemFlags(flags);
    return this;
  }

  /**
   * Builds the item
   *
   * @return an itemstack of the item
   */
  public ItemStack build() {
    if (this.meta != null) {
      this.item.setItemMeta(this.meta);
    }
    return this.item;
  }

  public static ItemBuilder fromSection(ConfigurationSection section) {
    Material material = Material.valueOf(section.getString("material"));

    int amount = 1;
    if (section.contains("amount")) {
      amount = section.getInt("amount");
    }

    short damage = 0;
    if (section.contains("damage")) {
      damage = (short) section.getInt("damage");
    }

    ItemBuilder builder = new ItemBuilder(material, amount, damage);



    if (section.contains("name")) {
      builder.withName(section.getString("name"));
    }

    // Will be an empty list if not present
    builder.withLore(section.getStringList("lore"));

    return builder;
  }

  private static String color(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}