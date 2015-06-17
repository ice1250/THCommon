package com.taehee.thcommon;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.taehee.thcommon.util.FileManager;
import com.taehee.thcommon.util.FileManager.FileMode;
import com.taehee.thcommon.util.FileManager.FolderMode;
import com.taehee.thcommon.util.FileManager.FolderType;
import com.taehee.thcommon.util.THLog;

public class CrashReportActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    showDialog();
  }
  
  private void showDialog() {
    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
    alt_bld.setMessage("앱이 강제종료 되었습니다. 리포팅 해주세요.").setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // Action for 'Yes' Button
        sendErrorMail();
        finish();
      }
    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        finish();
      }
    });
    AlertDialog alert = alt_bld.create();
    alert.setTitle("에러보고");
    alert.setIcon(R.drawable.ic_launcher);
    alert.show();
  }
  
  private void sendErrorMail() {
    File file = FileManager.getFile(getApplicationContext(), "/log/" + getIntent().getStringExtra("date") + ".txt", FolderType.FOLDER_EXTERNAL_FILES, FolderMode.EXISTED_ONLY, FileMode.EXISTED_ONLY);
    if (file != null && file.exists()) {
      Intent sendIntent = new Intent(Intent.ACTION_SEND);
      String subject = "EasyKok Crash Report"; // here subject
      String body = getIntent().getStringExtra("STACKTRACE"); // here email body
      
      sendIntent.setType("plain/text");
      sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "ice1250@gmail.com" }); // your developer email id
      sendIntent.putExtra(Intent.EXTRA_TEXT, body);
      sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
      sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
      sendIntent.setType("message/rfc822");
      startActivity(Intent.createChooser(sendIntent, "메일을 선택해 주세요"));
    }
  }
  
}
