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

  private final AJTag applicableTag;
  private final Map<Integer, ChatColor> colorInsertions;

  public AJTagModifier(AJTag applicableTag, Map<Integer, ChatColor> colorInsertions) {
    this.applicableTag = applicableTag;
    this.colorInsertions = colorInsertions;
  }

  public static class AJTagModifierBuilder {

    private final AJTag applicableTag;
    private final Map<Integer, ChatColor> insertions;

    private ChatColor previousColour;
    private int index;

    public AJTagModifierBuilder(AJTag applicableTag) {
      this.applicableTag = applicableTag;
      this.insertions = Maps.newHashMap();

      this.previousColour = null;
      this.index = 0;
    }

    public void applyColour(@NotNull ChatColor color) {
      if (previousColour != null && previousColour == color) {
        index++;
      } else {
        previousColour = color;
        this.insertions.put(index, color);
      }
    }

    public AJTagModifier build() {
      return new AJTagModifier(this.applicableTag, this.insertions);
    }


  }


}
