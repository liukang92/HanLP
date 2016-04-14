package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.Sentiment;
import com.hankcs.hanlp.sentiment.analyzer.PatialNegativeSentimentAnalyzer;
import com.hankcs.hanlp.sentiment.common.SentimentUtil;
import com.hankcs.hanlp.sentiment.common.Tuple;
import com.hankcs.hanlp.sentiment.extractor.naturerule.FeatureDictionaryTupleExtractor;
import com.hankcs.hanlp.sentiment.extractor.naturerule.SentimentDictionaryTupleExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class DemoTupleExtract {
	public static void testSentimentAnalysis() {
		List<String> lines = null;
		int right = 0;
		int total = 0;
		try {
			lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang2.txt").getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		DictionaryBasedTupleExtractor extractor1 = new SentimentDictionaryTupleExtractor();
		DictionaryBasedTupleExtractor extractor2 = new FeatureDictionaryTupleExtractor();
		for (String sentence : lines) {
			String[] split = sentence.split("\t");
			String text = split[0].split("//")[0];
			String label = split[1];
//				System.out.println(text);
//				System.out.println(HanLP.segment(text));
			Sentiment.setSentimentAnalyzer(new PatialNegativeSentimentAnalyzer());
			String predict = SentimentUtil.to4PolarityString(HanLP.sentiment(text.trim(), "洗发水", "拉芳"));
			System.out.println(predict);
			if (label.contains(predict) || predict.contains(label)) {
				right++;
				total++;
			} else {
				List<Tuple> sentiment = extractor1.extractTuple(text.trim(), "洗发水", "$拉芳");
				List<Tuple> feature = extractor2.extractTuple(text.trim(), "洗发水", "$拉芳");
				if (sentiment.size() + feature.size() > 0) {
					System.out.println(text + ":" + label + ":" + predict);
					System.out.println(HanLP.segment(text));
					System.out.println("sentiment : " + sentiment);
					System.out.println("feature : " + feature);
					total++;
				}
			}
		}
		System.out.println("true:" + right + "\trelate:" + total + "\ttotal:" + lines.size());
		System.out.println((float) right / total);
	}

	public static void testTupleExtract() {
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
//		try {
//			List<String> lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
//			testCase = new String[lines.size()];
//			lines.toArray(testCase);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		DictionaryBasedTupleExtractor extractor1 = new SentimentDictionaryTupleExtractor();
		DictionaryBasedTupleExtractor extractor2 = new FeatureDictionaryTupleExtractor();
		for (String sentence : testCase) {
			sentence = sentence.split("\t")[0];
			System.out.println(sentence);
			System.out.println(HanLP.segment(sentence));
			System.out.println("sentiment : " + extractor1.extractTuple(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println("feature : " + extractor2.extractTuple(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println(SentimentUtil.toPolarityString(HanLP.sentiment(sentence.trim(), "洗发水", "拉芳")));
			System.out.println();
		}
	}

	public static void main(String[] args) {
		testSentimentAnalysis();
//		testTupleExtract();
	}
}
