package com.counect.cube.ocrcomparejpa.utils;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * url工具类
 *
 */
public class HttpUtilV2 {
	private static final Logger log = LoggerFactory.getLogger(HttpUtilV2.class);

	public enum HttpStatus {
		SUCCESS,
		ERROR,
		CONNECTION_TIMEOUT,
		SOCKET_TIMEOUT;
	}
	
	public static class HttpResponse {
		private HttpStatus httpStatus = HttpStatus.SUCCESS;
		private String response;
		
		public HttpResponse(String response) {
			this(HttpStatus.SUCCESS,response);
		}
		
		public HttpResponse(HttpStatus httpStatus, String response) {
			this.httpStatus = httpStatus;
			this.response = response;
		}
		public HttpResponse(HttpStatus httpStatus) {
			this(httpStatus,null);
		}

		public HttpStatus getHttpStatus() {
			return httpStatus;
		}
		public void setHttpStatus(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
		}
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
		
		public boolean isSuccess() {
			return HttpStatus.SUCCESS.equals(httpStatus);
		}
		
		@Deprecated
		public boolean isTimeout() {
			return HttpStatus.CONNECTION_TIMEOUT.equals(httpStatus) || HttpStatus.SOCKET_TIMEOUT.equals(httpStatus);
		}
		
		public boolean isConnectTimeout() {
			return HttpStatus.CONNECTION_TIMEOUT.equals(httpStatus);
		}
		
		public boolean isSocketTimeout() {
			return HttpStatus.SOCKET_TIMEOUT.equals(httpStatus);
		}
		
		@Override
		public String toString() {
			return "httpStatus:"+httpStatus+",response:"+response;
		}
		
	}
	
	/**
	 * 发送post stream请求
	 */
	public static HttpResponse postStream(String url, String data) {
		return postStream(url, data, custom());
	}

	 /**
     * 通过Https往API post xml数据
     * @param url    API地址
     */
    public static HttpResponse postStream(String url, String data, HttpRequestContext build) {

    	HttpResponse result = null;
    	CloseableHttpClient httpClient = null;
    	HttpUriRequest httpPost = null;
    	CloseableHttpResponse response = null;
		try {
			httpClient = getHttpClient(build);
			RequestBuilder requestBuild = RequestBuilder.post().setUri(url)
					.setEntity(new StringEntity(data, build.getEncoding())).setConfig(getRequestConfig(build));
			addHeads(requestBuild, build.getHeads());
			httpPost = requestBuild.build();
			
			
			response = httpClient.execute(httpPost);
			result = new HttpResponse(EntityUtils.toString(response.getEntity(), build.getEncoding()));
			
		}catch (Exception e) {
			result = exceptionResult(e);
			log.error("url："+url+",data:"+data,e);
		} finally {
			closeConnect(httpPost, response, httpClient);
		}
		
		return result;
    }

    
    private static HttpResponse exceptionResult(Exception e) {
    	HttpResponse result = null;
    	if(e instanceof ConnectTimeoutException) {
			result = new HttpResponse(HttpStatus.CONNECTION_TIMEOUT);
		} else if(e instanceof SocketTimeoutException) {
			result = new HttpResponse(HttpStatus.SOCKET_TIMEOUT);
		} else {
			result = new HttpResponse(HttpStatus.ERROR);
		}
    	return result;
    }
    
    
    /**
     * http put请求与post的区别为http协议上标识url的调用是幂等的
     * put请求接收方需要以流的方式接收参数
     * @param url
     * @param params
     * @return
     */
    public static HttpResponse doPut(String url, Map<String,String> params) {
    	return doPut(url, params, custom());
	}
    
    public static HttpResponse doPut(String url, Map<String, String> params, HttpRequestContext build) {
    	List<BasicNameValuePair> basicParams = new ArrayList<BasicNameValuePair>();
    	if(params!=null && !params.isEmpty()) {
    		for(String key : params.keySet()) {
    			basicParams.add(new BasicNameValuePair(key, params.get(key)));
    		}
    	}
    	return doPut(url, basicParams, build);
	}

	public static HttpResponse doPut(String url, List<BasicNameValuePair> basicParams) {
		return doPut(url, basicParams, custom());
	}

	public static HttpResponse doPut(String url, List<BasicNameValuePair> basicParams, HttpRequestContext build) {
		CloseableHttpClient client = null;
		CloseableHttpResponse res = null;
    	HttpResponse result = null;
    	HttpUriRequest put = null;
		
		try {
			client = getHttpClient(build);
			RequestBuilder requestBuild = RequestBuilder.put().setUri(url).setConfig(getRequestConfig(build))
				.setHeader("Connection", "Close")
				.setEntity(new UrlEncodedFormEntity(basicParams, build.getEncoding()));
			addHeads(requestBuild, build.getHeads());
	        put = requestBuild.build();
		
	        
	        res = client.execute(put);
			result = new HttpResponse(EntityUtils.toString(res.getEntity(), build.getEncoding()));
        
        } catch (Exception e) {
			result = exceptionResult(e);
        	log.error("url："+url+",data:"+basicParams,e);
		} finally {
			closeConnect(put, res, client);
		}
        return result;
	}

	public static HttpResponse doPut(String url, String data, HttpRequestContext build) {
		CloseableHttpClient client = null;
		CloseableHttpResponse res = null;
		HttpResponse result = null;
		HttpUriRequest put = null;
		
		try {
			client = getHttpClient(build);
			RequestBuilder requestBuild = RequestBuilder.put().setUri(url).setConfig(getRequestConfig(build))
				.setHeader("Connection", "Close")
				.setEntity(new StringEntity(data, build.getEncoding()));
			addHeads(requestBuild, build.getHeads());
	        put = requestBuild.build();
		
	        
	        res = client.execute(put);
			result = new HttpResponse(EntityUtils.toString(res.getEntity(), build.getEncoding()));
        
        } catch (Exception e) {
			result = exceptionResult(e);
        	log.error("url："+url+",data:"+data,e);
		} finally {
			closeConnect(put, res, client);
		}
        return result;
	}
	
	
	public static HttpResponse doGet(String url, Map<String, String> reqJson) {
		return doGet(url, reqJson, custom());
	}
    
    public static HttpResponse doGet(String url, Map<String, String> reqJson, HttpRequestContext build) {
    	List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
    	if(reqJson!=null && !reqJson.isEmpty()) {
    		for(String key : reqJson.keySet()) {
    			params.add(new BasicNameValuePair(key, reqJson.get(key)));
    		}
    	}
		return doGet(url, params, build);
	}
    
	public static HttpResponse doGet(String url, List<? extends NameValuePair> params) {
		return doGet(url, params, custom());
	}
    
	public static HttpResponse doGet(String url, List<? extends NameValuePair> params, HttpRequestContext build) {
		
		String urlString = CollectionUtils.isEmpty(params)? url: url + "?" + URLEncodedUtils.format(params, build.getEncoding());
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpResponse result = null;
		HttpUriRequest get = null;
		try {
			client = getHttpClient(build);
			RequestBuilder requestBuild = RequestBuilder.get().setUri(urlString).setConfig(getRequestConfig(build));
			addHeads(requestBuild, build.getHeads());
			get = requestBuild.build();

	        response = client.execute(get);
			result = new HttpResponse(EntityUtils.toString(response.getEntity(), build.getEncoding()));
		}catch (Exception e) {
			result = exceptionResult(e);
			log.error("get请求异常，url:" + urlString, e);
		} finally {
			closeConnect(get, response, client);
		}
		return result;
	}
	
	public static HttpResponse doPost(String url, Map<String, String> reqJson) {
		return doPost(url, reqJson, custom());
	}

    public static HttpResponse doPost(String url, String reqJson) {
        return doPost(url, reqJson, custom());
    }

    public static HttpResponse doPost(String url, String reqJson, HttpRequestContext build) {
        CloseableHttpClient client = null;
        CloseableHttpResponse res = null;
        HttpResponse result = null;
        HttpUriRequest post = null;

        try {
            client = getHttpClient(build);
            RequestBuilder requestBuild = RequestBuilder.post().setUri(url).setConfig(getRequestConfig(build))
                    .setHeader("Connection", "Close")
                    .setEntity(new StringEntity(reqJson));
            addHeads(requestBuild, build.getHeads());
            post = requestBuild.build();


            res = client.execute(post);
            result = new HttpResponse(EntityUtils.toString(res.getEntity(), build.getEncoding()));

        } catch (Exception e) {
            result = exceptionResult(e);
//            log.error("url："+url+",data:"+params,e);
        } finally {
            closeConnect(post, res, client);
        }
        return result;
    }

    public static HttpResponse doPost(String url, Map<String, String> reqJson, HttpRequestContext build) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if(reqJson!=null && !reqJson.isEmpty()) {
			for(String key : reqJson.keySet()) {
				params.add(new BasicNameValuePair(key, reqJson.get(key)));
			}
		}
		return doPost(url, params, build);
	} 

	public static HttpResponse doPost(String url, List<? extends NameValuePair> params) {
		return doPost(url, params, custom());
	}
	
	public static HttpResponse doPost(String url, List<? extends NameValuePair> params, HttpRequestContext build) {
		CloseableHttpClient client = null;
		CloseableHttpResponse res = null;
		HttpResponse result = null;
		HttpUriRequest post = null;
		
		try {
			client = getHttpClient(build);
			RequestBuilder requestBuild = RequestBuilder.post().setUri(url).setConfig(getRequestConfig(build))
				.setHeader("Connection", "Close")
				.setEntity(new UrlEncodedFormEntity(params, build.getEncoding()));
			addHeads(requestBuild, build.getHeads());
	        post = requestBuild.build();
		
	        
	        res = client.execute(post);
			result = new HttpResponse(EntityUtils.toString(res.getEntity(), build.getEncoding()));
        
        } catch (Exception e) {
			result = exceptionResult(e);
        	log.error("url："+url+",data:"+params,e);
		} finally {
			closeConnect(post, res, client);
		}
        return result;
	}

	private static void closeConnect(HttpUriRequest request, CloseableHttpResponse res, CloseableHttpClient client) {
		if(request != null) {
			try {
				request.abort();
			} catch (Exception e) {
	       		log.error("",e);
			}
		}
		
		if(res != null) {
			try {
				EntityUtils.consume(res.getEntity());
			} catch (IOException e1) {
	       		log.error("",e1);
			}
			
			try {
				res.close();
			} catch (IOException e) {
	       		log.error("",e);
			}
		}
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
	       		log.error("",e);
			}
		}
	}
	
	
	public static HttpRequestContext custom() {
    	return new HttpRequestContext();
    }

	private static void addHeads(RequestBuilder requestBuild, List<Header> heads) {
    	if(!CollectionUtils.isEmpty(heads)) {
    		for(Header head : heads) {
    			requestBuild.addHeader(head);
    		}
    	}
	}
	
    private static CloseableHttpClient getHttpClient(HttpRequestContext build) {
    	CloseableHttpClient client = null;
    	FileInputStream instream = null;
    	try {
			if(build.getKeyPath()!=null && !"".equals(build.getKeyPath())) {
				//使用证书
				KeyStore keyStore = KeyStore.getInstance(build.getKeyType());
				instream = new FileInputStream(new File(build.getKeyPath()));
				keyStore.load(instream, build.getCertPass().toCharArray());//设置证书密码
			
				SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, build.getCertPass().toCharArray()).build();
				// Allow TLSv1 protocol only
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslcontext,new String[]{"TLSv1"},null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				
				client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} else {
				client = HttpClients.createDefault();
			}
		}catch (Exception e) {
			log.error("",e);
       	} finally {
       		if(instream != null) {
	   			try {
					instream.close();
				} catch (IOException e) {
		       		log.error("",e);
				}
       		}
       	}
    	return client;
	}

    private static RequestConfig getRequestConfig(HttpRequestContext build) {
		return RequestConfig.custom()
				.setConnectTimeout(build.getConnectTimeout()).setSocketTimeout(build.getSocketTimeout()).build();
	}

	public static class HttpRequestContext {
   	   //连接超时时间，默认3秒
       private int socketTimeout = 30000;
       //传输超时时间，默认5秒
       private int connectTimeout = 50000;
       //https 签名证书路径
       private String keyPath;
       //证书密码
       private String certPass;
       private String keyType = "PKCS12";
       private String encoding = "UTF-8";
       
       private List<Header> heads;
       
       
       
		public String getEncoding() {
			return encoding;
		}
		public HttpRequestContext setEncoding(String encoding) {
			this.encoding = encoding;
			return this;
		}
		public int getSocketTimeout() {
			return socketTimeout;
		}
		public HttpRequestContext setSocketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
			return this;
		}
		public int getConnectTimeout() {
			return connectTimeout;
		}
		public HttpRequestContext setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}
		public String getKeyPath() {
			return keyPath;
		}
		public HttpRequestContext setKeyPath(String keyPath) {
			this.keyPath = keyPath;
			return this;
		}
		public String getCertPass() {
			return certPass;
		}
		public HttpRequestContext setCertPass(String certPass) {
			this.certPass = certPass;
			return this;
		}
		public String getKeyType() {
			return keyType;
		}
		public HttpRequestContext setKeyType(String keyType) {
			this.keyType = keyType;
			return this;
		}
		public List<Header> getHeads() {
			return heads;
		}
		public HttpRequestContext setHeads(List<Header> heads) {
			this.heads = heads;
			return this;
		}
		
   }

}
