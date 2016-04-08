package com.hankcs.hanlp.sentiment.common;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.sentiment.common.Tuple.Polarity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liukang
 * @since 2016/3/30
 */
public class SentimentUtil {
	public static final String SEG_MARK = "。|！|？";
	public static final String[] NEGATIVE_WORD = "不|不是|不会|不够|不大|不得|不用|不算|不能|不见得|不须|不至于|免于|尚未|并不|并未|无|无从|无法|无须|有待|未|杜绝|毫无|没|没有|绝非|非|非同".split("\\|");
	public static final String CONJUNCTION_WORD = "但|还是";
	public static final String SENTIMENT_MARK = "#S";
	public static final String FEATURE_MARK = "#F";
	public static final String OBJECT_MARK = "#O";
	public static final String CORE = "$";
	public static final String SPLIT = "#";
	public static final String NATURE_MARK = "/";

	public static boolean isStartsWithCertainNature(String n, String... narr) {
		for (String nature : narr) {
			if (n.startsWith(nature)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isConjunctionWord(String word) {
		return ArrayUtils.contains(CONJUNCTION_WORD.split("\\|"), word);
	}

	public static boolean isMatchRegex(String text, String... regexes) {
		for (String regex : regexes) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	public static Pair<Integer, Integer> getOneMatcher(String text, String[] regexes, boolean first) {
		MutablePair<Integer, Integer> pair = new MutablePair<>(0, 0);
		for (String regex : regexes) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			if (first) {
				if (m.find()) {
					pair.setLeft(m.start());
					pair.setRight(m.end());
					break;
				}
			} else {
				while (m.find()) {
					pair.setLeft(m.start());
					pair.setRight(m.end());
				}
			}
		}
		return pair;
	}

	public static String convertNature(Nature n) {
		switch (n) {
			case ude1:
			case ude2:
			case ude3:
				return "ude";
			case rz:
			case rzs:
			case rzt:
			case rzv:
				return "rz";
			case ule:
			case vshi:
			case vn:
				return n.name();
			default:
				return n.name().substring(0, 1);
		}
	}

	public static String convertNature(String s) {
		return convertNature(Nature.valueOf(s));
	}

	public static int posPolarity(int polarity) {
		if (polarity == Polarity.positive.ordinal()) {
			return Polarity.positive.ordinal();
		} else if (polarity == Polarity.negative.ordinal()) {
			return Polarity.negative.ordinal();
		} else {
			return Polarity.positive.ordinal();
		}
	}

	public static int negPolarity(int polarity) {
		if (polarity == Polarity.positive.ordinal()) {
			return Polarity.negative.ordinal();
		} else if (polarity == Polarity.negative.ordinal()) {
			return Polarity.positive.ordinal();
		} else {
			return Polarity.negative.ordinal();
		}
	}

	public static int analysePolarity(String sentiment, int polarity) {
		for (String word : sentiment.split(SPLIT)) {
			if (ArrayUtils.contains(NEGATIVE_WORD, word)) {// || !word.startsWith(CORE) && DomainSentimentDictionary.get("洗发水", word) == 2
				polarity = negPolarity(polarity);
			}
		}
		return polarity;
	}

	public static String toPolarityString(float polarity){
		if (polarity > 0){
			return "正面";
		}else if(polarity < 0){
			return "负面";
		}
		return "中性";
	}
}
