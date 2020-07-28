package com.mb.utils;


import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;

public class ZipUtils {

    public static void unZipFiles(String zipPath,String descDir) throws Exception {
        unZipFiles(new File(zipPath),descDir);
    }

    /**
     * 解压文件到指定目录
     * @param zipFile 压缩文件
     * @param descDir 指定目录
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile,String descDir) throws IOException {
//		request.setCharacterEncoding("utf-8");
        File pathFile = new File(descDir);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }
        /**
         * ZipFile类用于从zip文件中读取条目
         * getEntries()返回ZIP文件条目中的枚举
         */
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry) entries.nextElement();
            entry.setUnixMode(644);//解决Linux乱码
            String zipEntryName = entry.getName();
            System.out.println(zipEntryName);
            InputStream in = zip.getInputStream(entry);
            BufferedInputStream bis = new BufferedInputStream(in);
            String outPath = (descDir+zipEntryName).replaceAll("\\*","/");
            //判断路径是否存在，不存在则创建文件路径
            File file = new File(outPath.substring(0,outPath.lastIndexOf("/")));
            if (!file.exists()){
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹，如果是上面已经上传，不需要解压
            if (new File(outPath).isDirectory()){
                continue;
            }
            //输出文件路径信息
//			System.out.println(outPath);
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0){
                out.write(buf1,0,len);
            }
            in.close();
            out.close();
        }
        System.out.println("*******************解压完毕********************");
    }

    public static void main(String[] args) {
        try {
            URL url=ZipUtils.class.getResource("/jvm/demo.zip");
            System.out.println(1);
            File file=new File(url.getPath());
            unZipFiles(file,"d:/test/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}