package com.sgwr.app.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import com.sgwr.app.AppContext;
import com.sgwr.app.AppException;


public class HttpUtils {
	private final static int connectionRequestTimeout = 20000;
	private final static int connectTimeout = 20000;
	private final static int connManTimeout = 20000;
	private static DefaultHttpClient httpClient;

	public enum HoldCookieType
	{
		File, AppContext
	}

	private static final HoldCookieType cookieType = HoldCookieType.File;

	/**
	 * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
	 */
	static ResponseHandler<Map<String, Object>> responseHandler = new ResponseHandler<Map<String, Object>>() {
		@Override
		public Map<String, Object> handleResponse(HttpResponse response)
		{
			// TODO Auto-generated method stub
			byte[] bytes = null;
			String strMsg = null;
			Map<String, Object> maps = new HashMap<String, Object>();

			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK)
			{
				HttpEntity entity = response.getEntity();
				if (entity == null)
				{
					strMsg = "Response content is null.";
					bytes = null;
				}
				else
				{
					try
					{
						strMsg = "OK";
						bytes = EntityUtils.toByteArray(entity);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						strMsg = e.getMessage() + "  " + e.getStackTrace();
						bytes = null;
					}
					finally
					{
						try
						{
							entity.consumeContent();
						}
						catch (IOException e)
						{
							strMsg = e.getMessage() + "  " + e.getStackTrace();
							bytes = null;
						}
					}
				}
			}
			else
			{
				strMsg = statusLine.getStatusCode() + "     "
						+ statusLine.getReasonPhrase();
				bytes = null;
			}

			maps.put("message", strMsg);
			maps.put("bytes", bytes);

			return maps;
		}
	};

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context)
		{
			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount >= 3)
			{
				// 如果连接次数超过了最大值则停止重试
				return false;
			}
			if (exception instanceof NoHttpResponseException)
			{
				// 如果服务器连接失败重试
				return true;
			}
			if (exception instanceof SSLHandshakeException)
			{
				// 不要重试ssl连接异常
				return false;
			}

			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent)
			{
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

	public static String getUserAgent()
	{
		String userAgent = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
				+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
		return userAgent;
	}

	/**
	 * 初始化设置 HttpClient
	 */
	static
	{
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		HttpProtocolParams.setUserAgent(params, getUserAgent());
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, connectTimeout);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, connectionRequestTimeout);
		// Cookie策略
		HttpClientParams.setCookiePolicy(params,
				CookiePolicy.BROWSER_COMPATIBILITY);
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, connManTimeout);
		// 增加最大连接到200
		ConnManagerParams.setMaxTotalConnections(params, 200);
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager connManager = new ThreadSafeClientConnManager(
				params, schReg);

		httpClient = new DefaultHttpClient(connManager, params);
		httpClient.setHttpRequestRetryHandler(requestRetryHandler);
	}

	/**
	 * 将参数和URL序列化 给 GET 请求
	 * 
	 * @param url
	 * @param params
	 * @param encode
	 * @return
	 */
	public static String getUrlByParams(String url, Map<String, String> params,
			String encode)
	{
		StringBuilder sb = new StringBuilder(url);
		try
		{
			if (sb.indexOf("?") < 0)
			{
				sb.append("?");
			}
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				if (entry != null)
				{
					sb.append("&");
					sb.append(entry.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue(), encode));
				}
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return sb.toString().replace("?&", "?");
	}

	/**
	 * Http Get 请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] requestHttpGet(AppContext context, String url)
			throws AppException
	{
		byte[] bytes = null;
		HttpGet httpGet = null;
		httpGet = new HttpGet(url);
		HttpContext clientContext = new BasicHttpContext();
		if (context != null)
		{
			if (cookieType == HoldCookieType.File)
			{
				String cookie = readCookie(context);
				if (!StringUtils.isEmpty(cookie))
				{
					httpGet.setHeader("Cookie", cookie);
				}
			}
			else
			{
				CookieStore cookieStore = context.CurrentCookie;
				clientContext.setAttribute(ClientContext.COOKIE_STORE,
						cookieStore);
			}
		}

		Map<String, Object> result = null;
		try
		{
			result = httpClient
					.execute(httpGet, responseHandler, clientContext);
			String strMsg = (String) result.get("message");
			if (strMsg.equals("OK"))
			{
				bytes = (byte[]) result.get("bytes");
			}
			else
			{
				throw AppException.http(new Exception(strMsg));
			}
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.http(e);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.io(e);
		}
		finally
		{
			httpGet.abort();
		}

		return bytes;
	}

	/**
	 * Http Post 请求
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static byte[] requestHttpPost(AppContext context, String url,
			Map<String, String> params, Map<String, File> files, Charset charset)
			throws AppException
	{
		byte[] bytes = null;
		HttpPost httpPost = null;

		// 创建一个可以包含多个不同部分的实体
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		if (params != null)
		{
			// 文本参数
			for (Map.Entry<String, String> entry : params.entrySet())
			{
				StringBody stringBody = new StringBody(entry.getValue(),
						ContentType.create("text/plain", charset));
				builder.addPart(entry.getKey(), stringBody);
			}
		}

		if (files != null)
		{
			// 文件参数
			for (Map.Entry<String, File> entry : files.entrySet())
			{
				FileBody fileBody = new FileBody(entry.getValue());
				builder.addPart(entry.getKey(), fileBody);
			}
		}

		// 实例化HttpPost请求
		httpPost = new HttpPost(url);
		HttpContext clientContext = new BasicHttpContext();
		boolean hasCookie = false;
		if (context != null)
		{
			// 从文件中读取cookie
			if (cookieType == HoldCookieType.File)
			{
				String cookie = readCookie(context);
				if (!StringUtils.isEmpty(cookie))
				{
					httpPost.setHeader("Cookie", cookie);
					hasCookie = true;
				}
			}
			else
			// 从AppContext 获取cookie
			{
				CookieStore cookieStore = context.CurrentCookie;
				if (cookieStore != null)
				{
					hasCookie = true;
				}
				clientContext.setAttribute(ClientContext.COOKIE_STORE,
						cookieStore);
			}
		}
		httpPost.setEntity(builder.build());

		Map<String, Object> result = null;
		try
		{
			result = httpClient.execute(httpPost, responseHandler,
					clientContext);
			String strMsg = (String) result.get("message");
			if (strMsg.equals("OK"))
			{
				bytes = (byte[]) result.get("bytes");
				CookieStore cookieStore = httpClient.getCookieStore();
				if (cookieStore != null && !hasCookie)
				{
					if (cookieType == HoldCookieType.File)
					{
						List<Cookie> cookies = cookieStore.getCookies();
						StringBuilder sb = new StringBuilder();
						for (Cookie item : cookies)
						{
							sb.append(item.getName() + "=" + item.getValue());
							sb.append(";");
						}
						saveCookie(context, sb.toString());
					}
					else
					{
						context.CurrentCookie = cookieStore;
					}
				}
			}
			else
			{
				throw AppException.http(new Exception(strMsg));
			}
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.http(e);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AppException.io(e);
		}
		finally
		{
			httpPost.abort();
		}

		return bytes;
	}

	private static String readCookie(AppContext context)
	{
		return (String) DataCacheUtils.readObject(context, "cookie.txt");
	}

	private static void saveCookie(AppContext context, String cookie)
	{
		DataCacheUtils.saveObject(context, cookie, "cookie.txt");
	}

	public static void release()
	{
		if (httpClient != null)
		{
			ClientConnectionManager man = httpClient.getConnectionManager();
			if (man != null)
				man.shutdown();
			httpClient = null;
		}
	}

	public static String Byte2Str(byte[] bytes)
	{
		String result = "";
		try
		{
			result = new String(bytes, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
