package tech.ajfs.ajtags.tag.impl;

import java.util.Map;
import lombok.EqualsAndHashCode;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.tag.AJTagModifier;

@EqualsAndHashCode
public class AJTagImpl implements AJTag {


  private final String name;
  private String formattedDisplay;
  private String strippedDisplay;

  public AJTagImpl(String name, String formattedDisplay) {
    this.name = name;
    this.formattedDisplay = formattedDisplay;
    this.strippedDisplay = ChatColor.stripColor(formattedDisplay);
  }

  @Override
  public @NotNull String getName() {
    return this.name;
  }

  @Override
  public @NotNull String getDisplay() {
    return this.formattedDisplay;
  }

  @Override
  public void setDisplay(@NotNull String display) {
    this.formattedDisplay = display;
  }

  @Override
  public String getDisplay(@NotNull AJTagModifier modifier) {
    if (modifier.getColorInsertions().size() == 0) {
      return getDisplay();
    }

    StringBuilder display = new StringBuilder(this.strippedDisplay);
    for (Map.Entry<Integer, ChatColor> entry : modifier.getColorInsertions().entrySet()) {
      int index = entry.getKey();
      String insertion = entry.getValue().toString();
      display.insert(index, insertion);
    }

    return display.toString();
  }

}
