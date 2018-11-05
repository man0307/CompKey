package com.mcy.comkey.template.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 竞争关键字节点
 *
 * @author 满朝阳 2018/11/3
 */
public class CompetitiveKeywordNode implements Comparable {
    /**
     * 竞争关键字
     */
    String word;
    /**
     * k对于s的竞争程度
     */
    Double compK;
    /**
     * 与该竞争关键字相关联的中介关键字
     */
    List<MediationKeywordNode> mediationKeywordNodeLists = new ArrayList<>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getCompK() {
        return compK;
    }

    public void setCompK(Double compK) {
        this.compK = compK;
    }

    public List<MediationKeywordNode> getMediationKeywordNodeLists() {
        return mediationKeywordNodeLists;
    }

    public void setMediationKeywordNodeLists(List<MediationKeywordNode> mediationKeywordNodeLists) {
        this.mediationKeywordNodeLists = mediationKeywordNodeLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitiveKeywordNode)) {
            return false;
        }

        CompetitiveKeywordNode that = (CompetitiveKeywordNode) o;

        if (getWord() != null ? !getWord().equals(that.getWord()) : that.getWord() != null) {
            return false;
        }
        if (getCompK() != null ? !getCompK().equals(that.getCompK()) : that.getCompK() != null) {
            return false;
        }
        return getMediationKeywordNodeLists() != null ? getMediationKeywordNodeLists().equals(that.getMediationKeywordNodeLists()) : that.getMediationKeywordNodeLists() == null;
    }

    @Override
    public int hashCode() {
        int result = getWord() != null ? getWord().hashCode() : 0;
        result = 31 * result + (getCompK() != null ? getCompK().hashCode() : 0);
        result = 31 * result + (getMediationKeywordNodeLists() != null ? getMediationKeywordNodeLists().hashCode() : 0);
        return result;
    }

    /**
     * 覆写compareTo方法 用于排序 降序
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        } else if (o != null && o instanceof CompetitiveKeywordNode) {
            CompetitiveKeywordNode node = (CompetitiveKeywordNode) o;
            if(Double.doubleToLongBits(node.getCompK())==Double.doubleToLongBits(this.getCompK())){
                return 0;
            }else if(Double.doubleToLongBits(node.getCompK())>Double.doubleToLongBits(this.getCompK())){
                return 1;
            }else {
                return -1;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "CompetitiveKeywordNode{" +
                "竞争关键词为 =" + word + '\'' +
                ", 竞争性测度 =" + compK + "\n" ;

    }
}
