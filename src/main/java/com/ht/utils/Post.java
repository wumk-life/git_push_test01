package com.ht.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于进行同步请求的post请求工具类
 * @ClassName: Post 
 * @Description: 用于进行同步请求的post请求工具类
 * @author g.yang@i-vpoints.com
 * @date 2015年12月31日 下午4:27:44 
 */
public class Post {

	/**
	 * 用于进行同步请求的post请求工具类
	 * @Title: post 
	 * @param contentType application/x-www-form-urlencoded;charset=utf-8
	 * @return String
	 * 		接收到信息的字符串形式
	 * @author g.yang@i-vpoints.com 
	 * @throws IOException 
	 * @date 2015年12月31日 下午4:26:57 
	 */
	public static final String post(String url,Map<String,String> params,String contentType) throws IOException{
		String result = "";
	    Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
	    Protocol.registerProtocol("https", https);
	    PostMethod post = new PostMethod(url);
	    HttpClient client = new HttpClient();
	    HttpClientParams clientParams = new HttpClientParams();
	    client.setParams(clientParams);
	    try {
	        post.setRequestHeader("Content-Type", contentType);
	        List<NameValuePair> list = new ArrayList<NameValuePair>();
	        NameValuePair nvp = null;
	        for(Map.Entry<String, String> entry : params.entrySet()){
	        	nvp = new NameValuePair();
	        	nvp.setName(entry.getKey());
	        	nvp.setValue(entry.getValue());
	        	list.add(nvp);
	        }
	        System.out.println("list:"+list);
	        post.setRequestBody(list.toArray(new NameValuePair[]{}));
	        client.executeMethod(post);
	        result = post.getResponseBodyAsString();
	        Protocol.unregisterProtocol("https");
	    } catch (HttpException e) {
	        throw e;
	    } catch (IOException e) {
	    	throw e;
	    } catch(Exception e) {
	    	throw e;
	    }
	    return result;
	}
	
	/**
	 * post请求工具类
	 * 
	 * @date 2017年7月27日下午4:01:09
	 * @param url
	 * @param param	post请求参数,a=a&b=b格式
	 * @return
	 * @throws UnsupportedEncodingException
	 * @author jq.yin@i-vpoints.com
	 */
	public static String post(String url, String param) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		// 设置参数
		StringEntity stringEntity = new StringEntity(param, "UTF-8");
		// 正文是URLEncoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
		stringEntity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(stringEntity);
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例
			httpClient = HttpClients.createDefault();
//			httpPost.setConfig(config);	// timeout时间等设置
			// 执行请求
			httpResponse = httpClient.execute(httpPost);
//			log.debug("请求起始行:" + httpResponse.getStatusLine().toString());
//			// 获得首部信息
//			log.debug("【获取响应头信息】");
//			Header[] hs = httpResponse.getAllHeaders();
//			for (Header h : hs) {
//				log.debug(h.getName() + "\t" + h.getValue());
//			}
			entity = httpResponse.getEntity();
			if( null != entity){
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭相关资源
			try {
				/*
				 * 关闭资源，底层的流,以下代码即:
				 * InputStream inputStream = entity.getContent();
				 * inputStream.close();
				 */
				EntityUtils.consume(entity);

				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	/**
	 * 发送非K-V形式参数的post请求
	 * @Title: notKvPost
	 * @return String
	 * @author g.yang@i-vpoints.com
	 * @date 2016年7月19日 上午11:38:54
	 */
	public static String notKvPost(String url, String content){
		StringBuilder responseContent = new StringBuilder();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			StringEntity se = new StringEntity(content);
			httpPost.setEntity(se);
			response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
			String line = br.readLine();
			while(null!=line){
				responseContent.append(line);
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				response.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return responseContent.toString();
	}

	public static String postJson(String url, String jsonString) throws IOException {
		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", https);
		PostMethod post = new PostMethod(url);
		HttpClient client = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		client.setParams(clientParams);
		post.setRequestHeader("Content-Type", "application/json;charset=utf-8");
		RequestEntity re = new StringRequestEntity(jsonString, "application/json", "utf-8");
		post.setRequestEntity(re);
		client.executeMethod(post);
		String result = post.getResponseBodyAsString();
		Protocol.unregisterProtocol("https");
		return result;
	}

}
