package org.eclipse.om2m.core.redirector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class state_action {

	public static void decision(int z) throws HttpException, IOException {
		// TODO Auto-generated method stub
		String url = "http://140.116.247.69:9000/state_post";
		HttpClient httpclient = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);
		StringBuilder sb = new StringBuilder();
		// sb.append(Redirector.datasize);
		sb.append(z);
		sb.append("//");
		sb.append(Redirector.datasize);
		sb.append("//");
		sb.append(ChangeWanem.loss_rate);
		sb.append("//");
		sb.append(ChangeWanem.bandwidth);
		// sb.append(1);
		// sb.append("//");
		// sb.append(1);
		// sb.append("//");
		// sb.append(1);
		StringRequestEntity requestEntity = new StringRequestEntity(sb.toString(),"application/xml", "UTF-8");
		httpMethod.setRequestEntity(requestEntity);
		// int statuscode = httpclient.executeMethod(httpMethod);
		// Redirector.return_protocol=new String(httpMethod.getResponseBody());
	}

}
