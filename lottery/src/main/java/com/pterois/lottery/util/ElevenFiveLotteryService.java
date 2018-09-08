package com.pterois.lottery.util;

public class ElevenFiveLotteryService {
	
	
	public static void main(String[] args) {
		String url="http://caipiao.163.com/award/gd11xuan5/20180907.html";  
		String filepath = "D:\\test\\20180907.html";  
		HttpUtil.download(url, filepath);  
	}

}
