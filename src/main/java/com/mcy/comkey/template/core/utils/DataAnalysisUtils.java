package com.mcy.comkey.template.core.utils;

import com.mcy.comkey.template.core.statics.FilePaths;

import java.io.*;
import java.util.*;

/**
 * 辅助工具类
 * 编码转换：GBK转UTF-8
 *
 * @author 满朝阳 2018/11/3
 */
public class DataAnalysisUtils {

    private static String preFileName = "C:\\Users\\asus\\Desktop\\dianzishangwu\\";
    private static String tailFileName = ".TRAIN";

    /**
     * 原有ANSI编码转换成UTF8
     *
     * @param srcFileName
     * @param destFileName
     * @throws IOException
     */
    public static void transferFile(String srcFileName, String destFileName) throws IOException {

        String line_separator = System.getProperty("line.separator");
        FileInputStream srcFile = new FileInputStream(srcFileName);
        StringBuffer content = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(srcFile), "GBK"));
        String line = null;
        Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), "utf-8");

        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + line_separator);
        }
        bufferedReader.close();
        srcFile.close();
        ow.write(content.toString());
        ow.close();

        System.out.println("转换完毕");
    }





    public static String buildFileNameRandom() {
        return UUID.randomUUID().toString().substring(0, 8) + FilePaths.FILE_SUFFIX;
    }

    /**
     * （弃用）
     * 解析keyWord  先将所有包含keyWord的搜索记录提取出来 便于分析中介关键字
     * 时间复杂度为 O(n)
     *
     * @param keysFileName
     * @param srcFileName
     */
    public static void dataAnalysis(String keysFileName, String srcFileName) {
        String lineSeparator = System.getProperty("line.separator");
        try (
                BufferedReader keyWordsBuffer = new BufferedReader(new FileReader(keysFileName));
                BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFileName));
        ) {
            String keyWord = new String();
            Map<String, Writer> fileWriterMap = new HashMap<String, Writer>();
            List<String> keyWords = new ArrayList<String>();
            while ((keyWord = keyWordsBuffer.readLine()) != null) {
                keyWord = keyWord.trim();
                keyWords.add(keyWord);
                String fileName = preFileName + keyWord + tailFileName;
                fileWriterMap.put(keyWord, new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            }
            String line = null;
            if (keyWords == null) {
                throw new Exception("no keys");
            }
            while ((line = bufferedReader.readLine()) != null) {
                String[] searchCards = line.split("\t");
                for (String cord : searchCards) {
                    cord = cord.trim();
                    if (cord.length() < 1) {
                        continue;
                    }
                    for (String str : keyWords) {
                        if (cord.contains(str)) {
                            Writer writer = fileWriterMap.get(str);
                            writer.write(cord + lineSeparator);
                        }
                    }
                }
            }
            for (Map.Entry<String, Writer> entry : fileWriterMap.entrySet()) {
                Writer fileWriter = entry.getValue();
                fileWriter.close();
            }
            System.out.println("解析完成");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
//        transferFile("F:\\temp\\b.TRAIN","F:\\temp\\a.TRAIN");
//        dataAnalysis("C:\\Users\\asus\\Desktop\\dianzishangwu\\keyWord.txt", "F:\\temp\\b.TRAIN");
    }

}
