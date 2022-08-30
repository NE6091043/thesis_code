import java.io.ByteArrayInputStream;



import java.io.IOException;



import java.io.InputStream;



import java.io.UnsupportedEncodingException;



import java.util.ArrayList;



import java.util.List;



import java.util.Random;



import java.util.regex.Matcher;



import java.util.regex.Pattern;







import javax.xml.bind.DatatypeConverter;



import javax.xml.parsers.DocumentBuilder;



import javax.xml.parsers.DocumentBuilderFactory;



import javax.xml.parsers.ParserConfigurationException;







import org.apache.commons.httpclient.HttpClient;



import org.apache.commons.httpclient.HttpException;



import org.apache.commons.httpclient.methods.DeleteMethod;



import org.apache.commons.httpclient.methods.GetMethod;



import org.apache.commons.httpclient.methods.PostMethod;



import org.apache.commons.httpclient.methods.StringRequestEntity;



import org.w3c.dom.Document;



import org.xml.sax.InputSource;



import org.xml.sax.SAXException;







import com.sun.net.httpserver.HttpExchange;



import com.sun.net.httpserver.HttpHandler;







// 這是 network application 的測試程式







public class TestNAForQueryDriven {







	//private static String Local_IP = "192.168.72.109";



	private static String Local_IP = "192.168.72.9";



	private static int Local_Port = 1400;



//	private static String context = "/monitor";





	private static String INCSE_IP = "140.116.247.69";



	private static String INCSE_Port = "18080";







	private static String app = "NA";



//	private static String container = "DATA";



//	private static int x=1;



//	private static int n;



	private static int sleeptime=1000;



	private static int DataSize=100;



	private static int iLoopCount=500;



	



	static int plot=1;







	public static void main(String[] args) throws HttpException, IOException {







		



		TestNAForQueryDriven na = new TestNAForQueryDriven();



		



		



		



		na.run(iLoopCount);



		



		



	}



	



	



	public static void shutdown() throws HttpException, IOException{















		String url = "http://140.116.247.69:9000/shutdown";















		HttpClient httpclient = new HttpClient();















		PostMethod httpMethod = new PostMethod(url);















		StringBuilder sb = new StringBuilder();







		







		sb.append(plot);















		StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(),"application/xml", "UTF-8");















		httpMethod.setRequestEntity(requestEntity);















		int statuscode = httpclient.executeMethod(httpMethod);







		







		String str=new String(httpMethod.getResponseBody());







		







		System.out.println(str);







		







	}



	



	



	private void start() throws HttpException, IOException {



		



		String url = "http://140.116.247.69:9000/start";







		HttpClient httpclient = new HttpClient();







		PostMethod httpMethod = new PostMethod(url);







		StringBuilder sb = new StringBuilder();







		StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(),"application/xml", "UTF-8");







		httpMethod.setRequestEntity(requestEntity);







		int statuscode = httpclient.executeMethod(httpMethod);



		



		String str=new String(httpMethod.getResponseBody());



		



		System.out.println(str);



	}







	private void run(Integer iLoopCount) throws HttpException, IOException {







		unregisterToINCSE();







		registerToINCSE();







		// 3. 搜尋資源(discovery)



		List<String> listReferenceURI = discoverResource("reader"); // ex:







		List<String> listLink = getLink(listReferenceURI); // ex:







//		System.out.println("listLink = " + listLink);







		GetDescriptor(listLink);







		// 設定 wanem的packet loss rate



		//statusCode = Chan	geWanem.start(iPacketLossRate);



		



		delay(1000);



		



		







		boolean isStarted = startPingOnINCSE("incse/applications/Ping/process/start");



//				boolean isStarted = true;



		if (isStarted) {



			delay(1000);



			



			// 5. 送 command 給 device



			



//			start();



			



			Random rand=new Random();



			

			for (int i = 1; i <= iLoopCount; i++) {

//			for (int i = 1; i <= iLoopCount+1; i++) {

//				if(i==iLoopCount+1) {



//



//					try {



//



//						shutdown();



//



//					} catch (IOException e) {



//



//						// TODO Auto-generated catch block



//



//						e.printStackTrace();



//



//					}



//



//					break;



//



//				}



				System.out.println("i = " + i);



				



//				int x=rand.nextInt(2);

//

//		

//

//

//

//				if(1<=i && i<=200) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(200<i && i<=400) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(400<i && i<=800) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=1500;

//

//

//

//					}

//

//

//

//				}else if(800<i && i<=1000) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=1000;

//

//

//

//					}

//

//

//

//				}else if(3000<i && i<=3200) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=1500;

//

//

//

//					}

//

//

//

//				}else if(3200<i && i<=3400) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(3400<i && i<=3800) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(3800<i && i<=4000) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1500;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(1000<i && i<=1200) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(1200<i && i<=1400) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(1400<i && i<=1800) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=1500;

//

//

//

//					}

//

//

//

//				}else if(1800<i && i<=2000) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(4000<i && i<=4200) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=500;

//

//

//

//					}else {

//

//

//

//						DataSize=1000;

//

//

//

//					}

//

//

//

//				}else if(4200<i && i<=4400) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=1000;

//

//

//

//					}

//

//

//

//				}else if(4400<i && i<=4800) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=1500;

//

//

//

//					}

//

//

//

//				}else if(4800<i && i<=5000) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(2000<i && i<=2200) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(2200<i && i<=2400) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}else if(2400<i && i<=2800) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=1000;

//

//

//

//					}else {

//

//

//

//						DataSize=1500;

//

//

//

//					}

//

//

//

//				}else if(2800<i && i<=3000) {

//

//

//

//					if(x==1) {

//

//

//

//						DataSize=100;

//

//

//

//					}else {

//

//

//

//						DataSize=500;

//

//

//

//					}

//

//

//

//				}



				



				



				if(1<=i && i<=125) {

					

					DataSize=1500;



				}else if(125<i && i<=250) {



					DataSize=500;



				}else if(250<i && i<=375) {



					DataSize=1000;



				}else if(375<i && i<=500) {



					DataSize=100;

				}



				



				String cmd = "mncse/applications/D2_Reader/readers/savecards";



				sendCmdToDevice(cmd, DataSize, i);



//							System.out.println();







				try {



					Thread.sleep(sleeptime);



				} catch (InterruptedException e) {



					e.printStackTrace();



				}



			}



		



			



			







			// statusCode = ChangeWanem.start(0);



			// System.out.println("ChangeWanem(0) = " + statusCode);





		}



		



		



	}











	private static void delay(int i) {



		try {



			Thread.sleep(i);



		} catch (InterruptedException e) {



			e.printStackTrace();



		}



	}



	



	private boolean startPingOnINCSE(String cmd) {



		try {





			String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/" + cmd;



			HttpClient httpclient = new HttpClient();



			PostMethod httpMethod = new PostMethod(url);



			httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");







			httpclient.executeMethod(httpMethod);



			System.out.println("******************************\n");



			return true;



		} catch (UnsupportedEncodingException e) {



			e.printStackTrace();



		} catch (IOException e) {



			e.printStackTrace();



		}



		return false;



	}







	private void sendCmdToDevice(String cmd, int test_data_length, int i) {



		try {



			// 送 command 給 device





			String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/" + cmd;



			HttpClient httpclient = new HttpClient();



			PostMethod httpMethod = new PostMethod(url);



			httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");



			



			String val_ = "";



			if (i < 10) {



				val_ = "00" + Integer.toString(i, 10);



			} else if (i < 100) {



				val_ = "0" + Integer.toString(i, 10);



			} else {



				val_ = Integer.toString(i, 10);



			}



			val_ += str_generator(test_data_length);



			



			StringBuilder sb = new StringBuilder();



			sb.append("<obj>");



			sb.append("<str name='data' val='" + val_ + "'/>");



			sb.append("<str name='index' val='" + i + "'/>");



			sb.append("<str name='timestamp' val='" + System.currentTimeMillis() +"'/>");



			sb.append("<str name='coredelay' val='" + 0.0 +"'/>");



			sb.append("<str name='efficiency' val='" + 0.0 +"'/>");



			sb.append("</obj>");







			StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(), "application/xml", "UTF-8");



			httpMethod.setRequestEntity(requestEntity);







			httpclient.executeMethod(httpMethod);



			







		} catch (UnsupportedEncodingException e) {



			e.printStackTrace();



		} catch (IOException e) {



			e.printStackTrace();



		}







	}







	private void unregisterToINCSE() {



		System.out.println("unregisterToINCSE...");



		try {



			String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/incse/applications/NA";



			HttpClient httpclient = new HttpClient();



			DeleteMethod httpMethod = new DeleteMethod(url);



			httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");



			httpclient.executeMethod(httpMethod);



		} catch (Exception e) {



			e.printStackTrace();



		}



	}







	private void GetDescriptor(List<String> listLink) {







		for (String strLink : listLink) {



			try {



				String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/" + strLink + "/containers/DESCRIPTOR/contentInstances/latest/content";



				HttpClient httpclient = new HttpClient();



				GetMethod httpMethod = new GetMethod(url);



				httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");







				







				httpclient.executeMethod(httpMethod);



//				String strResponseBody = httpMethod.getResponseBodyAsString();



//				System.out.println(strResponseBody);







			} catch (UnsupportedEncodingException e) {



				e.printStackTrace();



			} catch (IOException e) {



				e.printStackTrace();



			}



		}



	}







	private List<String> getLink(List<String> listReference) {



		List<String> listLink = new ArrayList<String>();



		for (String strReference : listReference) {



			try {



				String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/" + strReference;



				HttpClient httpclient = new HttpClient();



				GetMethod httpMethod = new GetMethod(url);



				httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");



				httpclient.executeMethod(httpMethod);



				String strResponseBody = httpMethod.getResponseBodyAsString();



//				System.out.println(strResponseBody);







				// get link of resource



				String patternStr = "<om2m:link>(.+)</om2m:link>";



				Pattern pattern = Pattern.compile(patternStr);



				Matcher matcher = pattern.matcher(strResponseBody);



				if (matcher.find()) {



					String strTargetID = matcher.group(1);



					listLink.add(strTargetID);



				}







			} catch (UnsupportedEncodingException e) {



				e.printStackTrace();



			} catch (IOException e) {



				e.printStackTrace();



			}



		}



		return listLink;



	}







	private List<String> discoverResource(String strResourceType) {



		List<String> listReferenceURI = new ArrayList<String>();



		try {



			String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/incse/discovery?searchString=ResourceType/" + strResourceType;



			HttpClient httpclient = new HttpClient();



			GetMethod httpMethod = new GetMethod(url);



			httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");



			httpclient.executeMethod(httpMethod);



			String strResponseBody = httpMethod.getResponseBodyAsString();



			// System.out.println(strResponseBody);







			// get reference uri of resource



			String patternStr = "<reference>(.+)</reference>";



			Pattern pattern = Pattern.compile(patternStr);



			Matcher matcher = pattern.matcher(strResponseBody);



			while (matcher.find()) {



				String strReferenceURI = matcher.group(1);



				listReferenceURI.add(strReferenceURI);



			}







		} catch (UnsupportedEncodingException e) {



			e.printStackTrace();



		} catch (IOException e) {



			e.printStackTrace();



		}







		return listReferenceURI;



	}











	private void registerToINCSE() {







		try {



			// Create NA application



			String url = "http://" + INCSE_IP + ":" + INCSE_Port + "/om2m/incse/applications";



			HttpClient httpclient = new HttpClient();



			PostMethod httpMethod = new PostMethod(url);



			httpMethod.addRequestHeader("Authorization", "Basic YWRtaW46YWRtaW4");







			StringBuilder sb = new StringBuilder();



			sb.append("<om2m:application xmlns:om2m='http://uri.etsi.org/m2m' appId='" + app + "'>");



			sb.append("<om2m:aPoCPaths>");



			sb.append("<om2m:aPoCPath>");



			sb.append("<om2m:path>http://" + Local_IP + ":" + Local_Port + "</om2m:path>");



			sb.append("</om2m:aPoCPath>");



			sb.append("</om2m:aPoCPaths>");



			sb.append("</om2m:application>");







			StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(), "application/xml", "UTF-8");



			httpMethod.setRequestEntity(requestEntity);







			httpclient.executeMethod(httpMethod);



			







		} catch (UnsupportedEncodingException e) {



			e.printStackTrace();



		} catch (IOException e) {



			e.printStackTrace();



		}



	}







//	/**



//	 * 產生指定大小的資料量



//	 */



	static String str_generator(int size) {



		StringBuffer out = new StringBuffer();



		String strChars = ascii_letters() + digits();



		for (int i = 0; i < size - 3; i++) {



			int idx = (int) (Math.random() * strChars.length());



			String str = strChars.substring(idx, idx + 1);



			out.append(str);



		}



		return out.toString();



	}



	



	



	//2021.12.9



	static String foo(int size) {



	    







	    // String strChars = ascii_letters() + digits();



	    // mod



	    // 2021.11.14



	    // java char size == 2 bytes



	    



	    // mod



	    // 2021.12.9



		// StringBuffer out = new StringBuffer();



		// java char size == 2 bytes



		  



	    String res="";



	    



	    for (int i = 0; i < size; ++i) {



	      res+='a';



	    }



	    return res;



	  }



	



	static String ascii_letters() {



		StringBuffer out = new StringBuffer();



		for (char c = 'a'; c <= 'z'; c++) {



			out.append(c);



		}







		for (char c = 'A'; c <= 'Z'; c++) {



			out.append(c);



		}







		return out.toString();



	}







	static String digits() {



		StringBuffer out = new StringBuffer();



		for (int c = 0; c <= 9; c++) {



			out.append(c);



		}



		return out.toString();



	}







	static class MyHandler implements HttpHandler {



		public void handle(HttpExchange t) throws IOException {







			String body = "";



			int i;



			char c;



			try {



				InputStream is = t.getRequestBody();







				while ((i = is.read()) != -1) {



					c = (char) i;



					body = (String) (body + c);



				}



			} catch (Exception e) {



				e.printStackTrace();



			}



			System.out.println("Received notification:");



//			System.out.println(body);







			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();



			try {



				DocumentBuilder dBuilder = dbf.newDocumentBuilder();







				Document notifyDoc = dBuilder.parse(new InputSource(new ByteArrayInputStream(body.getBytes("utf-8"))));







				String contentInstance64 = notifyDoc.getElementsByTagName("om2m:representation").item(0).getTextContent();



//				System.out.println("ContentInstance (Base64-encoded):\n" + contentInstance64 + "\n");







				String contentInstance = new String(DatatypeConverter.parseBase64Binary(contentInstance64));



//				System.out.println("ContentInstance:\n" + contentInstance + "\n");







				Document instanceDoc = dBuilder.parse(new InputSource(new ByteArrayInputStream(contentInstance.getBytes("utf-8"))));



				String content64 = instanceDoc.getElementsByTagName("om2m:content").item(0).getTextContent();



//				System.out.println("Content (Base64-encoded):\n" + content64 + "\n");







				final String content = new String(DatatypeConverter.parseBase64Binary(content64));



//				System.out.println("Content:\n" + content + "\n");







				t.sendResponseHeaders(204, -1);







			} catch (ParserConfigurationException e) {



				e.printStackTrace();



				t.sendResponseHeaders(501, -1);



			} catch (SAXException e) {



				e.printStackTrace();



				t.sendResponseHeaders(501, -1);



			} catch (IOException e) {



				e.printStackTrace();



				t.sendResponseHeaders(501, -1);



			}







		}



	}



}



