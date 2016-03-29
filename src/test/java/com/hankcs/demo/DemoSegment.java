/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:02</create-date>
 *
 * <copyright file="DemoSegment.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 标准分词
 *
 * @author hankcs
 */
public class DemoSegment {
	public static void main(String[] args) {
		String[] testCase = new String[]{
				"买完第二天一看，洗发水漏了一点，其他没什么",
				"结婚的和尚未结婚的确实在干扰分词啊",
				"有一些以前年轻的时候的非主流香水的味道",
				"800g   12.5元/袋 金芦精制米5kg   25.9元/袋 福临门金粳稻大米5kg   29.9元/袋 福临门金典长粒香10kg    74.9元/袋 福临门花生调和油5L      46.9元/桶 鲁花压榨一级花生油4L    99.8元/桶",
				"在7 11买了iphone6和iphone 7，装了windows phone asdf系统，商品和服务实在不错",
				"http://www.baidu.com鹿晗代言的伊利QQ星，嘗嘗味道还不错liukang92@163.com",
				"买水果然后来世博园最后去世博会",
				"欢迎新老师生前来就餐",
				"工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作",
				"随着页游兴起到现在的页游繁盛，依赖于存档进行逻辑判断的设计减少了，但这块也不能完全忽略掉。",
		};
//		try {
//			List<String> lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
//			testCase = new String[lines.size()];
//			lines.toArray(testCase);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		HanLP.Config.enableDebug();
		for (String sentence : testCase) {
			List<Term> termList = HanLP.segment(sentence);
			System.out.println(termList);
		}
	}
}
