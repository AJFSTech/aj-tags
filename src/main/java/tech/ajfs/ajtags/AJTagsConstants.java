package tech.ajfs.ajtags;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AJTagsConstants {

  /**
   * The permission to use all /ajtags commands
   */
  public static String ADMIN_PERMISSION = "ajtags.admin";
  /**
   * The permission to use basic /tags commands
   */
  public static String USER_PERMISSION = "ajtags.use";
  /**
   * The permission to access and use the tag colouring GUI
   */
  public static String TAG_COLOUR_PERMISSION = "ajtags.colourtags";
  /**
   * The prefix to tag individual permission
   */
  public static String TAG_PERMISSION_PREFIX = "ajtags.tag.";

}
