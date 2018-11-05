package com.mcy.comkey.template.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 中介关键字节点
 * @author 满朝阳 2018/11/3
 */
public  class MediationKeywordNode implements Comparable{

    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 中介关键字
     */
    private String word;
    /**
     竞争性关键字的权重
     */
    private Double weight;

    /**
     * 在关键字记录中的该中介关键字出现的次数
     */
    private Integer saRecordCount;
    /**
     * 与各个竞争性关键字的测度
     */
    Map<String,Double> compMap = new HashMap<String,Double>();


    public Integer getSaRecordCount() {
        return saRecordCount;
    }

    public void setSaRecordCount(Integer saRecordCount) {
        this.saRecordCount = saRecordCount;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Map<String, Double> getCompMap() {
        return compMap;
    }

    public void setCompMap(Map<String, Double> compMap) {
        this.compMap = compMap;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof MediationKeywordNode)){ return false;}

        MediationKeywordNode that = (MediationKeywordNode) o;

        if (getWord() != null ? !getWord().equals(that.getWord()) : that.getWord() != null) { return false;}
        if (getWeight() != null ? !getWeight().equals(that.getWeight()) : that.getWeight() != null) {return false;}
        return getCompMap() != null ? getCompMap().equals(that.getCompMap()) : that.getCompMap() == null;
    }

    @Override
    public int hashCode() {
        int result = getWord() != null ? getWord().hashCode() : 0;
        result = 31 * result + (getWeight() != null ? getWeight().hashCode() : 0);
        result = 31 * result + (getCompMap() != null ? getCompMap().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if(o == this){
            return 0;
        }else if(o!=null && o instanceof MediationKeywordNode){
            MediationKeywordNode node = (MediationKeywordNode)o;
            if(Double.doubleToLongBits(node.getWeight())==Double.doubleToLongBits(this.getWeight())){
                return 0;
            }else if(Double.doubleToLongBits(node.getWeight())>Double.doubleToLongBits(this.getWeight())){
                return 1;
            }else {
                return -1;
            }
        }
        return -1;
    }
}