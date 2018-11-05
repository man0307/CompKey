package com.mcy.comkey.template.core.statics;

/**
 * 文件路径相关的常量
 * @author 满朝阳 2018/11/4
 */
public class FilePaths {
    /**
     * 原始文件路径
     */
    public static final String ORIGINAL_FILE_NAME = "srcFile.TRAIN";
    /**
     * 清洗数据后的文件路径
     */
    public static final String CLEANED_FILE_NAME = "cleaned.TRAIN";
    /**
     * 临时文件名称后缀
     */
    public static final String  FILE_SUFFIX = ".TEMPORARY";
    /**
     * 暂存分词结果前缀
     */
    public static final String  TEMP_FILE = "暂存分词结果.txt";
    /**
     * 当前工程目录
     */
    public static final String ALL_PATH_SUFFIX = System.getProperty("user.dir") + "\\";;
    /**
     * 总文件的分词结果 默认不删除
     */
    public static final String ALL_PARTICIPLE_DOCUMENT_NAME = "allParticipleDocumentName.txt";

    public static final String FINALLY_RESULT = "finallyResult.txt";

}
