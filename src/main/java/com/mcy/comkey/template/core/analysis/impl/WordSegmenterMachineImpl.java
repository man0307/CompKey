package com.mcy.comkey.template.core.analysis.impl;

import com.mcy.comkey.template.core.analysis.inter.WordSegmenterMachine;
import com.mcy.comkey.template.core.model.TwoTuple;
import com.mcy.comkey.template.core.statics.FilePaths;
import com.mcy.comkey.template.core.utils.DataAnalysisUtils;
import org.apdplat.word.WordFrequencyStatistics;
import org.apdplat.word.segmentation.SegmentationAlgorithm;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.mcy.comkey.template.core.statics.FilePaths.ALL_PATH_SUFFIX;
import static com.mcy.comkey.template.core.statics.FilePaths.TEMP_FILE;

/**
 *  分词相关的算法
 *  @author 满朝阳 2018/11/5
 */
public class WordSegmenterMachineImpl implements WordSegmenterMachine {
    /**
     * 对指定的清洗数据之后的文件进行词频统计 返回生成的词频统计之后的Map
     * 自动生成分词文件的文件名
     *
     * @param srcFilePath
     * @return
     */
    @Override
    public Map<String, Integer> wordFrequencyStatistics(String srcFilePath) {
        Map<String, Integer> frequentMap = new HashMap<>();
        //生成随机文件名称
        String temporaryPath = DataAnalysisUtils.buildFileNameRandom();
        return wordFrequencyStatistics(srcFilePath, temporaryPath);
    }

    /**
     * wordFrequencyStatistics方法的重载方法 全局唯一的大文件就是清理后的初始文件 所以如果该文件已经分词
     * 那么将会将分词结果保留 只需要构建map即可 指定分词文件名称
     *
     * @param srcFilePath
     * @return
     */
    @Override
    public Map<String, Integer> wordFrequencyStatistics(String srcFilePath, String participleFilePath) {
        Map<String, Integer> mapResult = new HashMap<>();
        File file = new File(participleFilePath);
        boolean isExist = file.exists();
        if (isExist) {
            mapResult = buildData(participleFilePath);
        } else {
            WordFrequencyStatistics wordFrequencyStatistics = new WordFrequencyStatistics();
            wordFrequencyStatistics.setRemoveStopWord(true);
            wordFrequencyStatistics.setResultPath(participleFilePath);
            wordFrequencyStatistics.setSegmentationAlgorithm(SegmentationAlgorithm.MaxNgramScore);
            try {
                File tempResultFile = new File(ALL_PATH_SUFFIX + TEMP_FILE);
                File srcFile = new File(srcFilePath);
                wordFrequencyStatistics.seg(srcFile, tempResultFile);
                //输出词频统计结果
                wordFrequencyStatistics.dump();
                if (tempResultFile.exists() && tempResultFile.isFile()) {
                    tempResultFile.delete();
                }
//                if (!srcFilePath.equals(FilePaths.ALL_PATH_SUFFIX + FilePaths.ALL_PARTICIPLE_DOCUMENT_NAME) &&
//                        !srcFilePath.equals(FilePaths.ALL_PATH_SUFFIX + FilePaths.ORIGINAL_FILE_NAME)
//                        && !srcFilePath.equals(FilePaths.ALL_PATH_SUFFIX + FilePaths.CLEANED_FILE_NAME)) {
//                    if (srcFile.exists() && srcFile.isFile()) {
//                        srcFile.delete();
//                    }
//                }
            } catch (Exception e) {
                //todo logger
                System.out.println(e);
            }
            mapResult = buildData(participleFilePath);
        }
        return mapResult;
    }


    /**
     * 根据分词之后的文件构造Map
     *
     * @param filePath
     * @return
     */
    private static Map<String, Integer> buildData(String filePath) {
        Map<String, Integer> frequentMap = new HashMap<>();
        File file = new File(filePath);
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    String[] items = line.split(" ");
                    if (items.length == 2) {
                        try {
                            String keyword = items[0];
                            Integer count = Integer.parseInt(items[1].trim());
                            frequentMap.put(keyword, count);
                        } catch (Exception e) {
                            //todo logger 转化整数失败
                        }
                    }
                }
            }
            //正常生成Map之后要删除临时文件
System.out.println("尝试删除文件："+ file.getName());
            if (file.exists()) {
                file.delete();
System.out.println("删除文件："+ file.getName() + "成功");
            }
        } catch (FileNotFoundException e) {
            //todo logger
            e.printStackTrace();
        } catch (IOException e) {
System.out.println("删除文件："+ file.getName()+ "失败");
            //todo logger
            e.printStackTrace();
        }
        return frequentMap;
    }

    /**
     * 根据某个关键字从指定的格式化的搜索日志中提取记录行并存储在文件当中
     * 存储在文件当中方便用word分词器来进行分词 如若不然 文件过大的时候会导致字符长度超长分词不方便
     *
     * @param keyWord
     * @param srcFilePath
     * @return
     */
    @Override
    public TwoTuple<String, Integer> associatedRecordExtract(String keyWord, String srcFilePath) {
        TwoTuple<String, Integer> resultTuple = new TwoTuple<String, Integer>();
        String lineSeparator = System.getProperty("line.separator");
        String tempFileName = DataAnalysisUtils.buildFileNameRandom();
        int count = 0;
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFilePath));
                Writer writer = new OutputStreamWriter(new FileOutputStream(FilePaths.ALL_PATH_SUFFIX + tempFileName), "utf-8");
        ) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && line.contains(keyWord)) {
                    writer.write(line + lineSeparator);
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            //todo logger
            e.printStackTrace();
        } catch (IOException e) {
            //todo logger
            e.printStackTrace();
        }
        resultTuple.setFirst(tempFileName);
        resultTuple.setSecond(count);
        return resultTuple;
    }
}
