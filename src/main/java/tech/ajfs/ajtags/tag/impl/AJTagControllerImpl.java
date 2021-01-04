package tech.ajfs.ajtags.tag.impl;

import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

  public boolean init() {
    Set<AJTag> tags;
    try {
      tags = persistence.getImplementation().getAllTags();
    } catch (SQLException err) {
      err.printStackTrace();
      return false;
    }
    for (AJTag tag : tags) {
      this.tags.put(tag.getName(), tag);
    }
    return true;
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

    AJTag tag = new AJTagImpl(name, display);
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
