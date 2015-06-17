package com.taehee.thcommon.crash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.taehee.thcommon.R;
import com.taehee.thcommon.util.EmailUtil;
import com.taehee.thcommon.util.FileManager;

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
        Intent intent = EmailUtil.getMailIntentForCrash(CrashReportActivity.this, FileManager.getFileForCrashLoad(getApplicationContext(), getIntent().getStringExtra("date")), getIntent().getStringExtra("STACKTRACE"));
        if (intent != null) {
          startActivity(intent);
        }
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
  
}
