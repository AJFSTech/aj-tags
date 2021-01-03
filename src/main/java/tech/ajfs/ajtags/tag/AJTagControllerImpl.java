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

public class AJTagControllerImpl implements AJTagController {

  private final Map<String, AJTag> tags;

  public AJTagControllerImpl() {
    this.tags = Maps.newConcurrentMap();
  }

  @Override
  public @Nullable AJTag getTagByName(@NotNull String name) {
    return this.tags.get(name.toLowerCase(Locale.ROOT));
  }

  @Override
  public AJTag createTag(@NotNull String name, @NotNull String display) {
    if (this.tags.containsKey(name.toLowerCase(Locale.ROOT))) {
      throw new IllegalArgumentException("Tag with name " + name + " already exists");
    }

    AJTag tag = new AJTagImpl(name, display);
    this.tags.put(tag.getName().toLowerCase(Locale.ROOT), tag);
    return tag;
  }

  @Override
  public void deleteTag(@NotNull AJTag tag) {
    this.tags.remove(tag.getName());
  }

  @Override
  public @NotNull Collection<@NotNull AJTag> getAllTags() {
    return new ArrayList<>(this.tags.values());
  }
}
