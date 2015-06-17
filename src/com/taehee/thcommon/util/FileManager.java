package com.taehee.thcommon.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.taehee.thcommon.conf.Conf;
import com.taehee.thcommon.crash.BaseCrashAct;

public class FileManager {
  
  public enum FolderType {
    FOLDER_INTERNAL_ROOT, FOLDER_INTERNAL_CACHE, FOLDER_INTERNAL_FILES,
    
    FOLDER_EXTERNAL_ROOT, FOLDER_EXTERNAL_CACHE, FOLDER_EXTERNAL_FILES,
    
    FOLDER_OHTER, // 이때만 풀패스 경로를 받는다
  }
  
  /**
   * 폴더 모드
   */
  public enum FolderMode {
    EXISTED_ONLY, // 이미 존재하는 폴더
    CREATE_IF_NEEDED, // 폴더가 없으면 만들고 있으면 가져옴. 상위폴더까지 모두 생성
    CREATE_WITH_DELETE, // 폴더존재 상관없이 새로 만든다. 하위폴더까지 모두 삭제후 생성
  }
  
  public enum FileMode {
    EXISTED_ONLY, // 이미 존재하는 파일
    CREATE_IF_NEEDED, // 파일이 없으면 만들고 있으면 가져옴
    CREATE_WITH_DELETE, // 파일존재 상관없이 새로 만든다
  }
  
  /**
   * 디렉토리 생성
   * 
   * @param context
   * @param dirPath
   *          디렉터리 이후 부터의 패스를 받는다, FOLDER_OHTER 일때는 풀패스를 입력한다
   * @param type
   *          디렉토리 유형
   * @param mode
   *          디렉토리 반환 유형
   * @return FOLDER_OHTER의 경우 dirPath가 없을시 NULL
   */
  public static File getDir(Context context, String dirPath, FolderType type, FolderMode mode) {
    File rootDir = null;
    
    switch (type) {
      case FOLDER_INTERNAL_ROOT:
        // 폴더 경로 유효성 검사
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = new File(getInternalRootDirPath(context));
        } else {
          rootDir = new File(getInternalRootDirPath(context) + dirPath);
        }
        break;
      
      case FOLDER_INTERNAL_CACHE:
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = context.getCacheDir();
        } else {
          rootDir = new File(context.getCacheDir().getPath() + dirPath);
        }
        break;
      
      case FOLDER_INTERNAL_FILES:
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = context.getFilesDir();
        } else {
          rootDir = new File(context.getFilesDir().getPath() + dirPath);
        }
        break;
      
      case FOLDER_EXTERNAL_ROOT:
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = new File(getExternalRootDirPath(context));
        } else {
          rootDir = new File(getExternalRootDirPath(context) + dirPath);
        }
        break;
      
      case FOLDER_EXTERNAL_CACHE:
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = new File(new File(Environment.getExternalStorageDirectory(), Conf.EXTERNAL_FORDER_NAME), Conf.EXTERNAL_FORDER_CACHE_NAME);
        } else {
          File temp = new File(new File(Environment.getExternalStorageDirectory(), Conf.EXTERNAL_FORDER_NAME), Conf.EXTERNAL_FORDER_CACHE_NAME);
          rootDir = new File(temp.getPath() + dirPath);
        }
        break;
      
      case FOLDER_EXTERNAL_FILES:
        if (StringUtil.isTrimEmpty(dirPath)) {
          rootDir = new File(new File(Environment.getExternalStorageDirectory(), Conf.EXTERNAL_FORDER_NAME), Conf.EXTERNAL_FORDER_FILES_NAME);
        } else {
          File temp = rootDir = new File(new File(Environment.getExternalStorageDirectory(), Conf.EXTERNAL_FORDER_NAME), Conf.EXTERNAL_FORDER_FILES_NAME);
          rootDir = new File(temp.getPath() + dirPath);
        }
        
        break;
      
      case FOLDER_OHTER:
        if (StringUtil.isTrimEmpty(dirPath) == false) {
          rootDir = new File(dirPath);
        }
        break;
      
      default:
        rootDir = null;
        break;
    }
    
    switch (mode) {
      case EXISTED_ONLY:
        if (rootDir != null && !rootDir.exists()) {
          rootDir = null;
        }
        break;
      case CREATE_IF_NEEDED:
        if (rootDir != null && !rootDir.exists()) {
          if (!rootDir.mkdirs()) {
            rootDir = null;
          }
        }
        break;
      case CREATE_WITH_DELETE:
        break;
      
      default:
        rootDir = null;
        break;
    }
    
    return rootDir;
  }
  
  /**
   * 내부 저장소 루트 경로반환
   * 
   * @param context
   * @return
   */
  public static String getInternalRootDirPath(Context context) {
    String internalRootDirPath = "/data/data/" + context.getPackageName() + "/";
    return internalRootDirPath;
  }
  
  /**
   * 외부 저장소 루트 경로반환
   * 
   * @param context
   * @return
   */
  public static String getExternalRootDirPath(Context context) {
    String externalRootDirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Conf.EXTERNAL_FORDER_NAME + "/";
    return externalRootDirPath;
  }
  
  public static String getExternalDirPath(Context context, FolderType type) {
    String externalRootDirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Conf.EXTERNAL_FORDER_NAME + "/";
    String result = null;
    switch (type) {
      case FOLDER_INTERNAL_ROOT:
        result = getInternalRootDirPath(context);
        break;
      case FOLDER_INTERNAL_CACHE:
        result = context.getCacheDir().getAbsolutePath();
        break;
      case FOLDER_INTERNAL_FILES:
        result = context.getFilesDir().getAbsolutePath();
        break;
      case FOLDER_EXTERNAL_ROOT:
        result = externalRootDirPath;
        break;
      case FOLDER_EXTERNAL_CACHE:
        result = externalRootDirPath + Conf.EXTERNAL_FORDER_CACHE_NAME;
        break;
      case FOLDER_EXTERNAL_FILES:
        result = externalRootDirPath + Conf.EXTERNAL_FORDER_FILES_NAME;
        break;
      case FOLDER_OHTER:
        break;
    }
    return result;
  }
  
  /**
   * 파일을 반환한다
   * 
   * @param context
   * @param filePath
   *          파일 경로는 필수, 풀 패스가 아닌 /파일이름 형태로 적는다.
   * @param type
   *          폴더가 없는경우 생성할수 있도록 타입을 지정한다 폴더 모드 디폴트는 CREATE_IF_NEEDED
   * @param fileMode
   *          파일 반환 유형
   * @return
   */
  public static File getFile(Context context, String filePath, FolderType type, FileMode fileMode) {
    return getFile(context, filePath, type, FolderMode.CREATE_IF_NEEDED, fileMode);
  }
  
  /**
   * Crash 발생시 파일 생성
   * 
   * @param context
   * @param name
   *          파일 이름
   * @return
   */
  public static File getFileForCrashSave(Context context, String name) {
    return getFile(context, Conf.CRASH_FOLDER_NAME + name + ".txt", FolderType.FOLDER_EXTERNAL_FILES, FolderMode.CREATE_IF_NEEDED, FileMode.CREATE_IF_NEEDED);
  }
  
  /**
   * 저장된 Crash파일 가져오기
   * 
   * @param context
   * @param name
   *          파일 이름
   * @return
   */
  public static File getFileForCrashLoad(Context context, String name) {
    return getFile(context, Conf.CRASH_FOLDER_NAME + name + ".txt", FolderType.FOLDER_EXTERNAL_FILES, FolderMode.EXISTED_ONLY, FileMode.EXISTED_ONLY);
  }
  
  /**
   * 파일이 없으면 생성해서 반환하거나, 있으면 그냥 가져온다. 타입에 따른 분류
   * 
   * @param context
   * @param filePath
   *          파일 경로는 필수, 풀 패스가 아닌 /파일이름 형태로 적는다.
   * @param type
   *          폴더가 없는경우 생성할수 있도록 타입을 지정한다.
   * @param folderMode
   *          폴더 반환 유형
   * @param fileMode
   *          파일 반환 유형
   * @return
   */
  public static File getFile(Context context, String filePath, FolderType type, FolderMode folderMode, FileMode fileMode) {
    File nfile = null;
    File dir = null;
    String dirPath = "";
    
    if (StringUtil.isTrimEmpty(filePath)) return nfile;
    if (filePath.contains("/") && filePath.substring(0, filePath.lastIndexOf("/")).length() > 0) {
      dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
      dir = getDir(context, dirPath, type, folderMode);
    } else {
      dir = getDir(context, "", type, folderMode);
    }
    
    if (dir != null) {
      filePath = filePath.substring(filePath.lastIndexOf("/"));
      nfile = new File(dir.getPath() + filePath);
    }
    
    switch (fileMode) {
      case EXISTED_ONLY:
        if (!nfile.exists()) {
          nfile = null;
        }
        break;
      
      case CREATE_IF_NEEDED:
        if (!nfile.exists()) {
          try {
            nfile.createNewFile();
          } catch (IOException e) {
            nfile = null;
          }
        }
        break;
      
      case CREATE_WITH_DELETE:
        if (nfile.exists()) {
          nfile.delete();
        }
        
        try {
          nfile.createNewFile();
        } catch (IOException e) {
          nfile = null;
        }
        break;
      
      default:
        nfile = null;
        break;
    }
    
    return nfile;
  }
  
  /**
   * 해당디렉토리안에 위치한 파일을 삭제한다.
   * 
   * @param path
   * @return
   */
  public static boolean deleteFileByPath(String path) {
    File f = new File(path);
    if (f.exists()) {
      f.delete();
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * 해당 파일을 삭제한다.
   * 
   * @param f
   * @return
   */
  public static boolean deleteFile(File f) {
    boolean ret = false;
    if (f.exists()) {
      ret = f.delete();
    }
    return ret;
  }
  
  /**
   * 내부저장소 모든 디렉토리, 파일 삭제
   * 
   * @param context
   * @return
   */
  public static boolean deleteInternalRootdir(Context context) {
    File rootdir = getDir(context, "", FolderType.FOLDER_INTERNAL_ROOT, FolderMode.EXISTED_ONLY);
    if (rootdir != null) {
      return deleteAllFileWithFolder(context, rootdir);
    } else {
      return false;
    }
  }
  
  /**
   * 외부저장소 모든 파일 삭제
   * 
   * @param context
   * @return
   */
  public static boolean deleteExternalRootdir(Context context) {
    File rootdir = getDir(context, "", FolderType.FOLDER_EXTERNAL_ROOT, FolderMode.EXISTED_ONLY);
    if (rootdir != null) {
      return deleteAllFileWithFolder(context, rootdir);
    } else {
      return false;
    }
  }
  
  /**
   * 해당디렉토리의 모든파일과 해당디렉토리를 지운다.
   * 
   * @param context
   * @param targetFolder
   * @return
   */
  public static boolean deleteAllFileWithFolder(Context context, File targetFolder) {
    if (targetFolder.isDirectory()) {
      for (File child : targetFolder.listFiles()) {
        deleteAllFileWithFolder(context, child);
      }
    }
    
    boolean deleteFolder = targetFolder.delete();
    return deleteFolder;
  }
  
  /**
   * 해당디렉토리에 있는 파일들을 삭제한다
   * 
   * @param dir
   *          getInternalCacheDir(context), getInternalFilesDir(context) ...
   * @return 삭제 성공여부
   */
  public static boolean deleteFiles(File dir) {
    File filedir = dir;
    File[] files = filedir.listFiles();
    if (files != null) {
      for (File f : files) {
        f.delete();
      }
      
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * 인풋스트림을 파일로 저장한다. UI쓰레드에서 사용불가
   * 
   * @param input
   * @param file
   * @return
   */
  public static File streamToFile(InputStream input, File file) {
    
    OutputStream output;
    try {
      output = new FileOutputStream(file);
      
      byte data[] = new byte[1024];
      int count = 0;
      
      while ((count = input.read(data)) != -1) {
        output.write(data, 0, count);
      }
      output.flush();
      output.close();
      input.close();
      
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    return file;
  }
  
  public static byte[] convertFiletoByteArray(File file) {
    byte[] result = null;
    
    FileInputStream ios = null;
    ByteArrayOutputStream ous = null;
    try {
      byte[] buffer = new byte[4096];
      ous = new ByteArrayOutputStream();
      ios = new FileInputStream(file);
      int read = 0;
      while ((read = ios.read(buffer)) != -1) {
        ous.write(buffer, 0, read);
      }
      
      result = ous.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      
      result = null;
    } finally {
      try {
        if (ous != null) ous.close();
      } catch (IOException e) {
      }
      
      try {
        if (ios != null) ios.close();
      } catch (IOException e) {
      }
    }
    return result;
  }
  
  /**
   * 파일을 복사한다.
   * 
   * @param srcFile
   * @param destFile
   * @return false if fail
   */
  public static boolean copyFile(File srcFile, File destFile) {
    boolean result = false;
    try {
      InputStream in = new FileInputStream(srcFile);
      try {
        result = copyToFile(in, destFile);
      } finally {
        in.close();
      }
    } catch (IOException e) {
      result = false;
    }
    return result;
  }
  
  /**
   * 스트림으로부터 파일을 복사한다
   * 
   * @param inputStream
   * @param destFile
   * @return
   */
  public static boolean copyToFile(InputStream inputStream, File destFile) {
    try {
      if (destFile.exists()) {
        destFile.delete();
      }
      FileOutputStream out = new FileOutputStream(destFile);
      try {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) >= 0) {
          out.write(buffer, 0, bytesRead);
        }
      } finally {
        out.flush();
        try {
          out.getFD().sync();
        } catch (IOException e) {
        }
        out.close();
      }
      return true;
    } catch (IOException e) {
      return false;
    }
  }
  
  /**
   * String을 파일로 저장한다. UI쓰레드에서 사용불가 String데이터가 큰경우 문제가 발생할수있다.
   * 
   * @param input
   * @param file
   * @return
   */
  public static File stringToFile(String inputString, File file) {
    
    try {
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(inputString.getBytes());
      fos.close();
      
    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      return null;
    } catch (OutOfMemoryError e) {
      return null;
    }
    return file;
  }
  
  /**
   * 압풀 파일을 생성한다
   * 
   * @param path
   *          압축할 경로
   */
  public static void createZipFile(String path) {
    File dir = new File(path);
    String[] list = dir.list();
    String name = path.substring(path.lastIndexOf("/"), path.length());
    String _path;
    
    if (!dir.canRead() || !dir.canWrite()) return;
    
    int len = list.length;
    
    if (path.charAt(path.length() - 1) != '/') _path = path + "/";
    else _path = path;
    
    try {
      ZipOutputStream zip_out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(_path + name + ".zip"), 2048));
      
      for (int i = 0; i < len; i++)
        zip_folder(new File(_path + list[i]), zip_out);
      
      zip_out.close();
      
    } catch (FileNotFoundException e) {
      Log.e("File not found", e.getMessage());
      
    } catch (IOException e) {
      Log.e("IOException", e.getMessage());
    }
  }
  
  /**
   * ZipOutputStream를 넘겨 받아서 하나의 압축파일로 만든다.
   * 
   * @param file
   * @param zout
   * @throws IOException
   */
  private static void zip_folder(File file, ZipOutputStream zout) throws IOException {
    byte[] data = new byte[2048];
    int read;
    
    if (file.isFile()) {
      ZipEntry entry = new ZipEntry(file.getName());
      zout.putNextEntry(entry);
      BufferedInputStream instream = new BufferedInputStream(new FileInputStream(file));
      
      while ((read = instream.read(data, 0, 2048)) != -1)
        zout.write(data, 0, read);
      
      zout.closeEntry();
      instream.close();
      
    } else if (file.isDirectory()) {
      String[] list = file.list();
      if (list != null) {
        int len = list.length;
        for (int i = 0; i < len; i++) {
          zip_folder(new File(file.getPath() + "/" + list[i]), zout);
        }
      }
    }
  }
  
  /**
   * 압축을 해제 한다
   * 
   * @param zip_file
   * @param directory
   */
  public static boolean extractZipFiles(String zip_file, String directory) {
    boolean result = false;
    
    byte[] data = new byte[2048];
    ZipEntry entry = null;
    ZipInputStream zipstream = null;
    FileOutputStream out = null;
    
    if (!(directory.charAt(directory.length() - 1) == '/')) directory += "/";
    
    File destDir = new File(directory);
    boolean isDirExists = destDir.exists();
    boolean isDirMake = destDir.mkdirs();
    
    Log.d("FileManager", "extractZipFiles isDirMake/isDirExists = " + isDirMake + "/" + isDirExists + "/" + destDir.getAbsolutePath());
    
    try {
      zipstream = new ZipInputStream(new FileInputStream(zip_file));
      
      while ((entry = zipstream.getNextEntry()) != null) {
        int read = 0;
        File entryFile = new File(directory + entry.getName());
        
        if (!entryFile.exists()) {
          boolean isFileMake = entryFile.createNewFile();
          Log.d("FileManager", "extractZipFiles createNewFile = " + isFileMake + "/" + entryFile.getAbsolutePath());
        }
        
        out = new FileOutputStream(entryFile);
        while ((read = zipstream.read(data, 0, 2048)) != -1)
          out.write(data, 0, read);
        
        zipstream.closeEntry();
      }
      
      result = true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      result = false;
    } catch (IOException e) {
      e.printStackTrace();
      result = false;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      
      if (zipstream != null) {
        try {
          zipstream.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    
    return result;
  }
  
  /**
   * 압축파일을 원하는 위치에 풀어준다.
   * 
   * @param context
   * @param file
   * @return
   */
  // public static ArrayList<String> unZipFile(Context context, File file, String destPath){
  // ArrayList<String> file_names = new ArrayList<String>();
  // try {
  // ZipFile zipFile = new ZipFile(file);
  //
  // zipFile.extractAll(destPath);
  //
  // @SuppressWarnings("unchecked")
  // List<FileHeader> fileHeaders = zipFile.getFileHeaders();
  //
  // for(int i=0; i < fileHeaders.size(); i++){
  // file_names.add(fileHeaders.get(i).getFileName());
  // }
  //
  // } catch (ZipException e) {
  // e.printStackTrace();
  // return null;
  // }
  // return file_names;
  // }
  
  /**
   * 파일확장자를 가져온다.(소문자로 변환)
   * 
   * @param path
   * @return
   */
  public static String getFileExtention(String path) {
    if (path == null) {
      return null;
    }
    String fileExtension = path.toLowerCase().substring(path.lastIndexOf(".") + 1, path.length());
    
    return fileExtension;
  }
  
  /**
   * 
   * @param context
   * @param path
   * @return 0이면 지원하지않는 파일포멧, 1이면
   */
  public static boolean fileExtensionCheck(String filePath, String Extention) {
    if (!StringUtil.isEmpty(filePath)) {
      String fileExtension = filePath.toLowerCase().substring(filePath.lastIndexOf(".") + 1, filePath.length());
      
      if (StringUtil.equalData(fileExtension, Extention)) return true;
    }
    
    return false;
  }
  
  public static byte[] getFileBytes(File file) throws IOException {
    BufferedInputStream bis = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(file));
      int bytes = (int) file.length();
      byte[] buffer = new byte[bytes];
      int readBytes = bis.read(buffer);
      if (readBytes != buffer.length) {
        throw new IOException("Entire file not read");
      }
      return buffer;
    } finally {
      if (bis != null) {
        bis.close();
      }
    }
  }
}
