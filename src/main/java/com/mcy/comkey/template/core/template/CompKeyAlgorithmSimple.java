package com.mcy.comkey.template.core.template;

import com.mcy.comkey.template.core.analysis.inter.CompetitiveKeywordAnalysis;
import com.mcy.comkey.template.core.analysis.inter.MediationKeywordAnalysis;
import com.mcy.comkey.template.core.analysis.inter.WordSegmenterMachine;
import com.mcy.comkey.template.core.template.abs.CompKeyAlgorithmAbstract;

import java.io.*;

/**
 * @author 满朝阳 2018/11/3
 */
public class CompKeyAlgorithmSimple extends CompKeyAlgorithmAbstract {


    /**
     * 构造函数注入
     *
     * @param mediationKeywordAnalysis
     * @param competitiveKeywordAnalysis
     * @param wordSegmenterMachine
     */
    public CompKeyAlgorithmSimple(MediationKeywordAnalysis mediationKeywordAnalysis, CompetitiveKeywordAnalysis competitiveKeywordAnalysis, WordSegmenterMachine wordSegmenterMachine) {
        super(mediationKeywordAnalysis, competitiveKeywordAnalysis, wordSegmenterMachine);
    }

    @Override
    protected String cleanData(String srcFileName, String destFileName) {
        File file = new File(destFileName);
        if(file.exists() && file.isFile()){
            return destFileName;
        }
        String lineSeparator = System.getProperty("line.separator");
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFileName));
                Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), "utf-8");
        ) {
            String line = null;
            StringBuffer content = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.substring(39);
                String[] researchCords = line.split("\t");
                for(String temp : researchCords){
                    if(temp.length()>0){
                        content.append(temp + lineSeparator);
                    }
                }
            }
            ow.write(content.toString());
            System.out.println("清洗完毕。");
        } catch (Exception e) {
             //todo logger
            e.printStackTrace();
        }
        return destFileName;
    }
}
