package com.hankcs.hanlp.sentiment.common;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

/**
 * 句子的组成短语
 *
 * @author liukang
 * @since 2016/3/30
 */
public class Phrase {
	public String word;
	public String nature;
	public String mark;
	public String object;
	public int start;
	public int end;
	public int polarity;

	public Phrase(String word, Nature nature, int start, int end, String mark) {
		this.word = word;
		this.nature = nature.name();
		this.start = start;
		this.end = end;
		this.mark = mark;
	}

	public Phrase(Term t, int index) {
		this(t.word, t.nature, index, index + 1, "");
	}

	@Override
	public String toString() {
		return "Phrase{" +
				"word='" + word + '\'' +
				", nature='" + nature + '\'' +
				", mark='" + mark + '\'' +
				", object='" + object + '\'' +
				", start=" + start +
				", end=" + end +
				", polarity=" + polarity +
				'}';
	}
}
