package com.mcy.comkey.template.core.analysis.inter;

import com.mcy.comkey.template.core.model.CompetitiveKeywordNode;
import com.mcy.comkey.template.core.model.MediationKeywordAnalysisContext;
import com.mcy.comkey.template.core.model.MediationKeywordNode;

import java.util.List;
import java.util.Map;

/**
 * @author 满朝阳 2018/11/4
 */
public interface MediationKeywordAnalysis {

    /**
     * 根据词频来获得排序后的中介关键字
     * @param mediationKeywordAnalysisContext
     * @return
     */
    public List<MediationKeywordNode> buildsMediationKeywordNodes(MediationKeywordAnalysisContext mediationKeywordAnalysisContext);

    /**
     * 根据给出的中介关键字列表来解析出所有的竞争关键字
     *
     * @param mediationKeywordNodeList
     * @return
     */
    public List<CompetitiveKeywordNode> competitiveKeywordNode(List<MediationKeywordNode> mediationKeywordNodeList);
}
