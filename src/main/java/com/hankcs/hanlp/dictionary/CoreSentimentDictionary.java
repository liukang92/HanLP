/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/13 13:12</create-date>
 *
 * <copyright file="CoreSynonymDictionary.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.dictionary;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.algoritm.EditDistance;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.common.CommonSentimentDictionary;
import com.hankcs.hanlp.dictionary.common.CommonSynonymDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.List;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * 核心同义词词典
 *
 * @author hankcs
 */
public class CoreSentimentDictionary {
	static CommonSentimentDictionary dictionary;

	static {
		try {
			long start = System.currentTimeMillis();
			dictionary = CommonSentimentDictionary.create(IOUtil.getInputStream(HanLP.Config.CoreSentimentDictionaryPath));
			logger.info("载入核心情感词词典成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			System.err.println("载入核心情感词词典失败" + e);
			System.exit(-1);
		}
	}

	public static int get(String key) {
		return dictionary.get(key);
	}
}
