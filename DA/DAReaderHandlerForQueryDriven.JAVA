import java.io.IOException;

import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.util.List;

import java.util.regex.Matcher;

import java.util.regex.Pattern;



import org.apache.commons.httpclient.HttpClient;

import org.apache.commons.httpclient.methods.PostMethod;

import org.apache.commons.httpclient.methods.StringRequestEntity;



import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpHandler;



public class ReaderHandlerForQueryDriven implements HttpHandler {



	private static int number = 0;

//	private static int max_idx=-1;

	private static double total_delay=0.0;

	private static double avg_delay=0.0;

	

	static double eff1=0.8, eff2=0.8;



	@Override

	public void handle(HttpExchange t) throws IOException {

		

//		System.out.println("Received information :");

//		System.out.println("----------------------------------------------- :");

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



		// mod

		// 2021.12.9

//		System.out.println("body = " + body);

		// endregion



		++number;



		System.out.println("Received commands:" +number+" times");

		

		if (body.isEmpty()) {

			String strRequestURI = t.getRequestURI().getPath();

			String strRequestContext = strRequestURI.substring(0, strRequestURI.lastIndexOf("/"));

			strRequestURI.substring(strRequestURI.lastIndexOf("/") + 1);

//			System.out.println("strRequestContext = " + strRequestContext);

//			System.out.println("strRequestCmd = " + strRequestCmd);

			if (strRequestContext.equalsIgnoreCase(DeviceForQueryDriven.readerContext)) {

				// setDoorState(strRequestCmd);

			}



			t.sendResponseHeaders(204, -1);

		} else {

			try {



				// regex

				String strTimestamp = "";

//				String strNumber = "";

				String patternStr_1 = "<str name='timestamp' val='(\\d+)'/>";

				String patternStr_2 = "<str name='data' val=";

				Pattern pattern = Pattern.compile(patternStr_1);

				Matcher matcher = pattern.matcher(body);

				

//				String[] tmp=body.split("<str name='index' val='");

//				String idx=tmp[1].split("'/><str name='timestamp'")[0];

				

				

				

				String[] data = body.split("<str name='data' val='");

				String payload = data[1].split("'/>")[0];

				

				String[] tmp=body.split("<str name='index' val='");

				



				String idx=tmp[1].split("'/><str name='timestamp'")[0];



				

				String[] tmp2=body.split("<str name='efficiency' val='");

				



				String efficiency=tmp2[1].split("'/>")[0];

				

				if (matcher.find()) {

					strTimestamp = matcher.group(1);

					

					long previous_time = Long.parseLong(strTimestamp);

					long current_time = System.currentTimeMillis();

					long delay = current_time - previous_time;

					

					eff1=eff2;

					

					eff2=Double.parseDouble(efficiency);





					

					if(eff1>1) {



						eff1=0.2;



					}

					

//					if(delay>50000) {

//						delay=50000;

//					}

					

					int order = Integer.parseInt(idx);

					

					System.out.println("Order: " + order);



					System.out.println("Content Size:" + payload.length());



					// System.out.println("Content:\n" + content + "\n");



					System.out.println("body size = " + body.length());



					System.out.println("time =" + delay);

					

					System.out.println("efficiency =" + eff1);

					

					System.out.println("---------------"+delay);

					

					total_delay+=delay;

					avg_delay=total_delay/number;

					System.out.println("average delay =" + avg_delay);

					

//					String strNumber = body.substring(body.lastIndexOf(patternStr_2) + 22 , body.lastIndexOf(patternStr_2) + 25);

					//System.out.println(strNumber);



									

					

//					double est_lossrate=1-number/max_idx;

//					System.out.println("ssssssssssssssssssssssssssssssssss");

//					System.out.println("ssssssssssssssssssssssssssssssssss");

//					System.out.println("ssssssssssssssssssssssssssssssssss");

//					System.out.println(est_lossrate);

//					System.out.println("ssssssssssssssssssssssssssssssssss");

//					System.out.println("ssssssssssssssssssssssssssssssssss");

//					System.out.println("ssssssssssssssssssssssssssssssssss");

					

					

					if(delay<=0) {

						System.out.println("ssssssssssssssssssssssssssssssssss");

						System.out.println("ssssssssssssssssssssssssssssssssss");

						System.out.println("ssssssssssssssssssssssssssssssssss");

						System.out.println("ssssssssssssssssssssssssssssssssss");

						System.out.println("ssssssssssssssssssssssssssssssssss");

						System.out.println("ssssssssssssssssssssssssssssssssss");

					}

					

//					if(delay<990) {

//					

//						String url = "http://140.116.247.69:9000/receive_reward";

//	

//						HttpClient httpclient = new HttpClient();

//	

//						PostMethod httpMethod = new PostMethod(url);

//	

//						StringBuilder sb = new StringBuilder();

//						

//						

//						sb.append(order);

//						

//						sb.append("//");

//	

//						sb.append(delay);

//						

//						sb.append("//");

//	

//						sb.append(eff1);

//	

//						StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(),"application/xml", "UTF-8");

//	

//						httpMethod.setRequestEntity(requestEntity);

//	

//						httpclient.executeMethod(httpMethod);

//					}

					

					if(delay>10000) {

						eff1=0.0;

					}

					

					int line_idx=Integer.parseInt(idx);

					Path path = Paths.get("test.csv");

					List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

					String s=String.valueOf(order)+","+String.valueOf(delay)+","+String.valueOf(eff1);

					lines.set(order , ","+s);

					Files.write(path, lines, StandardCharsets.UTF_8);

					

					

					System.out.println("-----------------------------------------------");

				}		

				





				t.sendResponseHeaders(204, -1);

			} catch (IOException e) {

				e.printStackTrace();

				t.sendResponseHeaders(501, -1);

			}







		}



	}





}

