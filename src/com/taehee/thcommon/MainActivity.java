package com.taehee.thcommon;

import android.os.Bundle;
import android.view.View;

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
}
