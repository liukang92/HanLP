package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.domain.DomainDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.sentiment.common.Phrase;
import com.hankcs.hanlp.sentiment.common.PhraseSentence;
import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * 基于词典的tuple提取器
 *
 * @author liukang
 * @since 2016/3/28
 */
public abstract class DictionaryBasedTupleExtractor implements ITupleExtractor {
	//主题合并规则
	protected static final String[] OBJECT_NATURE_REGEX = new String[]{
			"(/#O.(/w)?)*/#O.",
	};
	// 主体覆盖句子数
	protected static final int OBJECT_LAG_NUM = 3;


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
