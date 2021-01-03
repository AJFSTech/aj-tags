package tech.ajfs.ajtags.persistence;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import tech.ajfs.ajtags.AJTags;
import tech.ajfs.ajtags.api.AJTag;
import tech.ajfs.ajtags.api.AJTagPlayer;
import tech.ajfs.ajtags.persistence.impl.PersistenceImplementation;

@RequiredArgsConstructor
public final class Persistence {

  private final AJTags plugin;

  @Getter
  private final PersistenceImplementation implementation;

  public void init() {
    this.implementation.init(this.plugin);
  }


  CompletableFuture<AJTagPlayer> loadPlayer(UUID uuid) {
    return CompletableFuture.supplyAsync(() -> this.implementation.loadPlayer(uuid));
  }

  CompletableFuture<Void> savePlayer(@NotNull AJTagPlayer player) {
    return CompletableFuture.runAsync(() -> this.implementation.savePlayer(player));
  }

  CompletableFuture<AJTag> loadTag(@NotNull String name) {
    return CompletableFuture.supplyAsync(() -> this.implementation.loadTag(name));
  }

  CompletableFuture<Void> saveTag(@NotNull AJTag tag) {
    return CompletableFuture.runAsync(() -> this.implementation.saveTag(tag));
  }

  CompletableFuture<Set<AJTag>> getAllTags() {
    return CompletableFuture.supplyAsync(this.implementation::getAllTags);
  }

}
