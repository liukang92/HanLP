/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/20 12:38</create-date>
 *
 * <copyright file="CoNLLWord.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.dependency.CoNll;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hankcs
 */
public class CoNLLWord
{
    /**
     * ID	当前词在句子中的序号，１开始.
     */
    public int ID;
    /**
     * 当前词语（或标点）的原型或词干，在中文中，此列与FORM相同
     */
    public String LEMMA;
    /**
     * 当前词语的词性（粗粒度）
     */
    public String CPOSTAG;
    /**
     * 当前词语的词性（细粒度）
     */
    public String POSTAG;
    /**
     * 当前词语的中心词
     */
    public CoNLLWord HEAD;
    /**
     * 当前词语与中心词的依存关系
     */
    public String DEPREL;
    /**
     * 当前词语的依赖词
     */
    public List<CoNLLWord> FOLLOW = new LinkedList<>();
    /**
     * 等效字符串
     */
    public String NAME;

    /**
     * 根节点
     */
    public static final CoNLLWord ROOT = new CoNLLWord(0, "##核心##", "ROOT", "root");
    /**
     * 空白节点，用于描述下标超出word数组的词语
     */
    public static final CoNLLWord NULL = new CoNLLWord(-1, "##空白##", "NULL", "null");

    /**
     *
     * @param ID 当前词在句子中的序号，１开始.
     * @param LEMMA 当前词语（或标点）的原型或词干，在中文中，此列与FORM相同
     * @param POSTAG 当前词语的词性（细粒度）
     */
    public CoNLLWord(int ID, String LEMMA, String POSTAG)
    {
        this.ID = ID;
        this.LEMMA = LEMMA;
        this.CPOSTAG = POSTAG.substring(0, 1);   // 取首字母作为粗粒度词性
        this.POSTAG = POSTAG;
        compile();
    }

    /**
     *
     * @param ID 当前词在句子中的序号，１开始.
     * @param LEMMA 当前词语（或标点）的原型或词干，在中文中，此列与FORM相同
     * @param CPOSTAG 当前词语的词性（粗粒度）
     * @param POSTAG 当前词语的词性（细粒度）
     */
    public CoNLLWord(int ID, String LEMMA, String CPOSTAG, String POSTAG)
    {
        this.ID = ID;
        this.LEMMA = LEMMA;
        this.CPOSTAG = CPOSTAG;
        this.POSTAG = POSTAG;
        compile();
    }

    public CoNLLWord(CoNllLine line)
    {
        LEMMA = line.value[2];
        CPOSTAG = line.value[3];
        POSTAG = line.value[4];
        DEPREL = line.value[7];
        ID = line.id;
        compile();
    }

    public CoNLLWord(CoNllLine[] lineArray, int index)
    {
        this(lineArray[index]);
    }

    private void compile()
    {
        this.NAME = PosTagCompiler.compile(POSTAG, LEMMA);
    }

    public String getRootString(){
        CoNLLWord cur = this;
        StringBuilder sb = new StringBuilder(cur.LEMMA).append(" [").append(cur.DEPREL);
        while ((cur = cur.HEAD) != null) {
            sb.append("] ").append(cur.LEMMA).append(" [").append(cur.DEPREL);
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return String.valueOf(ID) + '\t' + LEMMA + '\t' + LEMMA + '\t' + CPOSTAG + '\t' +
                POSTAG + '\t' + '_' + '\t' + HEAD.ID + '\t' + DEPREL + '\t' +
                '_' + '\t' + '_';
    }
}
