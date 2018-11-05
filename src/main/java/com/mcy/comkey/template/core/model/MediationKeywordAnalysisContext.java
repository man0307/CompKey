package com.mcy.comkey.template.core.model;

import java.util.Map;


/**
 * @author 满朝阳 2018/11/5
 */
public class MediationKeywordAnalysisContext {
     private Map<String, Integer> globalWordFrequencyMap;
     private Map<String, Integer> wordFrequencyMap;
     private String keyWord;
     private Integer recordCount;
     private Integer intermediaryKeywordNumberLimit;
     private double intermediaryKeywordWeightLimit;

    public Map<String, Integer> getGlobalWordFrequencyMap() {
        return globalWordFrequencyMap;
    }

    public void setGlobalWordFrequencyMap(Map<String, Integer> globalWordFrequencyMap) {
        this.globalWordFrequencyMap = globalWordFrequencyMap;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

    public void setWordFrequencyMap(Map<String, Integer> wordFrequencyMap) {
        this.wordFrequencyMap = wordFrequencyMap;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getIntermediaryKeywordNumberLimit() {
        return intermediaryKeywordNumberLimit;
    }

    public void setIntermediaryKeywordNumberLimit(Integer intermediaryKeywordNumberLimit) {
        this.intermediaryKeywordNumberLimit = intermediaryKeywordNumberLimit;
    }

    public double getIntermediaryKeywordWeightLimit() {
        return intermediaryKeywordWeightLimit;
    }

    public void setIntermediaryKeywordWeightLimit(double intermediaryKeywordWeightLimit) {
        this.intermediaryKeywordWeightLimit = intermediaryKeywordWeightLimit;
    }
}
