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
 * @author liukang
 * @since 2016/3/28
 */
public abstract class DictionaryBasedTupleExtractor implements ITupleExtractor {
	//	protected static final int DETECT_TERM_NUM = 5;

	protected static final String[] OBJECT_NATURE_REGEX = new String[]{
			"(/#O.(/w)?)*/#O.",
	};

	protected boolean belongsDomain(String word, String domain) {
		return domain.equals(DomainDictionary.dict.get(word));
	}

	protected abstract List<Tuple> extract(PhraseSentence ps, String domain, String object);

	@Override
	public List<Tuple> extractTuple(String text, String domain, String object) {
		return extract(new PhraseSentence(HanLP.segment(text)), domain, object);
	}
}
