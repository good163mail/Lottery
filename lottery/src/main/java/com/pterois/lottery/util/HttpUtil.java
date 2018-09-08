package com.pterois.lottery.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
 
public class HttpUtil {
 
	public static void main(String[] args) {
		test();
	}
 
	
	    
	public static void test(){
		//http://www.koubei.com/?spm=0.0.0.117.pR54PP&city=110100[0,1]
		String url = "http://caipiao.163.com/award/gd11xuan5/20180907.html";
		String content = httpGet(url);
		String regex = "href=\"([\\S]*?)\"\\s*?class=\"nav_a\\s*?shanghu\"\\s*?target=\"_blank\">本地商户</a>";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			String target = matcher.group(1);
			System.err.println("target="+target);
		}
		
	}
	/**
	 * java使用代理发送http请求
	 * 
	 * @return
	 */
	public static String httpGet(String url) {
		String ip = "xxxxx";
		String content = null;
		DefaultHttpClient httpclient = null;
		try {
			httpclient = new DefaultHttpClient();
			/** 设置代理IP **/
			HttpHost proxy = new HttpHost(ip, 8080);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			HttpGet httpget = new HttpGet(url);
 
			httpget.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					1000 * 30); // 设置请求超时时间
			httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
			httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");	//需要加上这个头字段
 
			HttpResponse resp = httpclient.execute(httpget);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY){
 
				System.out.println("当前访问页面重定向了,,,");
				Header[] locationHeader = resp.getHeaders("Location");
				if (locationHeader != null && locationHeader.length > 0) {
					String redirectUrl = locationHeader[0].getValue();
					System.out.println("redirectUrl:" + redirectUrl);
				}
 
			} else if (statusCode == HttpStatus.SC_OK) {
				InputStream in = null;
				HttpEntity entity = resp.getEntity();
				Header header = entity.getContentEncoding();
				if(header != null && header.getValue().equalsIgnoreCase("gzip")){	//判断返回内容是否为gzip压缩格式
					
					System.err.println("gzip");
					GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(entity);
					in = gzipEntity.getContent();
				}else{
					in = entity.getContent();
				}
				content = getHTMLContent(in);
				System.out.println("content:" + content);
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown(); // 关闭连接
		}
		return content;
	}
 
	private static String getHTMLContent(InputStream in) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String line = null;
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}
