package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.domain.DomainExceptionDictionary;
import com.hankcs.hanlp.dictionary.domain.DomainFeatureDictionary;
import com.hankcs.hanlp.sentiment.common.PhraseSentence;
import com.hankcs.hanlp.sentiment.common.SentimentUtil;
import com.hankcs.hanlp.sentiment.common.Tuple;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.CORE;
import static com.hankcs.hanlp.sentiment.common.SentimentUtil.SPLIT;
import static com.hankcs.hanlp.sentiment.common.SentimentUtil.negPolarity;

/**
 * 基于词典的tuple提取器
 *
 * @author liukang
 * @since 2016/3/28
 */
public abstract class DictionaryBasedTupleExtractor implements ITupleExtractor {
	/**
	 * TODO
	 * 1.情感词后缀修饰，如“香的很可怕”应该是负面
	 * 2.对情感词消歧（增加词性约束），如“可以”做形容词时是情感词，做情态动词时不是情感词
	 * 3.短语中出现的情感词应不予考虑，如“最佳情侣”中的“最佳”应该剔除
	 * 4.object出现在tuple短语之后时也要能够监听到object的存在
	 * 5.有些情感词后可以跟语气词，如“再也不会回购啦”
	 * 6.以前xxx，现在xxx的情况
	 * 7.“太xxx了”正负面无法判断
	 */
	//主体合并规则
	protected static final String[] OBJECT_NATURE_REGEX = new String[]{
			"(/#O.(/w)?)*/#O.",
	};
	// 主体覆盖句子数
	protected static final int OBJECT_LAG_NUM = 3;

	/**
	 * 根据feature调整sentiment的极性
	 */
	public int adjustPolarity(String domain, String feature, String sentiment, int polarity) {
		String[] split = feature.split(SPLIT);
		feature = split[split.length - 1];
		sentiment = sentiment.split("\\" + CORE)[1].split(SPLIT)[0];
		int featurePolarity = DomainFeatureDictionary.get(domain, feature);
		if (featurePolarity == Tuple.Polarity.negative.ordinal() || DomainExceptionDictionary.contains(domain, feature, sentiment)) {
			polarity = negPolarity(polarity);
		}
		return polarity;
	}

	@Override
	public List<Tuple> extractTuple(String text, String domain, String object) {
		PhraseSentence ps = new PhraseSentence(HanLP.segment(text));
		transformSentence(ps, domain);
		mergeSentence(ps);
		return extract(ps, domain, object);
	}

	/**
	 * 根据词典转换PhraseSentence
	 */
	protected abstract void transformSentence(PhraseSentence ps, String domain);

	/**
	 * 根据规则合并PhraseSentence
	 */
	protected abstract void mergeSentence(PhraseSentence ps);

	/**
	 * 根据规则抽取tuple
	 */
	protected abstract List<Tuple> extract(PhraseSentence ps, String domain, String object);

}
