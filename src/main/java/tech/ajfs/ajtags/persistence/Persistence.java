package tech.ajfs.ajtags.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;

@RequiredArgsConstructor
public final class Persistence {

  private final AJTags plugin;
  @Getter
  private final PersistenceImplementation implementation;

  public void init(AJTags plugin) {
    this.implementation.init(plugin);
  }

}
