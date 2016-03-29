package com.hankcs.hanlp.sentiment.simple;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreSentimentDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.sentiment.ITupleExtractor;
import com.hankcs.hanlp.sentiment.common.Tuple;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class SimpleRuleTupleExtractor implements ITupleExtractor {
	private static final int DETECT_TERM_NUM = 5;
	private static final String[] PREFIX_DEPREL = new String[]{"定中关系", "状中结构"};

	private boolean isStartsWithCertainNature(String n, String... narr) {
		for (String nature : narr) {
			if (n.startsWith(nature)) {
				return true;
			}
		}
		return false;
	}

	private String fillSentimentPrefix(CoNLLWord word) {
		StringBuilder sb = new StringBuilder();
		for (CoNLLWord fw : word.FOLLOW) {
			if (ArrayUtils.contains(PREFIX_DEPREL, fw.DEPREL) &&
					isStartsWithCertainNature(fw.POSTAG, Nature.d.name(), Nature.a.name())) {
				sb.append(fillSentimentPrefix(fw));
			}
		}
		return sb.append(word.LEMMA).toString();
	}

	private void detect(CoNLLSentence sentence, int i, String object, List<Tuple> tuples) {
		CoNLLWord sentiment = sentence.get(i);
		int polarity = CoreSentimentDictionary.get(sentiment.LEMMA);
		if (polarity != 0) {
			boolean backward = true,
					forward = true;
			for (int j = 1; j <= DETECT_TERM_NUM; j++) {
				if (backward && i - j > -1) {
					CoNLLWord feature = sentence.get(i - j);
					if (isStartsWithCertainNature(feature.POSTAG, Nature.n.name())) {
//						tuples.add(new Tuple(feature.LEMMA, sentiment.LEMMA, polarity));
						tuples.add(new Tuple(object, feature.LEMMA, fillSentimentPrefix(sentiment), polarity));
						break;
					} else if (isStartsWithCertainNature(feature.POSTAG, Nature.w.name()) || i - j == 0) {
						backward = false;
					}
				}

				if (forward && i + j < sentence.size()) {
					CoNLLWord feature = sentence.get(i + j);
					if (isStartsWithCertainNature(feature.POSTAG, Nature.n.name())) {
//						tuples.add(new Tuple(feature.LEMMA, sentiment.LEMMA, polarity));
						tuples.add(new Tuple(object, feature.LEMMA, fillSentimentPrefix(sentiment), polarity));
						break;
					} else if (isStartsWithCertainNature(feature.POSTAG, Nature.w.name()) || i + j == sentence.size() - 1) {
						forward = false;
					}
				}
			}
		}
	}

	@Override
	public List<Tuple> extractTuple(String text) {
		List<Tuple> tuples = new LinkedList<>();
//		List<Term> tokens = new ArrayList<>();
//		tokens.addAll(HanLP.segment(text));
		CoNLLSentence sentence = HanLP.parseDependency(text);
		System.out.println(sentence);
		String object = "拉芳";
		for (int i = 0; i < sentence.size(); i++) {
			if (sentence.get(i).POSTAG.equals(Nature.ntc.name())){
				object = sentence.get(i).LEMMA;
			}
			detect(sentence, i, object, tuples);
		}
		return tuples;
	}
}
