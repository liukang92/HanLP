package com.hankcs.hanlp.sentiment.extractor.naturerule;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.domain.DomainDictionary;
import com.hankcs.hanlp.dictionary.domain.DomainSentimentDictionary;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.common.PhraseSentence;
import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.*;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class SentimentDictionaryTupleExtractor extends DictionaryBasedTupleExtractor {
	// 注意优先级问题，一旦匹配成功则返回情感短语
	// 情感短语识别规则
	private static final String[] SENTIMENT_NATURE_REGEX = new String[]{
			"(/c)?/#S./ude/v/n(?!/y)",                    		// 香/#S的/ude闷/v人/n
			"(/c)?/#S./ude/p(?:/n|/vn)+/uyy(?!/y)",				// 香/#S的/ude跟/p理发/vn店/n/小妹/n一样/uyy
			"(/c)?/#S./v(?:/n|/vn)(?!/y)",						// 香/#S的/ude跟/p理发/vn店/n/小妹/n一样/uyy
			"(/c)?/#S./ude(/d)*(/a)+(?!/y)",              		// 破/#S的/ude[不/d]像样/a
			"(/c)?(/d)*(/v)?/#S.((?:/ule|/ude)(/m)?)?(?!/y)",	// 磕/v破/#S了/ule
			"(/c)?(/d)*(/a/ude)?/#S.(?!/y)",     				// 但/c还/d不至于/d太/d闷/#S了/ule
	};
	// 特征短语识别规则
	private static final String[] FEATURE_NATURE_REGEX = new String[]{
			"((?:/n|/vn)(/ude)?)*(?:/n|/vn)",
	};
	// tuple识别规则
	private static final String[] TUPLE_REGEX = new String[]{
			"/#Sv/#(?:O|F)n",
			"/#S.(?:/ude|/v)/#(?:O|F)n",
			"/#(?:O|F)n(?:(?:/a|/d)?/vshi|/v)?/#S.",
	};

	@Override
	protected List<Tuple> extract(PhraseSentence ps, String domain, String defaultObject) {
		List<Tuple> tuples = new LinkedList<>();
		ps.labelObject(defaultObject, OBJECT_LAG_NUM);
		for (int[] arr : ps.getIndexByRegex(TUPLE_REGEX, SENTIMENT_MARK)) {
			String object = ps.get(arr[1]).object;
			if (object == null){
				continue;
			}
			String feature = arr[0] == -1 ? null : ps.get(arr[0]).word;
			String sentiment = ps.get(arr[1]).word;
			tuples.add(new Tuple(ps.get(arr[1]).object, feature, sentiment, analysePolarity(sentiment, ps.get(arr[1]).polarity)));
		}
		return tuples;
	}

	@Override
	protected void transformSentence(PhraseSentence ps, String domain) {
		for (int i = 0; i < ps.size(); i++) {
			String word = ps.get(i).word;
			ps.get(i).nature = convertNature(ps.get(i).nature);
			int polarity = DomainSentimentDictionary.get(domain, word);
			if (DomainDictionary.dict.containsKey(word)) {
				ps.get(i).mark = NATURE_MARK + OBJECT_MARK + ps.get(i).nature;
			} else if (polarity != 0) {
				ps.get(i).mark = NATURE_MARK + SENTIMENT_MARK + ps.get(i).nature;
				ps.get(i).polarity = polarity;
			} else {
				ps.get(i).mark = NATURE_MARK + ps.get(i).nature;
			}
		}
	}


	@Override
	protected void mergeSentence(PhraseSentence ps) {
		ps.mergeByRegex(OBJECT_NATURE_REGEX, OBJECT_MARK + Nature.n)
				.stepmergeByRegex(SENTIMENT_NATURE_REGEX, SENTIMENT_MARK)
				.mergeByRegex(FEATURE_NATURE_REGEX, FEATURE_MARK + Nature.n);
	}
}
