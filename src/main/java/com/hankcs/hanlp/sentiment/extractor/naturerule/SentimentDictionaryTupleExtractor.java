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
	private static final String[] SENTIMENT_NATURE_REGEX = new String[]{
			"(/c)?/#S./ude/v/n",                             //香/#S的/ude闷/v人/n
			"(/c)?/#S./ude/p(?:/n|/vn)+/uyy",                  //香/#S的/ude跟/p理发/vn店/n/小妹/n一样/uyy
			"(/c)?/#S./ude(/d)*(/a)+",              //破/#S的/ude[不/d]像样/a
			"(/c)?(/d)*/v/#S.(?:/ule|/ude)?",              //磕/v破/#S了/ule
			"(/d|/c)*(/a/ude)?/#S.(/ule)?",     //但/c还/d不至于/d太/d闷/#S了/ule
	};

	private static final String[] FEATURE_NATURE_REGEX = new String[]{
			"((?:/n|/vn)(/ude)?)*(?:/n|/vn)",
	};

	private static final String[] TUPLE_REGEX = new String[]{
			"/#Sv/#Fn",
			"/#S.(?:/ude|/v)/#Fn",
			"/#Fn(?:(?:/a|/d)?/vshi|/v)?/#S.",
	};

	@Override
	protected List<Tuple> extract(PhraseSentence ps, String domain, String object) {
		List<Tuple> tuples = new LinkedList<>();
		transformSentence(ps, domain);
		mergeSentence(ps);
		ps.labelObject(object);
		for (int[] arr : ps.getIndexByRegex(TUPLE_REGEX, SENTIMENT_MARK)) {
			String feature = arr[0] == -1 ? "" : ps.get(arr[0]).word;
			String sentiment = ps.get(arr[1]).word;
			tuples.add(new Tuple(ps.get(arr[1]).object, feature, sentiment, analysePolarity(sentiment, ps.get(arr[1]).polarity)));
		}
		return tuples;
	}

	private void transformSentence(PhraseSentence ps, String domain) {
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

	private void mergeSentence(PhraseSentence ps) {
		ps.mergeByRegex(OBJECT_NATURE_REGEX, OBJECT_MARK + Nature.n)
				.stepmergeByRegex(SENTIMENT_NATURE_REGEX, SENTIMENT_MARK)
				.mergeByRegex(FEATURE_NATURE_REGEX, FEATURE_MARK + Nature.n);
	}
}
