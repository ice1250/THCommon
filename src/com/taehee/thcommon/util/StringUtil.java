package com.taehee.thcommon.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Base64;

public class StringUtil {
  
  /**
   * 빈문자 체크
   * 
   * @param str
   * @return
   */
  public static boolean isEmpty(CharSequence str) {
    if (str == null || str.length() <= 0) {
      return true;
    }
    
    return false;
  }
  
  /**
   * 빈문자 && trim 체크
   * 
   * @param str
   * @return
   */
  public static boolean isTrimEmpty(String str) {
    if (str == null || str.trim().length() <= 0) {
      return true;
    }
    return false;
  }
  
  /**
   * 바이트 길이
   * 
   * @param data
   * @return
   */
  public static int getByteSize(String data) {
    return getByteSize(data, (char) 0);
  }
  
  /**
   * 바이트 길이
   * 
   * @param data
   *          String 데이터
   * @param except
   *          제외할 단어
   * @return
   */
  public static int getByteSize(String data, char except) {
    int size = 0;
    
    for (int i = 0; i < data.length(); i++) {
      if (except != 0 && data.charAt(i) == except) continue;
      
      if (data.charAt(i) > 127) size += 2;
      else size++;
    }
    
    return size;
  }
  
  /**
   * 문자열이 숫자 문자열인('0'~'9')지를 판별
   * 
   * @param data
   *          판별할 문자열
   * @return
   */
  public static boolean isDigit(String data) {
    if (data == null || data.length() <= 0) {
      return false;
    }
    
    for (int i = 0; i < data.length(); i++) {
      if (data.charAt(i) >= '0' && data.charAt(i) <= '9') {
        continue;
      } else {
        return false;
      }
    }
    return true;
  }
  
  /**
   * 문자열이 숫자 문자열인('0'~'9')지를 판별
   * 
   * @param data
   *          판별할 문자열
   * @param except
   *          제외할 문자열
   * @return
   */
  public static boolean isDigit(String data, String except) {
    data = data.replace(except, "");
    
    return isDigit(data);
  }
  
  /**
   * 문자열이 숫자 문자열인('0'~'9')지를 판별
   * 
   * @param data
   *          판별할 문자열
   * @param except
   *          제외할 문자열 리스트
   * @return
   */
  public static boolean isDigit(String data, String... except) {
    for (String string : except) {
      data = data.replace(string, "");
    }
    
    return isDigit(data);
  }
  
  /**
   * 밀리세컨->해당 포맷으로 시간표시
   * 
   * @param format
   *          시간 포맷(ex. yyyy/MM/dd)
   * @param time
   *          밀리세컨드
   * @return
   */
  public static String changeDate(String format, long time) {
    String newDate = null;
    try {
      Date orgDate = new Date(time);
      SimpleDateFormat newFormat = new SimpleDateFormat(format);
      newDate = newFormat.format(orgDate);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newDate;
  }
  
  /**
   * 이전 포맷의 데이터를 새로운 포맷으로 변경
   * 
   * @param oldformat
   *          이전포맷
   * @param newformat
   *          새로운 포맷
   * @param time
   *          이번 포맷의 시간
   * @return
   */
  public static String changeDate(String oldformat, String newformat, String time) {
    String newDate = null;
    try {
      SimpleDateFormat org = new SimpleDateFormat(oldformat);
      SimpleDateFormat newFormat = new SimpleDateFormat(newformat);
      
      Date orgDate = org.parse(time);
      newDate = newFormat.format(orgDate);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newDate;
  }
  
  /**
   * 
   * @param arr
   * @return
   */
  public static String getByteArrayToHexString(byte[] arr) {
    String hex = "";
    String _tmp = "";
    String div = " ";
    for (int i = 0; i < arr.length; i++) {
      _tmp = Integer.toHexString(arr[i] & 0xff).toUpperCase();
      if (_tmp.length() == 1) _tmp = "0" + _tmp;
      
      hex += "0x" + _tmp;
      
      if (i < arr.length - 1) hex += div;
    }
    
    return hex;
  }
  
  /**
   * 
   * @param arr
   * @return
   */
  public static String getByteArrayToHexStringNo0x(byte[] arr) {
    String hex = "";
    String _tmp = "";
    String div = "";
    for (int i = 0; i < arr.length; i++) {
      _tmp = Integer.toHexString(arr[i] & 0xff).toUpperCase();
      if (_tmp.length() == 1) _tmp = "0" + _tmp;
      
      hex += "" + _tmp;
      
      if (i < arr.length - 1) hex += div;
    }
    
    return hex;
  }
  
  /**
   * 
   * @param src
   * @param dest
   * @return
   */
  public static boolean equalData(String src, String dest) {
    
    if (null == src || null == dest) {
      return false;
    }
    
    return src.equals(dest);
  }
  
  /**
   * 
   * @param src
   * @param dest
   * @return
   */
  public static boolean equalIgnoreCaseData(String src, String dest) {
    
    if (null == src || null == dest) {
      return false;
    }
    if (src.equalsIgnoreCase(dest)) {
      return true;
    }
    
    return false;
  }
  
  /**
   * 
   * @param src
   * @param param
   * @return
   */
  public static boolean equalData(String src, String... param) {
    
    if (null == src || null == param) {
      return false;
    }
    int size = param.length;
    int checkSize = 0;
    for (String arg : param) {
      if (null == arg) {
        return false;
      }
      if (src.equals(arg)) {
        checkSize++;
      }
    }
    
    return checkSize == size;
  }
  
  /**
   * 
   * @param src
   * @param param
   * @return
   */
  public static boolean startsWithData(String src, String... param) {
    if (null == src) {
      throw new NullPointerException();
    }
    
    for (String arg : param) {
      if (null == arg) {
        throw new NullPointerException();
      }
      
      if (src.startsWith(arg)) {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * 전화 번호 분리
   * 
   * @param noStr
   * @return
   */
  public static String[] getSplitTellNo(String noStr) {
    Pattern tellPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
    
    if (noStr == null) return new String[] { "", "", "" };
    
    Matcher matcher = tellPattern.matcher(noStr);
    if (matcher.matches()) {
      return new String[] { matcher.group(1), matcher.group(2), matcher.group(3) };
    } else {
      return new String[] { "", "", "" };
    }
  }
  
  /**
   * 인증번호 분리
   * 
   * @param msg
   * @return
   */
  public static String getSplitAuthNo(String msg) {
    
    Pattern smsPattern = Pattern.compile("(.*인증번호\\[.*)+(\\d{6})+(.*\\].*)"); // ".*[wizzap] 인증번호\\[.*)+(\\d{6})+(.*\\].*)"
    // Pattern smsPattern = Pattern.compile("(.\n*인증번호\\[.*)+(\\d{6})+(.*\\].*)"); // .-->.\n 줄바꿈(\n) 포함 가능
    
    if (msg == null) return "";
    
    Matcher matcher = smsPattern.matcher(msg);
    // 전체 매칭이 아닌 부분 매칭으로 변경
    if (matcher.find()) {
      return matcher.group(2);
    } else {
      return "";
    }
  }
  
  /**
   * Exception을 String으로 뽑아준다.
   * 
   * @param th
   * @return
   */
  public static String stackTraceToString(Throwable th) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    
    Throwable cause = th;
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }
    String stacktraceAsString = result.toString();
    printWriter.close();
    return stacktraceAsString;
  }
  
  /**
   * Base64 인코딩
   * 
   * @param content
   * @return
   */
  public static String getBase64encode(String content) {
    return Base64.encodeToString(content.getBytes(), 0);
  }
  
  /**
   * Base64 디코딩
   * 
   * @param content
   * @return
   */
  public static String getBase64decode(String content) {
    return new String(Base64.decode(content, 0));
  }
  
  /**
   * URL 인코딩
   * 
   * @param content
   * @return
   */
  public static String getURLEncode(String content) {
    try {
      // return URLEncoder.encode(content, "utf-8"); // UTF-8
      return URLEncoder.encode(content, "euc-kr"); // EUC-KR
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * URL 디코딩
   * 
   * @param content
   * @return
   */
  public static String getURLDecode(String content) {
    try {
      // return URLDecoder.decode(content, "utf-8"); // UTF-8
      return URLDecoder.decode(content, "euc-kr"); // EUC-KR
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}