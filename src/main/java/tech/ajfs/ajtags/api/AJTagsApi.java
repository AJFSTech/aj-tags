package tech.ajfs.ajtags.api;

/**
 * The AJTags API should be powerful enough to do whatever you need to do. It is used internally for
 * all commands and GUI interactions, without any casting to implementations. Leave an issue on our
 * Github if you have any suggestions or comments on the API: https://github.com/AJFSTech/aj-tags
 *
 * @see AJTag
 * @see AJTagController
 * @see AJTagPlayer
 * @see AJTagPlayerController
 */
public interface AJTagsApi {

  /**
   * Returns the {@link AJTagController} instance
   *
   * @return the {@link AJTagController} instance
   */
  AJTagController getTagController();

  /**
   * Returns the {@link AJTagPlayerController} instance
   *
   * @return the {@link AJTagPlayerController} instance
   */
  AJTagPlayerController getTagPlayerController();


}
