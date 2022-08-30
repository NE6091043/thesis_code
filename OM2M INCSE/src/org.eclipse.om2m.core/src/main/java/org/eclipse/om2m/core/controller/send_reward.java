package org.eclipse.om2m.core.controller;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class send_reward {
    public static void send() {
    	new Thread() {
    		public void run() {
    			for(;;) {
    				if(APocController.flag==true) {
                		try {
                			if(APocController.time<990) {
                				String url = "http://140.116.247.69:9000/receive_reward";
        						HttpClient httpclient = new HttpClient();
        						PostMethod httpMethod = new PostMethod(url);
        						StringBuilder sb = new StringBuilder();
        						sb.append(APocController.idx);
        						sb.append("//");
        						sb.append(APocController.time);
        						sb.append("//");
        						sb.append(APocController.eff1);
        						StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(),"application/xml", "UTF-8");
        						httpMethod.setRequestEntity(requestEntity);
        						int statuscode = httpclient.executeMethod(httpMethod);
                			}
    						
                		} catch (HttpException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		} catch (IOException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
    				}
    				APocController.flag=false;
            		try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }
}
