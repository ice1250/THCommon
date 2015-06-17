package com.taehee.thcommon.util;

import android.util.Log;

import com.taehee.thcommon.conf.Conf;

public class THLog {
  
  private static boolean DEBUG_FLAG = true;
  private static boolean ERROR_FLAG = true;
  private static boolean INFO_FLAG = true;
  private static boolean WARNING_FLAG = true;
  private static boolean VERBOS_FLAG = true;
  
  public static void v(String message) {
    if (VERBOS_FLAG) {
      Log.v(Conf.LOGTAG, message);
    }
  }
  
  public static void v(String tag, String message) {
    if (VERBOS_FLAG) {
      String log = buildLogMsg(message);
      Log.v(tag, log);
    }
  }
  
  public static void d(String message) {
    if (VERBOS_FLAG) {
      Log.d(Conf.LOGTAG, message);
    }
  }
  
  public static void d(String tag, String message) {
    if (DEBUG_FLAG) {
      String log = buildLogMsg(message);
      Log.d(tag, log);
    }
  }
  
  public static void e(String message) {
    if (VERBOS_FLAG) {
      Log.e(Conf.LOGTAG, message);
    }
  }
  
  public static void e(String tag, String message) {
    if (ERROR_FLAG) {
      String log = buildLogMsg(message);
      Log.e(tag, log);
    }
  }
  
  public static void i(String message) {
    if (VERBOS_FLAG) {
      String log = buildLogMsg(message);
      Log.i(Conf.LOGTAG, log);
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
      Log.w(Conf.LOGTAG, log);
    }
  }
  
  public static void w(String tag, String message) {
    if (WARNING_FLAG) {
      String log = buildLogMsg(message);
      Log.w(tag, log);
    }
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
