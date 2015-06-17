package com.taehee.thcommon.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.taehee.thcommon.conf.Conf;

public class EmailUtil {
  
  /**
   * 크래시 이후 메일 보내기
   * 
   * @param context
   * @param file
   *          첨부파일
   * @param body
   *          본문
   * @return 메일 인텐트
   */
  public static Intent getMailIntentForCrash(Context context, File file, String body) {
    Intent sendIntent = new Intent(Intent.ACTION_SEND);
    sendIntent.setType("plain/text");
    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { Conf.CRASH_EMAIL_ID }); // your developer email id
    sendIntent.putExtra(Intent.EXTRA_SUBJECT, Conf.CRASH_EMAIL_SUBJECT);
    if (body != null) {
      sendIntent.putExtra(Intent.EXTRA_TEXT, body);
    }
    if (file != null && file.exists()) {
      sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
    }
    sendIntent.setType("message/rfc822");
    return Intent.createChooser(sendIntent, "메일을 선택해 주세요");
  }
}
