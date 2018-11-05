package com.mcy.comkey.template.core.analysis.inter;

import com.mcy.comkey.template.core.model.TwoTuple;

import java.util.Map;

/**
 * 策略模式  可扩展多个分词器来供用户选择
 */
public interface WordSegmenterMachine {
    /**
     * 对指定的清洗数据之后的文件进行词频统计 返回生成的词频统计之后的Map
     * 自动生成分词文件的文件名
     *
     * @param srcFilePath
     * @return
     */
     Map<String, Integer> wordFrequencyStatistics(String srcFilePath);

    /**
     * wordFrequencyStatistics方法的重载方法 全局唯一的大文件就是清理后的初始文件 所以如果该文件已经分词
     * 那么将会将分词结果保留 只需要构建map即可 指定分词文件名称
     *
     * @param srcFilePath
     * @return
     */
      Map<String, Integer> wordFrequencyStatistics(String srcFilePath, String participleFilePath);

    /**
     * 根据某个关键字从指定的格式化的搜索日志中提取记录行并存储在文件当中
     * 存储在文件当中方便用word分词器来进行分词 如若不然 文件过大的时候会导致字符长度超长分词不方便
     * @param keyWord
     * @param srcFilePath
     * @return
     */
    TwoTuple<String,Integer> associatedRecordExtract(String keyWord, String srcFilePath);
}
