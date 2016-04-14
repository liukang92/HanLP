package com.hankcs.hanlp.sentiment.extractor.dependencyrule;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dependency.nnparser.Dependency;
import com.hankcs.hanlp.dictionary.domain.DomainDictionary;
import com.hankcs.hanlp.dictionary.domain.DomainSentimentDictionary;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.ITupleExtractor;
import com.hankcs.hanlp.sentiment.common.PhraseSentence;
import com.hankcs.hanlp.sentiment.common.SentimentUtil;
import com.hankcs.hanlp.sentiment.common.Tuple;
import org.apache.commons.lang3.ArrayUtils;

import java.util.LinkedList;
import java.util.List;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class SentimentDictionaryTupleExtractor extends DictionaryBasedTupleExtractor {
	// 注意优先级问题，一旦匹配成功则返回情感短语
	// 情感短语识别规则
	private static final String[] SENTIMENT_NATURE_REGEX = new String[]{
			"定中关系",	//定中关系ATT（attribute）
			"数量关系",	//数量关系QUN（quantity）
			"并列关系",	//并列关系COO（coordinate）
			"同位关系",	//同位关系APP（appositive）
			"左附加关系",	//前附加关系LAD（left adjunct）
			"右附加关系",	//后附加关系RAD（right adjunct）
			"动宾关系",	//动宾关系VOB（verb-object）
			"介宾关系",	//介宾关系POB（preposition-object）
			"主谓关系",	//主谓关系SBV（subject-verb）
			"比拟关系",	//比拟关系SIM（similarity）
			"核心关系",		//核心HED（head）
			"连谓结构",	//连谓结构VV（verb-verb）
			"关联结构",	//关联结构CNJ（conjunctive）
			"语态结构",	//语态结构MT（mood-tense）
			"独立结构",	//独立结构IS（independent structure）
			"状中结构",	//状中结构ADV（adverbial）
			"动补结构",	//动补结构CMP（complement）
			"“的”字结构",	//“的”字结构DE
			"“地”字结构",	//“地”字结构DI
			"“得”字结构",	//“得”字结构DEI
			"“把”字结构",	//“把”字结构BA
			"“被”字结构",	//“被”字结构BEI
			"独立分句",	//独立分句IC（independent clause）
			"依存分句",	//依存分句DC（dependent clause）
	};

	//右拓展关系
	private static  final String[] RIGHT_EXPAND_DEPENDENCY = {
			"右附加关系",	//后附加关系RAD（right adjunct）
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
			if (object == null) {
				continue;
			}
			String feature = arr[0] == -1 ? null : ps.get(arr[0]).word;
			String sentiment = ps.get(arr[1]).word;
			int polarity = ps.get(arr[1]).polarity;
			if (feature != null) {
				polarity = adjustPolarity(domain, feature, sentiment, polarity);
			}
			tuples.add(new Tuple(object, feature, sentiment, analysePolarity(sentiment, polarity)));
		}
		return tuples;
	}

	protected List<Tuple> extract(String text, String domain, String defaultObject) {
		List<Tuple> tuples = new LinkedList<>();
		CoNLLSentence cs = HanLP.parseDependency(text);
		for (CoNLLWord word : cs){
			if (DomainSentimentDictionary.contains(domain, word.NAME)){
				StringBuilder sb = new StringBuilder(word.NAME);
				for (CoNLLWord fw : word.FOLLOW){
					if (ArrayUtils.contains(RIGHT_EXPAND_DEPENDENCY, fw.DEPREL)){
						sb.append(fw.NAME);
					}
				}
				System.out.println(sb.toString());
			}
		}
		System.out.println(cs);
		return tuples;
	}

	@Override
	protected void transformSentence(PhraseSentence ps, String domain) {
		for (int i = 0; i < ps.size(); i++) {
			String word = ps.get(i).word;
			ps.get(i).nature = convertNature(ps.get(i).nature);
			int polarity = DomainSentimentDictionary.get(domain, word);
			if (DomainDictionary.contains(domain, word)) {
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

	public static void main(String[] args) {
		String[] testCase = new String[]{
				"光凭前代言人是张燕这一条就足够low了好吗，别的压根不用比[doge]",
				"物流有点不给力，习惯好评",
				"开始买的时候没看清，以为是洗发露，结果是护发素，好大一瓶，也好",
				"回复:喂你好吗？胃你好吗？ 去头屑用雨洁☞用了全是头屑☜",
				"回复:糖洗发水广告电视上貌似看到过，拍得好难看啊",
				"偶然发现拉芳洗发水的代言人竟然是“某人”，那么好的，剩下的半瓶真的可以顺理成章地用来洗衣服啦，并且再也不会回购啦~",
				"别买！香的跟洗发店小妹一样！用的时候都要熏吐了！",
				" 【9品牌洗发水检测比对报告：清洁有效物含量飘柔最低】4月22日讯，为了解洗发水的清洁效果，2014年3月，《消费者报道》对市面上比较常见的海飞丝、飘柔、清扬、巴黎欧莱雅、舒蕾、拉芳、霸王等7款去屑洗发水进行了测试。检测结果显示，这7个品牌样品中，巴黎欧莱雅的有效物含量最高，飘柔最低。",
				"韩国润膏多功效，二和一洗发护发。自用款，我感觉洗后头皮特别舒服。之前用拉芳、欧莱雅一天后头皮都特别痒。",
				"爱生活，远离拉芳[喵喵]",
				"拉芳不如韩束，链家是什么",
				"之前用了感觉不怎么样 现在在用一款洛菲洗发水还不错",
				"装修之后沃尔玛环境有很明显的改善",
				"之前用过，不太喜欢，现在用雅顺的",
		};
		SentimentDictionaryTupleExtractor extractor = new SentimentDictionaryTupleExtractor();
		for (String sentence : testCase) {
			sentence = sentence.split("\t")[0];
			System.out.println(sentence);
			System.out.println(HanLP.segment(sentence));
			System.out.println("sentiment : " + extractor.extract(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println();
		}
	}
}
