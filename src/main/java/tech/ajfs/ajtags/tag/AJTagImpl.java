package tech.ajfs.ajtags.tag;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.api.AJTag;

@EqualsAndHashCode
public class AJTagImpl implements AJTag {

  private final String name;
  private String display;

  public AJTagImpl(String name, String display) {
    this.name = name;
    this.display = display;
  }

  @Override
  public @NotNull String getName() {
    return this.name;
  }

  @Override
  public @NotNull String getDisplay() {
    return this.display;
  }

  @Override
  public void setDisplay(@NotNull String display) {
    this.display = display;
  }

}
