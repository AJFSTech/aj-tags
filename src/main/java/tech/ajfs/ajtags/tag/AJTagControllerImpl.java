package tech.ajfs.ajtags.tag;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagController;
import tech.ajfs.ajtags.persistence.Persistence;

public class AJTagControllerImpl implements AJTagController {

  private final Persistence persistence;
  private final Map<String, AJTag> tags;

  public AJTagControllerImpl(Persistence persistence) {
    this.persistence = persistence;
    this.tags = Maps.newConcurrentMap();
  }

  @Override
  public @Nullable AJTag getTagByName(@NotNull String name) {
    return this.tags.get(name.toLowerCase(Locale.ROOT));
  }

  @Override
  public AJTag createTag(@NotNull String name, @NotNull String display, boolean save) {
    if (this.tags.containsKey(name.toLowerCase(Locale.ROOT))) {
      throw new IllegalArgumentException("Tag with name " + name + " already exists");
    }

    AJTag tag = new AJTagImpl(name, display, this.persistence);
    this.tags.put(tag.getName().toLowerCase(Locale.ROOT), tag);

    if (save) {
      this.persistence.saveTag(tag);
    }

    return tag;
  }

  @Override
  public void deleteTag(@NotNull AJTag tag, boolean persistent) {

  }

  @Override
  public @NotNull Collection<@NotNull AJTag> getAllTags() {
    return new ArrayList<>(this.tags.values());
  }
}
