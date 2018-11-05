package com.mcy.comkey.template.core.analysis.inter;

import com.mcy.comkey.template.core.model.CompetitiveKeywordNode;
import com.mcy.comkey.template.core.model.MediationKeywordNode;

import java.util.List;

/**
 * @author 满朝阳 2018/11/4
 */
public interface CompetitiveKeywordAnalysis {

    /**
     * 通过给出的竞争关键词原始列表 计算出所有的竞争关键词对于关键字的竞争性程度
     * @param competitiveKeywordNodeList
     * @return
     */
     List<CompetitiveKeywordNode> computeCompetitiveKeywordNode(List<CompetitiveKeywordNode> competitiveKeywordNodeList);
}
