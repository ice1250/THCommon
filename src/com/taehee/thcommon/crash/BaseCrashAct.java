package com.taehee.thcommon.crash;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;

import com.taehee.thcommon.util.FileManager;
import com.taehee.thcommon.util.FileManager.FileMode;
import com.taehee.thcommon.util.FileManager.FolderMode;
import com.taehee.thcommon.util.FileManager.FolderType;
import com.taehee.thcommon.util.StringUtil;

public abstract class BaseCrashAct extends Activity implements UncaughtExceptionHandler {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Thread.setDefaultUncaughtExceptionHandler(this);
  }
  
  public long getAvailableInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSizeLong();
    long availableBlocks = stat.getAvailableBlocksLong();
    return availableBlocks * blockSize;
  }
  
  public long getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSizeLong();
    long totalBlocks = stat.getBlockCountLong();
    return totalBlocks * blockSize;
  }
  
  public String createInformationString() {
    String returnVal = "";
    
    returnVal += "Android Version : " + android.os.Build.VERSION.RELEASE;
    returnVal += "\n";
    returnVal += "Model : " + android.os.Build.MODEL;
    returnVal += "\n";
    returnVal += "Total Internal memory     : " + getTotalInternalMemorySize();
    returnVal += "\n";
    returnVal += "Available Internal memory : " + getAvailableInternalMemorySize();
    returnVal += "\n";
    return returnVal;
  }
  
  /**
   * Crash 이후 띄울 엑티비티를 지정한다.
   * 
   * @return
   */
  public abstract Class<?> getCrashReportAct();
  
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    String report = "";
    
    Date curDate = new Date();
    report += "Error Report collected on : " + curDate.toString();
    report += "\n";
    report += "\n";
    report += "Informations :";
    report += "\n";
    report += "==============";
    report += "\n";
    report += "\n";
    
    report += createInformationString();
    
    report += "\n\n";
    report += "Stack : \n";
    report += "======= \n";
    
    final Writer result = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(result);
    ex.printStackTrace(printWriter);
    String stacktrace = result.toString();
    report += stacktrace;
    
    Throwable cause = ex.getCause();
    if (cause != null) {
      report += "\n";
      report += "Cause : \n";
      report += "======= \n";
    }
    
    while (cause != null) {
      cause.printStackTrace(printWriter);
      report += result.toString();
      cause = cause.getCause();
    }
    
    printWriter.close();
    report += "****  End of current Report ***";
    
    final String report2 = report;
    final String date = StringUtil.changeDate("yyyyMMdd/HH:mm:ss", System.currentTimeMillis());
    
    Thread th = new Thread() {
      @Override
      public void run() {
        File file = FileManager.getFileForCrashSave(BaseCrashAct.this, date);
        FileManager.stringToFile(report2, file);
      }
    };
    th.start();

    if (getCrashReportAct() != null) {
      Intent intent = new Intent(this, getCrashReportAct());
      intent.putExtra("STACKTRACE", report);
      intent.putExtra("date", date);
      startActivity(intent);
    }
    
    Process.killProcess(Process.myPid());
    System.exit(10);
  }
}
