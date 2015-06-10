package com.taehee.thcommon.util;

import android.util.Log;

public class THLog {
  
  private static boolean DEBUG_FLAG = true;
  private static boolean ERROR_FLAG = true;
  private static boolean INFO_FLAG = true;
  private static boolean WARNING_FLAG = true;
  private static boolean VERBOS_FLAG = true;
  private static final String TAG = "TAEHEE";
  
  public static void v(String message) {
    if (VERBOS_FLAG) {
      Log.v(TAG, message);
    }
  }
  
  public static void v(String tag, String message) {
    if (VERBOS_FLAG) {
      String log = buildLogMsg(message);
      Log.v(TAG, log);
    }
  }
  
  public static void d(String message) {
    if (VERBOS_FLAG) {
      Log.d(TAG, message);
    }
  }
  
  public static void d(String tag, String message) {
    if (DEBUG_FLAG) {
      String log = buildLogMsg(message);
      Log.d(TAG, log);
    }
  }
  
  public static void e(String message) {
    if (VERBOS_FLAG) {
      Log.e(TAG, message);
    }
  }
  
  public static void e(String tag, String message) {
    if (ERROR_FLAG) {
      String log = buildLogMsg(message);
      Log.e(TAG, log);
    }
  }
  
  public static void i(String message) {
    if (VERBOS_FLAG) {
      String log = buildLogMsg(message);
      Log.i(TAG, log);
    }
  }
  
  public static void i(String tag, String message) {
    if (INFO_FLAG) {
      String log = buildLogMsg(message);
      Log.i(tag, log);
    }
  }
  
  public static void w(String message) {
    if (VERBOS_FLAG) {
      String log = buildLogMsg(message);
      Log.w(TAG, log);
    }
  }
  
  public static void w(String tag, String message) {
    if (WARNING_FLAG) {
      String log = buildLogMsg(message);
      Log.w(tag, log);
    }
  }
  
  private static String getFileName() {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
    return ste.getFileName();
  }
  
  private static String buildLogMsg(String message) {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
    StringBuilder sb = new StringBuilder();
    sb.append(ste.getMethodName());
    sb.append("(");
    sb.append(ste.getFileName());
    sb.append(":");
    sb.append(ste.getLineNumber());
    sb.append(") ");
    if (StringUtil.isEmpty(message) == false) {
      sb.append(message);
    }
    return sb.toString();
  }
  
}
