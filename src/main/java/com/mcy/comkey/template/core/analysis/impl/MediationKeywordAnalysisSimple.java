package com.mcy.comkey.template.core.analysis.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mcy.comkey.template.core.analysis.inter.MediationKeywordAnalysis;
import com.mcy.comkey.template.core.analysis.inter.WordSegmenterMachine;
import com.mcy.comkey.template.core.model.*;
import com.mcy.comkey.template.core.statics.FilePaths;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author 满朝阳 2018/11/3
 */
public class MediationKeywordAnalysisSimple implements MediationKeywordAnalysis {

    private WordSegmenterMachine wordSegmenterMachine;

    private CountDownLatch countDownLatch;

    public MediationKeywordAnalysisSimple(WordSegmenterMachine wordSegmenterMachine) {
        this.wordSegmenterMachine = wordSegmenterMachine;
    }

    private static ThreadFactory eventCustomerThreadFactory = new ThreadFactoryBuilder().setNameFormat("MediationKeywordAnalysisSimple-pool-%d").build();
    private static ExecutorService executorService = new ThreadPoolExecutor(10, 120,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000), eventCustomerThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Override
    public List<MediationKeywordNode> buildsMediationKeywordNodes(MediationKeywordAnalysisContext mediationKeywordAnalysisContext) {
        List<MediationKeywordNode> resultList = new ArrayList<>();
        Integer recordCount = mediationKeywordAnalysisContext.getRecordCount();
        String keyWord = mediationKeywordAnalysisContext.getKeyWord();
        for (Map.Entry<String, Integer> entry : mediationKeywordAnalysisContext.getWordFrequencyMap().entrySet()) {
            MediationKeywordNode node = new MediationKeywordNode();
            String word = entry.getKey();
            Integer wordCount = entry.getValue();
            Double weight = (wordCount * 1.0) / recordCount;
            node.setWeight(weight);
            node.setSaRecordCount(wordCount);
            node.setWord(word);
            node.setKeyWord(keyWord);
            resultList.add(node);
        }
        Collections.sort(resultList);
        List<MediationKeywordNode> resultListFinally = new ArrayList<>();

        //降噪处理
        for (int i = 1; i < resultList.size(); i++) {
            MediationKeywordNode node = resultList.get(i);
            if(node.getWord().contains(keyWord)){
                continue;
            }
            if (i >= mediationKeywordAnalysisContext.getIntermediaryKeywordNumberLimit()
                    || node.getWeight() < mediationKeywordAnalysisContext.getIntermediaryKeywordWeightLimit()) {
                break;
            }
            resultListFinally.add(node);
        }
        return resultListFinally;
    }

    @Override
    public List<CompetitiveKeywordNode> competitiveKeywordNode(List<MediationKeywordNode> mediationKeywordNodeList) {
        List<CompetitiveKeywordNode> list = new ArrayList<>();
        //并发写入关键字 需要使用阻塞队列
        LinkedBlockingQueue<CompetitiveKeywordNode> queue = new LinkedBlockingQueue<>();
        String filePath = FilePaths.ALL_PATH_SUFFIX + FilePaths.CLEANED_FILE_NAME;
        //多线程构造原始的CompetitiveKeywordNode列表
        countDownLatch = new CountDownLatch(mediationKeywordNodeList.size());


        for (MediationKeywordNode node : mediationKeywordNodeList) {
            CompetitiveKeywordNodeBuilderContext context = new CompetitiveKeywordNodeBuilderContext();
            context.setFilePath(filePath);
            context.setMediationKeywordNode(node);
            context.setQueue(queue);
            executorService.execute(new CompetitiveKeywordNodeBuilderThread(context));
        }
        try {
            //等待15分钟
            countDownLatch.await(15 * 60, TimeUnit.SECONDS);
            //强制关停所有线程
//            executorService.shutdownNow();
            for(CompetitiveKeywordNode node : queue){
                list.add(node);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 利用多线程来构建竞争性关键字的原始列表
     */
    private class CompetitiveKeywordNodeBuilderThread implements Runnable {

        /**
         * 只读 多线程安全
         */
        private CompetitiveKeywordNodeBuilderContext context;

        public CompetitiveKeywordNodeBuilderThread(CompetitiveKeywordNodeBuilderContext context) {
            this.context = context;
        }

        @Override
        public void run() {

            TwoTuple<String, Integer> tuple = wordSegmenterMachine
                    .associatedRecordExtract(context.getMediationKeywordNode().getWord(), context.getFilePath());
            String tempRecordsFilePath = tuple.getFirst();
            String keyWord = context.getMediationKeywordNode().getKeyWord();
            Integer aCount = tuple.getSecond();
            int sa = context.getMediationKeywordNode().getSaRecordCount();
            //统计竞争关键字的频次
            MediationKeywordNode mediationKeywordNode = context.getMediationKeywordNode();
            Map<String, Integer> intermediaryKeyword = wordSegmenterMachine.wordFrequencyStatistics(tempRecordsFilePath);

            LinkedBlockingQueue<CompetitiveKeywordNode> queue = context.getQueue();
            for (Map.Entry<String, Integer> entry : intermediaryKeyword.entrySet()) {
                try{
                    String word = entry.getKey();
                    if(word.contains(keyWord)){
                        continue;
                    }
                    Integer count = entry.getValue();
                    if (word.equals(mediationKeywordNode.getWord())) {
                        continue;
                    }
                    CompetitiveKeywordNode node = new CompetitiveKeywordNode();
                    List<MediationKeywordNode> list = node.getMediationKeywordNodeLists();
                    list.add(mediationKeywordNode);
                    node.setWord(word);
                    Map<String, Double> comMap = mediationKeywordNode.getCompMap();
                    //计算单个的竞争程度 k是竞争关键字 a是中介关键字 s是关键字
                    //公式 Comp(k,s) = |{ka}| / (|{a]|-|{sa}|)
                    Double compK = null;
                    int ka = count;
                    int a = aCount;
                    compK = (ka * 1.0) / (a - sa);
                    comMap.put(word, compK);
                    queue.add(node);
                }catch (Exception e){
                    //todo logger
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();

        }
    }
}
