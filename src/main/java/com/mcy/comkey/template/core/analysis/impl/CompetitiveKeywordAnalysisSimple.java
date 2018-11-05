package com.mcy.comkey.template.core.analysis.impl;

import com.mcy.comkey.template.core.analysis.inter.CompetitiveKeywordAnalysis;
import com.mcy.comkey.template.core.model.CompetitiveKeywordNode;
import com.mcy.comkey.template.core.model.MediationKeywordNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CompetitiveKeywordAnalysisSimple implements CompetitiveKeywordAnalysis  {
    @Override
    public List<CompetitiveKeywordNode> computeCompetitiveKeywordNode(List<CompetitiveKeywordNode> competitiveKeywordNodeList) {
        for(CompetitiveKeywordNode node : competitiveKeywordNodeList){
            if(CollectionUtils.isEmpty(node.getMediationKeywordNodeLists())){
                continue;
            }
            //聚合 计算竞争度
           String word = node.getWord();
           double compK = 0.0d;
           for(MediationKeywordNode node1 : node.getMediationKeywordNodeLists()){
               compK += node1.getWeight()*node1.getCompMap().get(word);
           }
           node.setCompK(compK);
        }
        Collections.sort(competitiveKeywordNodeList);
        return competitiveKeywordNodeList;
    }
}
