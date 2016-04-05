package com.hankcs.hanlp.sentiment.common;

import com.hankcs.hanlp.seg.common.Term;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * @author liukang
 * @since 2016/3/31
 */
public class PhraseSentence implements Iterable<Phrase> {
	private Phrase[] phrases;
	private int[] index;

	public PhraseSentence(List<Term> terms) {
		phrases = new Phrase[terms.size()];
		index = new int[size()];
		for (int i = 0; i < size(); i++) {
			phrases[i] = new Phrase(terms.get(i), i);
		}
	}

	//标记最近object
	public void labelObject(String object, int labelLag) {
		String lastObject = object;
		int lag = labelLag;
		for (int i = 0; i < size(); i = get(i).end) {
			if (get(i).mark.contains(OBJECT_MARK)) {
				lastObject = get(i).word;
				lag = labelLag;
			}
			if (lag > 0) {
				if (SEG_MARK.contains(get(i).word)) {
					lag--;
				}
				get(i).object = lastObject;
			}
		}
	}

	//遍历获取特定mark的短语列表
	public List<Integer> getPhraseList(String mark) {
		List<Integer> heads = new LinkedList<>();
		for (int i = 0; i < size(); i = get(i).end) {
			if (get(i).mark.contains(mark)) {
				heads.add(i);
			}
		}
		return heads;
	}

	//根据regexes对特定mark进行合并
	public PhraseSentence mergeByRegex(String[] regexes, String mark) {
		String natureString = toNatureString();
		for (String regex : regexes) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(natureString);
			while (m.find()) {
				String[] split = m.group().split(NATURE_MARK);
				int start = natureString.substring(0, m.start()).split(NATURE_MARK).length - 1;
				int end = start + split.length - 1;
				start = index[start];
				end = get(index[end - 1]).end;
				if (end >= get(start).end) {
					get(start).word = toWordString(start, -1, end);
					get(start).end = end;
					get(start).mark = NATURE_MARK + mark;
				}
			}
		}
		return this;
	}

	//根据regexes对特定mark逐条进行合并
	public PhraseSentence stepmergeByRegex(String[] regexes, String mark) {
		Set<Integer> used = new HashSet<>();
		for (int core : getPhraseList(mark)) {
			if (!get(core).mark.contains(mark) || used.contains(core)) {
				continue;
			}
			int maxLen = 0, maxStart = core, maxEnd = maxStart + maxLen;
			String natureString = toNatureString(core);
			for (String regex : regexes) {
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(natureString);
				while (m.find()) {
					String[] split = m.group().split(NATURE_MARK);
					int start = natureString.substring(0, m.start()).split(NATURE_MARK).length - 1;
					int end = start + split.length - 1;
					start = index[start];
					end = get(index[end - 1]).end;
					int len = end - start;
					if (len > maxLen) {
						maxStart = start;
						maxEnd = end;
						maxLen = len;
					}
				}
			}
			if (maxLen == 0){
				get(core).mark = get(core).mark.replace(mark, "");
			}else{
				for (int i = maxStart; i < maxEnd; i++) {
					if (get(i).mark.contains(SENTIMENT_MARK)) {
						used.add(i);
					}
				}
				mergeWords(maxStart, maxEnd, core);
			}
		}
		used.clear();
		return this;
	}

	//根据规则和指定mark短语列表，返回匹配的tupleIndex
	public List<int[]> getIndexByRegex(String[] regexes, String mark) {
		List<int[]> ret = new LinkedList<>();
		for (int core : getPhraseList(mark)) {
			int[] tuple = new int[]{-1, core};
			for (String regex : regexes) {
				Pattern p = Pattern.compile(regex);
				String natureString = toNatureString(core);
				Matcher m = p.matcher(natureString);
				if (m.find()) {
					int start = natureString.substring(0, m.start()).split(NATURE_MARK).length - 1;
					int end = start + m.group().split(NATURE_MARK).length - 1;
					for (int i = start; i < end; i++) {
						int cur = index[i];
						if (get(cur).mark.contains(FEATURE_MARK) || get(cur).mark.contains(OBJECT_MARK)) {
							tuple[0] = cur;
						}
					}
					break;
				}
			}
			ret.add(tuple);
		}
		return ret;
	}

	//根据规则返回匹配的tupleIndex
	public List<int[]> getIndexByRegex(String[] regexes) {
		List<int[]> ret = new LinkedList<>();
		for (String regex : regexes) {
			Pattern p = Pattern.compile(regex);
			String natureString = toNatureString();
			Matcher m = p.matcher(natureString);
			while (m.find()) {
				int start = natureString.substring(0, m.start()).split(NATURE_MARK).length - 1;
				int end = start + m.group().split(NATURE_MARK).length - 1;
				int len = end - start;
				int[] tuple = new int[len];
				System.arraycopy(index, start, tuple, 0, len);
				ret.add(tuple);
			}
		}
		return ret;
	}

	//返回没有核心词的词性字符串
	public String toNatureString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = 0; i < size(); i = get(i).end, n++) {
			sb.append(get(i).mark);
			index[n] = i;
		}
		return sb.toString();
	}

	//返回以某一个词为核心词的词性字符串
	public String toNatureString(int core) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = 0; i < size(); i = get(i).end, n++) {
			if (get(i).mark.contains(SENTIMENT_MARK) && i != core) {
				sb.append(NATURE_MARK).append(get(i).nature);
			} else {
				sb.append(get(i).mark);
			}
			index[n] = i;
		}
		return sb.toString();
	}

	//返回指定范围内的短语
	public String toWordString(int start, int core, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i = get(i).end) {
			sb.append(SPLIT);
			if (i == core) {
				sb.append(CORE);
			}
			sb.append(get(i).word.replace(CORE, ""));
		}
		return sb.delete(0, 1).toString();
	}

	public Phrase get(int i) {
		return phrases[i];
	}

	public int size() {
		return phrases.length;
	}

	private void mergeWords(int start, int end, int core) {
		get(start).word = toWordString(start, core, end);
		get(start).end = end;
		get(start).mark = get(core).mark;
		get(start).object = get(core).object;
		get(start).polarity = get(core).polarity;
	}

	@Override
	public String toString() {
		return "PhraseSentence{" +
				"phrases=" + Arrays.toString(phrases) +
				'}';
	}

	@Override
	public Iterator<Phrase> iterator() {
		return new PhraseIterator();
	}

	class PhraseIterator implements Iterator<Phrase> {
		//TODO
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Phrase next() {
			return null;
		}

		@Override
		public void remove() {

		}
	}
}
