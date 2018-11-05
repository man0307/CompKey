package com.mcy.comkey.template.core.model;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class CompetitiveKeywordNodeBuilderContext {
    /**
     *  中介关键词节点
     */
    private MediationKeywordNode mediationKeywordNode;

    private String filePath;

    /**
     * 在关键字记录中的中介关键字的记录数
     */
    private Integer saRecordCount;


    public Integer getSaRecordCount() {
        return saRecordCount;
    }

    public void setSaRecordCount(Integer saRecordCount) {
        this.saRecordCount = saRecordCount;
    }

    LinkedBlockingQueue<CompetitiveKeywordNode> queue;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LinkedBlockingQueue<CompetitiveKeywordNode> getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue<CompetitiveKeywordNode> queue) {
        this.queue = queue;
    }

    public MediationKeywordNode getMediationKeywordNode() {
        return mediationKeywordNode;
    }

    public void setMediationKeywordNode(MediationKeywordNode mediationKeywordNode) {
        this.mediationKeywordNode = mediationKeywordNode;
    }

    @Override
    public String toString() {
        return "CompetitiveKeywordNodeBuilderContext{" +
                "mediationKeywordNode=" + mediationKeywordNode +
                ", filePath='" + filePath + '\'' +
                ", saRecordCount=" + saRecordCount +
                ", queue=" + queue +
                '}';
    }
}
