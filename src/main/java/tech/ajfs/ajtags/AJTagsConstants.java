package tech.ajfs.ajtags;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AJTagsConstants {

  public static final int BSTATS_ID = 9884;

  /**
   * The permission to use all /ajtags commands
   */
  public static final String ADMIN_PERMISSION = "ajtags.admin";
  /**
   * The permission to use basic /tags commands
   */
  public static final String USER_PERMISSION = "ajtags.use";
  /**
   * The permission to access and use the tag colouring GUI
   */
  public static final String TAG_COLOUR_PERMISSION = "ajtags.colourtags";
  /**
   * The prefix to tag individual permission
   */
  public static final String TAG_PERMISSION_PREFIX = "ajtags.tag.";

}
