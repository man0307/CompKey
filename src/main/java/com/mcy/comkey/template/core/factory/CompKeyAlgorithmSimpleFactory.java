package com.mcy.comkey.template.core.factory;

import com.mcy.comkey.template.core.analysis.impl.CompetitiveKeywordAnalysisSimple;
import com.mcy.comkey.template.core.analysis.impl.MediationKeywordAnalysisSimple;
import com.mcy.comkey.template.core.analysis.impl.WordSegmenterMachineImpl;
import com.mcy.comkey.template.core.template.CompKeyAlgorithmSimple;
import com.mcy.comkey.template.core.template.abs.CompKeyAlgorithmAbstract;

/**
 * @author 满朝阳 2018/11/5
 */
public class CompKeyAlgorithmSimpleFactory implements CompKeyAlgorithmFactory {

    private static class CompKeyAlgorithmSimpleHandler{
        private static CompKeyAlgorithmSimple INSTANCE = new CompKeyAlgorithmSimple(new MediationKeywordAnalysisSimple(new WordSegmenterMachineImpl())
                ,new CompetitiveKeywordAnalysisSimple(),new WordSegmenterMachineImpl());;
    }

    @Override
    public CompKeyAlgorithmAbstract buildCompKeyAlgorithm() {
        return CompKeyAlgorithmSimpleHandler.INSTANCE;
    }

    public static void main(String[] args){
        CompKeyAlgorithmSimpleFactory compKeyAlgorithmSimpleFactory = new CompKeyAlgorithmSimpleFactory();
        CompKeyAlgorithmAbstract compKeyAlgorithmAbstract = compKeyAlgorithmSimpleFactory.buildCompKeyAlgorithm();
        compKeyAlgorithmAbstract.competitiveKeywordRecommend("蓝月亮");
    }
}
