package tech.ajfs.ajtags.tag;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;

/**
 * A serialized tag modifier. Used for custom tag modifications done by players
 */
@Getter
public final class AJTagModifier {

  private final int id;
  private final AJTag applicableTag;
  private final Map<Integer, ChatColor> colorInsertions;

  public AJTagModifier(int id, AJTag applicableTag, Map<Integer, ChatColor> colorInsertions) {
    this.id = id;
    this.applicableTag = applicableTag;
    this.colorInsertions = colorInsertions;
  }

  public static class AJTagModifierBuilder {

    private final AJTag applicableTag;
    private final String strippedTagDisplay;
    private final Map<Integer, ChatColor> insertions;

    private ChatColor previousColour;
    private int index;

    public AJTagModifierBuilder(AJTag applicableTag) {
      this.applicableTag = applicableTag;
      this.strippedTagDisplay = ChatColor.stripColor(applicableTag.getDisplay());
      this.insertions = Maps.newHashMap();

      this.previousColour = null;
      this.index = 0;
    }

    /**
     * Returns whether any colors have been applied
     *
     * @return whether the {@link AJTagModifierBuilder} is empty
     */
    public boolean isEmpty() {
      return this.insertions.isEmpty();
    }


    /**
     * @return whether the index is at the end of the string
     */
    public boolean isFinishedBuilding() {
      return this.index >= this.strippedTagDisplay.length();
    }

    /**
     * Incrementally sets the colour at the next index
     *
     * @param color is the {@link ChatColor} to apply at the next index
     */
    public void applyColour(@NotNull ChatColor color) {
      if (previousColour != null && previousColour == color) {
        index++;
      } else {
        previousColour = color;
        this.insertions.put(index, color);
      }
    }

    /**
     * Sets an {@link ChatColor} at a given index, ignoring the current builder index
     *
     * @param index is the index to set the {@link ChatColor} at
     * @param color is the {@link ChatColor} to set
     */
    public void setColour(int index, @NotNull ChatColor color) {
      this.insertions.put(index, color);
    }

    public AJTagModifier build(int id) {
      return new AJTagModifier(id, this.applicableTag, this.insertions);
    }


  }


}
