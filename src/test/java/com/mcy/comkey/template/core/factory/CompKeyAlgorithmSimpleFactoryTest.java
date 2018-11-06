package com.mcy.comkey.template.core.factory;


import com.mcy.comkey.template.core.statics.FilePaths;
import com.mcy.comkey.template.core.template.abs.CompKeyAlgorithmAbstract;
import com.mcy.comkey.template.core.utils.DataCleanUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class CompKeyAlgorithmSimpleFactoryTest {
    private CompKeyAlgorithmSimpleFactory factory = new CompKeyAlgorithmSimpleFactory();

    private static int M = 1024 * 1024;


    /**
     * 测量对于不同数据集的算法运行时间性能
     */
    @Test
    public void performanceTest() {
        int count = 10;
        String keyWord = "蓝月亮";
        String srcFilePath = FilePaths.ALL_PATH_SUFFIX + FilePaths.ORIGINAL_FILE_NAME;
        File file = new File(srcFilePath);
        CompKeyAlgorithmAbstract compKeyAlgorithm = factory.buildCompKeyAlgorithm();
        long begTime1 = System.currentTimeMillis();
        compKeyAlgorithm.competitiveKeywordRecommend(keyWord);
        long endTime1 = System.currentTimeMillis();
        long castTime = endTime1 - begTime1;

        //将总文件平均分成n分 分别求每一分的文件生成耗时
        List<String> fileNames = DataCleanUtils.documentCutting(srcFilePath, count, false);

        long totalTime = 0L;
        for (String name : fileNames) {
            String partialFilePath = FilePaths.ALL_PATH_SUFFIX + name;
            compKeyAlgorithm.setSrcFilePath(partialFilePath);
            File file1 = new File(partialFilePath);
            long begTime = System.currentTimeMillis();
            compKeyAlgorithm.competitiveKeywordRecommend(keyWord);
            long endTime = System.currentTimeMillis();
            System.out.println("大小为" + (file1.length() * 1.0) / M + "m（原始文件的十分之一）的" + name + "文件生成" + keyWord + "竞争性关键词用了：" + (endTime - begTime)*1.0/1000 + "秒");
            totalTime += (endTime - begTime);
        }
        System.out.println("大小为" + (file.length() * 1.0) / M + "m的原始文件生成" + keyWord + "竞争性关键词用了：" + castTime*1.0/1000 + "秒");
        System.out.println(count + "个子文件的平均耗时为 ：" + (totalTime / count) + "毫秒");
        System.out.println("总文件的耗时是其十分之一大小的子文件的" + (castTime * 1.0 / (totalTime / count)) + "倍数");

        //清理中间生成的杂项文件
        DataCleanUtils.cleanIntermediateFile();
    }


}
