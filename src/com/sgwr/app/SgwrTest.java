package com.sgwr.app;

import com.sgwr.app.utils.MultiMailSender;
import com.sgwr.app.utils.MultiMailSender.MultiMailSenderInfo;
import android.test.AndroidTestCase;

public class SgwrTest extends AndroidTestCase {
	private final static String TAG = "SgwrTest";
	final static String url = "http://192.168.1.113:4939/Handler/";

	static String strUrl = "http://192.168.1.113:4939/Hand1ler/UserHandler.ashx";

	public void testLogin()
	{
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("username", "admin");
//		params.put("password", "1957");
//		params.put("type", "login");

		//这个类主要是设置邮件
	      MultiMailSenderInfo mailInfo = new MultiMailSenderInfo(); 
	      mailInfo.setMailServerHost("smtp.126.com"); 
	      mailInfo.setMailServerPort("25"); 
	      mailInfo.setValidate(true); 
	      mailInfo.setUserName("xxx@126.com"); 
	      mailInfo.setPassword("xx");//您的邮箱密码 
	      mailInfo.setFromAddress("xxx@126.com"); 
	      mailInfo.setToAddress("xx@xx.com"); 
	      mailInfo.setSubject("设置邮箱标题"); 
	      mailInfo.setContent("设置邮箱内容");	      
	      //这个类主要来发送邮件 
	      MultiMailSender sms = new MultiMailSender(); 
	      sms.sendTextMail(mailInfo);//发送文体格式	       
	      
		
	}

	// public void testLogin()
	// {
	// try
	// {
	// UserInfo user = UserService.login(null, "admin", "1957");
	// Log.i(TAG, user.UserName);
	// }
	// catch (AppException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// public void testNotify()
	// {
	// List<IncidentNotifyInfo> list = new ArrayList<IncidentNotifyInfo>();
	// try
	// {
	// list = IncidentService.getIncidentNotifies(null, "122547");
	// }
	// catch (AppException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// if (!list.isEmpty())
	// {
	// Date date = list.get(0).CreatedTime;
	//
	// String localDate1 = DateTimeUtils.getLocalDateTime(date);
	// String localDate2 = DateTimeUtils.Utc2Local(date,
	// AppConfig.DATE_TIME_PATTEN);
	//
	// Log.i("date1", date.toLocaleString());
	// Log.i("date2", localDate1);
	// Log.i("date3", localDate2);
	//
	// }
	// }
}
