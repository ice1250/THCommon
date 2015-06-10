package com.taehee.thcommon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.taehee.thcommon.util.THLog;

public class MainActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    THLog.i("hello world 2");
    findViewById(R.id.tvHello).setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        throw new NullPointerException();
      }
    });
  }
}
