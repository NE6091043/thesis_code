/*******************************************************************************
 * Copyright (c) 2013-2015 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Thierry Monteil (Project co-founder) - Management and initial specification,
 *         conception and documentation.
 *     Mahdi Ben Alaya (Project co-founder) - Management and initial specification,
 *         conception, implementation, test and documentation.
 *     Christophe Chassot - Management and initial specification.
 *     Khalil Drira - Management and initial specification.
 *     Yassine Banouar - Initial specification, conception, implementation, test
 *         and documentation.
 ******************************************************************************/
package org.eclipse.om2m.core.comm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.comm.service.RestClientService;
import org.eclipse.om2m.commons.resource.ErrorInfo;
import org.eclipse.om2m.commons.resource.StatusCode;
import org.eclipse.om2m.commons.rest.RequestIndication;
import org.eclipse.om2m.commons.rest.ResponseConfirm;

/**
 * 
 * A generic client that acts as a proxy to forward requests to specific rest
 * clients based on their communication protocol such as HTTP, COAP, etc.
 * 
 * @author <ul>
 *         <li>Mahdi Ben Alaya < ben.alaya@laas.fr > < benalaya.mahdi@gmail.com
 *         ></li>
 *         <li>Yassine Banouar < ybanouar@laas.fr > < yassine.banouar@gmail.fr >
 *         </li>
 *         <li>Marouane El kiasse < melkiasse@laas.fr > < kiasmarouane@gmail.com
 *         ></li>
 *         </ul>
 */
public class RestClient {
	/** Logger */
	private static Log LOGGER = LogFactory.getLog(RestClient.class);
	/**
	 * Contains all discovered specific rest clients that will considered for
	 * sending requests
	 */
	public static Map<String, RestClientService> restClients = new HashMap<String, RestClientService>();

	/**
	 * Selects a specific client (HTTP by default) id available and uses it to
	 * send the request.
	 * 
	 * @param requestIndication
	 *            - The generic request to handle
	 * @return The generic returned response
	 */
	public ResponseConfirm sendRequest(RequestIndication requestIndication) {
		// LOGGER.info("the requestIndication RC: " + requestIndication);

		ResponseConfirm responseConfirm = new ResponseConfirm();
		// Find the appropriate client from the map and send the request
		
		// Display to check the discovered protocols
		String protocol = requestIndication.getBase().split("://")[0];
		System.out.println(protocol + "@RestClient.java");
		System.out.println(protocol + "@RestClient.java");
		System.out.println(protocol + "@RestClient.java");
		System.out.println(protocol + "@RestClient.java");
		System.out.println(protocol + "@RestClient.java");
		
		if (restClients.containsKey(protocol)) {
			try {
				responseConfirm = restClients.get(protocol).sendRequest(requestIndication);
				if (responseConfirm.getStatusCode() == null) {
					throw new Exception();
				}
			} catch (Exception e) {
				LOGGER.error("RestClient error", e);

				// 將錯誤訊息寫到文字檔裡
//				try {
//					FileWriter fw = new FileWriter("test_error_msg.txt", true); // True則表示用附加的方式寫到檔案原有內容之後
//					BufferedWriter bw = new BufferedWriter(fw); // 將BufferedWeiter與FileWrite物件做連結
//					bw.write("RestClient error in RestClient.java");
//					bw.write("\r\n");
//					bw.flush();
//					bw.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}

				responseConfirm = new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_INTERNAL_SERVER_ERROR, "RestClient error"));
			}
		} else {
			System.out.println(protocol+" not found!!");
			System.out.println(protocol+" not found!!");
			System.out.println(protocol+" not found!!");
			responseConfirm = new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_NOT_IMPLEMENTED, "No RestClient service Found"));
		}
//		LOGGER.info(responseConfirm);
		return responseConfirm;
	}

	/**
	 * Gets RestClients
	 * 
	 * @return restClients
	 */
	public static Map<String, RestClientService> getRestClients() {
		return restClients;
	}

	/**
	 * Sets RestClient
	 * 
	 * @param sclClients
	 */
	public static void setRestClients(Map<String, RestClientService> sclClients) {
		RestClient.restClients = sclClients;
	}
}
