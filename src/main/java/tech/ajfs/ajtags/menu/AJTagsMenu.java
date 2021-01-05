package tech.ajfs.ajtags.menu;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.entity.Player;
import tech.ajfs.ajtags.AJTags;

public class AJTagsMenu {

  private static final String[] MAIN_GUI_LAYOUT = {
      "         ",
      "         ",
      "  p   t  ",
      "         ",
      "         "
  };

  private static final String[] TAGS_GUI_LAYOUT = {
      "         ",
      " ttttttt ",
      " ttttttt ",
      " ttttttt ",
      " p  c  n ",
  };

  private final InventoryGui mainMenu;
  private final InventoryGui tagsMenu;

  public AJTagsMenu(AJTags plugin, AJTagsMenuOptions options) {
    this.tagsMenu = new InventoryGui(plugin, "tags", MAIN_GUI_LAYOUT);

    this.mainMenu = new InventoryGui(plugin, options.getMainMenuTitle(), MAIN_GUI_LAYOUT);
    this.mainMenu.setFiller(options.getFillerItem());

    // Main Menu
    this.mainMenu.addElement(new StaticGuiElement(
        'p',
        options.getPresetsItem(),
        1,
        click -> {
          tagsMenu.show(click.getEvent().getWhoClicked());
          return true;
        }
    ));

    this.mainMenu.addElement(new StaticGuiElement(
        't',
        options.getTagsItem(),
        1,
        click -> {
          tagsMenu.show(click.getEvent().getWhoClicked());
          return true;
        }
    ));

    this.tagsMenu.setFiller(options.getFillerItem());

    GuiElementGroup tagGroup = new GuiElementGroup('t');



    this.tagsMenu.build();
    this.mainMenu.build();
  }

  public void open(Player player) {
    this.mainMenu.show(player);
  }

}
