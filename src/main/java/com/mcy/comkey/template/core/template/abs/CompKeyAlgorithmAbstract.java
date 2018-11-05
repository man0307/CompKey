package com.mcy.comkey.template.core.template.abs;

import com.mcy.comkey.template.core.analysis.inter.CompetitiveKeywordAnalysis;
import com.mcy.comkey.template.core.analysis.inter.MediationKeywordAnalysis;
import com.mcy.comkey.template.core.analysis.inter.WordSegmenterMachine;
import com.mcy.comkey.template.core.model.CompetitiveKeywordNode;
import com.mcy.comkey.template.core.model.MediationKeywordAnalysisContext;
import com.mcy.comkey.template.core.model.MediationKeywordNode;
import com.mcy.comkey.template.core.model.TwoTuple;
import com.mcy.comkey.template.core.statics.FilePaths;
import com.mcy.comkey.template.core.utils.DataAnalysisUtils;
import org.apdplat.word.WordFrequencyStatistics;
import org.apdplat.word.segmentation.SegmentationAlgorithm;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mcy.comkey.template.core.statics.FilePaths.ALL_PATH_SUFFIX;
import static com.mcy.comkey.template.core.statics.FilePaths.TEMP_FILE;

/**
 * ComKey算法的核心骨架 （模板设计模式 +  策略模式）
 *
 * @author 满朝阳 2018/11/3
 */
public abstract class CompKeyAlgorithmAbstract {

    /**
     * 策略的接口 (三个接口)
     */
    private MediationKeywordAnalysis mediationKeywordAnalysis;

    private CompetitiveKeywordAnalysis competitiveKeywordAnalysis;

    private WordSegmenterMachine wordSegmenterMachine;

    /**
     * 中介关键词个数限制
     */
    private Integer intermediaryKeywordNumberLimit = 30;

    /**
     * 中介关键词权重限制
     */
    private double intermediaryKeywordWeightLimit = 0.00000001d;

    /**
     * 构造函数注入
     *
     * @param mediationKeywordAnalysis
     * @param competitiveKeywordAnalysis
     * @param wordSegmenterMachine
     */
    public CompKeyAlgorithmAbstract(MediationKeywordAnalysis mediationKeywordAnalysis,
                                    CompetitiveKeywordAnalysis competitiveKeywordAnalysis,
                                    WordSegmenterMachine wordSegmenterMachine) {
        this.competitiveKeywordAnalysis = competitiveKeywordAnalysis;
        this.mediationKeywordAnalysis = mediationKeywordAnalysis;
        this.wordSegmenterMachine = wordSegmenterMachine;
    }

    /**
     * 根据给定的 keyWord来求的排序后的竞争性关键字
     * 该模板的核心骨架
     * 梳理一下 整个数据解析的过程
     * 整个框架的目的是根据关键字通过所提供的搜索日志找出排序后的*竞争关键字
     * 1 首先传入一个关键字
     * 2 我们首先要对给出的搜索日志做编码的转换与数据的格式化(清洗数据)
     * 3 数据清洗之后 我们从格式化的搜索日志中提取出所有与该关键字相关的记录
     * 4 根据这个关键字相关的记录来进行分词并且频次分析提取中介关键字 （中介关键字可以通过进行数量或者频次的限制来进行降噪）
     * 5 根据每个中介关键字在整个格式化的搜索日志当中提取相关联的数据（操作方式同步骤4一样但是可以通过并发，异步等操作 利用多核的优势提高速度）
     * 并且进行分词、频次分析
     * 6 根据所的到的数据来对得到的竞争关键字进行计算和排序
     *
     * @param keyWord
     * @return
     */
    public List<CompetitiveKeywordNode> competitiveKeywordRecommend(String keyWord) {
        String srcFilePath = FilePaths.ALL_PATH_SUFFIX + FilePaths.ORIGINAL_FILE_NAME;
        String destFilePath = FilePaths.ALL_PATH_SUFFIX + FilePaths.CLEANED_FILE_NAME;
        String globalParticipleFilePath = FilePaths.ALL_PATH_SUFFIX + FilePaths.ALL_PARTICIPLE_DOCUMENT_NAME;
        String cleanedFilePath = cleanData(srcFilePath, destFilePath);

        //  全局大文件分词、词频统计
        Map<String, Integer> globalWordFrequencyMap = wordSegmenterMachine.wordFrequencyStatistics(cleanedFilePath, globalParticipleFilePath);

        //提取记录 生成中间文件
        TwoTuple<String, Integer> tuple = wordSegmenterMachine.associatedRecordExtract(keyWord, cleanedFilePath);
        String tempRecordsFilePath = tuple.getFirst();

        //生成中介关键词
        Map<String, Integer> intermediaryKeyword = wordSegmenterMachine.wordFrequencyStatistics(tempRecordsFilePath);
        MediationKeywordAnalysisContext context = new MediationKeywordAnalysisContext();
        context.setGlobalWordFrequencyMap(globalWordFrequencyMap);
        context.setWordFrequencyMap(intermediaryKeyword);
        context.setIntermediaryKeywordNumberLimit(this.getIntermediaryKeywordNumberLimit());
        context.setIntermediaryKeywordWeightLimit(this.getIntermediaryKeywordWeightLimit());
        context.setRecordCount(tuple.getSecond());
        context.setKeyWord(keyWord);
        List<MediationKeywordNode> mediationKeywordNodeList = mediationKeywordAnalysis
                .buildsMediationKeywordNodes(context);
        //得到未加工的所有竞争关键字
        List<CompetitiveKeywordNode> competitiveKeywordNodeList = mediationKeywordAnalysis.competitiveKeywordNode(mediationKeywordNodeList);
        //得到排序后的竞争关键字
        List<CompetitiveKeywordNode> sortedList = competitiveKeywordAnalysis.computeCompetitiveKeywordNode(competitiveKeywordNodeList);
        //将最终结果保存到文件当中
        if (dumpResult(sortedList)) {
            //todo logger
            System.out.println("保存最终结果成功");
        } else {
            //todo lgger
            System.out.println("保存最终结果失败");
        }
        return sortedList;
    }

    /**
     * 用于将原有的数据清洗为便于分词和词频统计的数据格式  因为常用的是搜索日志 所以建议统一清洗为每行一个搜索记录
     *
     * @param srcFileName
     * @param destFileName
     */
    protected abstract String cleanData(String srcFileName, String destFileName);

    private static boolean dumpResult(List<CompetitiveKeywordNode> sortedList) {
        try (
                Writer writer = new OutputStreamWriter(new FileOutputStream(FilePaths.ALL_PATH_SUFFIX + FilePaths.FINALLY_RESULT), "utf-8");
        ) {
            for (CompetitiveKeywordNode node : sortedList) {
                writer.write(node.toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setIntermediaryKeywordNumberLimit(Integer intermediaryKeywordNumberLimit) {
        this.intermediaryKeywordNumberLimit = intermediaryKeywordNumberLimit;
    }

    public MediationKeywordAnalysis getMediationKeywordAnalysis() {
        return mediationKeywordAnalysis;
    }

    public void setMediationKeywordAnalysis(MediationKeywordAnalysis mediationKeywordAnalysis) {
        this.mediationKeywordAnalysis = mediationKeywordAnalysis;
    }

    public Integer getIntermediaryKeywordNumberLimit() {
        return intermediaryKeywordNumberLimit;
    }

    public double getIntermediaryKeywordWeightLimit() {
        return intermediaryKeywordWeightLimit;
    }

    public void setIntermediaryKeywordWeightLimit(double intermediaryKeywordWeightLimit) {
        this.intermediaryKeywordWeightLimit = intermediaryKeywordWeightLimit;
    }

    public static void main(String[] args) {
//        Map<String, Integer> map = buildData("F:\\Project\\ComKey\\totleFrequent.TEMPORARY");
        File file = new File(FilePaths.ALL_PATH_SUFFIX + FilePaths.ALL_PARTICIPLE_DOCUMENT_NAME);
        file.getName();
    }
}
