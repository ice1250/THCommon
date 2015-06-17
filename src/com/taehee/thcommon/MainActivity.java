package com.taehee.thcommon;

import android.os.Bundle;
import android.view.View;

import com.taehee.thcommon.crash.BaseCrashAct;
import com.taehee.thcommon.crash.CrashReportActivity;

public class MainActivity extends BaseCrashAct {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.tvHello).setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        throw new NullPointerException();
      }
    });
  }
  
  @Override
  public Class<?> getCrashReportAct() {
    return CrashReportActivity.class;
  }
}
