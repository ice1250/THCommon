package com.taehee.thcommon;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.util.Log;

import com.taehee.thcommon.util.FileManager;
import com.taehee.thcommon.util.StringUtil;
import com.taehee.thcommon.util.THLog;
import com.taehee.thcommon.util.FileManager.FileMode;
import com.taehee.thcommon.util.FileManager.FolderMode;
import com.taehee.thcommon.util.FileManager.FolderType;

public class THApplication extends Application {
  
  private UncaughtExceptionHandler mUncaughtExceptionHandler;
  
  @Override
  public void onCreate() {
    setUncaughtExceptionHandler();
    super.onCreate();
  }
  
  private void setUncaughtExceptionHandler() {
    this.mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      
      @Override
      public void uncaughtException(Thread thread, final Throwable ex) {
        THLog.i("taehee", StringUtil.stackTraceToString(ex));
        
        Thread th = new Thread() {
          @Override
          public void run() {
            File file = FileManager.getFile(getApplicationContext(), "/log/" + StringUtil.changeDate("yyyyMMdd/HH:mm:ss", System.currentTimeMillis()) + ".txt", FolderType.FOLDER_EXTERNAL_FILES, FolderMode.CREATE_IF_NEEDED, FileMode.CREATE_IF_NEEDED);
            FileManager.stringToFile(StringUtil.stackTraceToString(ex), file);
          }
        };
        th.start();
        mUncaughtExceptionHandler.uncaughtException(thread, ex);
      }
    });
  }
}
