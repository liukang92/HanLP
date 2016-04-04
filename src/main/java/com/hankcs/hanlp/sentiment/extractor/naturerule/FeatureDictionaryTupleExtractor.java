package com.hankcs.hanlp.sentiment.extractor.naturerule;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.domain.DomainDictionary;
import com.hankcs.hanlp.dictionary.domain.DomainFeatureDictionary;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.common.PhraseSentence;
import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.*;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class FeatureDictionaryTupleExtractor extends DictionaryBasedTupleExtractor {
	private static final Map<String, Integer> FEATURE_PRE_WORD = new HashMap<String, Integer>() {{
		put("有", 1);
		put("没有", 2);
	}};
	private static final Map<String, Integer> OBJECT_PRE_WORD = new HashMap<String, Integer>() {{
		put("用", 1);
		put("不用", 2);
	}};
	private static final Map<String, Integer> OBJECT_MID_WORD = new HashMap<String, Integer>() {{
		put("比得上", 1);
		put("比不上", 2);
		put("不如", 2);
	}};
	private static final String FEATURE_PRE_MARK = "#PF";
	//	private static final String OBJECT_PRE_MARK = "#PO";
	private static final String OBJECT_MID_MARK = "#MO";

	private static final String[] FEATURE_NATURE_REGEX = new String[]{
			"(((/n)|(/vn))(/ude)?)*/#F.",
	};

	private static final String[] TUPLE_REGEX = new String[]{
//			"/#PO./#O.",
			"/#PF./#F.",
	};
	private static final String[] TRITUPLE_REGEX = new String[]{
			"/#O./#MO./#O.",
	};

	@Override
	protected List<Tuple> extract(PhraseSentence ps, String domain, String object) {
		List<Tuple> tuples = new LinkedList<>();
		transformSentence(ps, domain);
		mergeSentence(ps);
		ps.labelObject(object);
		for (int[] arr : ps.getIndexByRegex(TUPLE_REGEX)) {
			String feature = null;
			String sentiment = null;
			for (int i : arr) {
				if (ps.get(i).mark.contains(OBJECT_MARK)) {
					object = feature = ps.get(i).word;
				} else if (ps.get(i).mark.contains(FEATURE_MARK)) {
					feature = ps.get(i).word;
				} else {
					sentiment = ps.get(i).word;
				}
			}
			if (object.equals(feature)) {
				tuples.add(new Tuple(object, feature, sentiment, OBJECT_PRE_WORD.get(sentiment)));
			} else {
				tuples.add(new Tuple(object, feature, sentiment, FEATURE_PRE_WORD.get(sentiment)));
			}
		}
		for (int[] arr : ps.getIndexByRegex(TRITUPLE_REGEX)) {
			tuples.add(new Tuple(ps.get(arr[0]).word, ps.get(arr[0]).word, ps.get(arr[1]).word + ps.get(arr[2]).word, OBJECT_MID_WORD.get(ps.get(arr[1]).word)));
			tuples.add(new Tuple(ps.get(arr[2]).word, ps.get(arr[0]).word, ps.get(arr[1]).word + ps.get(arr[2]).word, negPolarity(OBJECT_MID_WORD.get(ps.get(arr[1]).word))));
		}
		return tuples;
	}

	private void transformSentence(PhraseSentence ps, String domain) {
		for (int i = 0; i < ps.size(); i++) {
			String word = ps.get(i).word;
			ps.get(i).nature = convertNature(ps.get(i).nature);
			ps.get(i).mark = NATURE_MARK;
			if (DomainDictionary.dict.containsKey(word)) {
				ps.get(i).mark += OBJECT_MARK;
			} else if (DomainFeatureDictionary.contains(domain, word)) {
				ps.get(i).mark += FEATURE_MARK;
			} else if (FEATURE_PRE_WORD.containsKey(word)) {
				ps.get(i).mark += FEATURE_PRE_MARK;
//			} else if (OBJECT_PRE_WORD.containsKey(word)) {
//				ps.get(i).mark += OBJECT_PRE_MARK;
			} else if (OBJECT_MID_WORD.containsKey(word)) {
				ps.get(i).mark += OBJECT_MID_MARK;
			}
			ps.get(i).mark += ps.get(i).nature;
		}
	}

	private void mergeSentence(PhraseSentence ps) {
		ps.mergeByRegex(OBJECT_NATURE_REGEX, OBJECT_MARK + Nature.n)
				.stepmergeByRegex(FEATURE_NATURE_REGEX, FEATURE_MARK);
	}
}
