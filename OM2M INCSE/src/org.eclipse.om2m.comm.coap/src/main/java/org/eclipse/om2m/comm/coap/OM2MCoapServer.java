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
 *     Khalil Drira - Management and initial specification.
 *     Samir Medjiah - Conception and implementation of the CoAP binding.
 *     Rim Frikha - Implementation of the CoAP binding.
 ******************************************************************************/

package org.eclipse.om2m.comm.coap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.californium.core.coap.BlockOption;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.MessageDeliverer;
import org.eclipse.om2m.commons.resource.StatusCode;
import org.eclipse.om2m.commons.rest.RequestIndication;
import org.eclipse.om2m.commons.rest.ResponseConfirm;
import org.eclipse.om2m.core.service.SclService;

//import ch.ethz.inf.vs.californium.coap.CoAP;
//import ch.ethz.inf.vs.californium.coap.OptionSet;
//import ch.ethz.inf.vs.californium.coap.Request;
//import ch.ethz.inf.vs.californium.coap.Response;
//import ch.ethz.inf.vs.californium.coap.CoAP.ResponseCode;
//import ch.ethz.inf.vs.californium.network.Exchange;
//import ch.ethz.inf.vs.californium.server.MessageDeliverer;
//import ch.ethz.inf.vs.californium.server.Server;
import org.eclipse.californium.core.CoapServer;
import org.json.JSONException;
import org.json.JSONObject;

public class OM2MCoapServer {

	private CoapServer server;
	private CoapMessageDeliverer msgDeliverer;
	private static int port = Integer.parseInt(System.getProperty("org.eclipse.om2m.coap.port", "5683"));

	/*
	 * Application entry point.
	 */

	public void startServer() throws Exception {

		server = new CoapServer(port);
		msgDeliverer = new CoapMessageDeliverer();
		server.setMessageDeliverer(msgDeliverer);

		server.start();

	}

	public void stopServer() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
}

class CoapMessageDeliverer implements MessageDeliverer {

	private static Log LOGGER = LogFactory.getLog(CoapMessageDeliverer.class);

	private static SclService scl;
	private static String context = System.getProperty("org.eclipse.om2m.sclBaseContext", "/om2m");
	Request req;
	Response resp;

	@Override
	public void deliverRequest(Exchange exchange) {

		req = exchange.getRequest();

		try {
			resp = service(req);
			LOGGER.info("the response= " + resp);
		} catch (SocketException e) {
			LOGGER.error("the service failed! ", e);
		} catch (IOException e) {
			LOGGER.error("IOexception", e);
		}

		LOGGER.info("request = " + req);
		exchange.sendResponse(resp);

	}

	@Override
	public void deliverResponse(Exchange rqst, Response rspns) {
		rqst.sendResponse(rspns);
		LOGGER.info("response= " + rspns);

	}

	/**
	 * Converts a {@link CoapServerRequest} to a {@link RequestIndication} and
	 * uses it to invoke the SCL service. Converts the received
	 * {@link ResponseConfirm} to a {@link CoapServerResponse} and returns it
	 * back.
	 */

	public Response service(Request request) throws SocketException, IOException {
		if (isJson(request.getPayloadString())) {
			try {
				calculateDelay(request.getPayloadString());
				recordToken(request.getTokenString(), request.getPayloadString());

				// creation of the CoAP Response
				int mid = request.getMID();
				byte[] token = request.getToken();
				OptionSet options = request.getOptions();
				
				Response response = new Response(ResponseCode.CHANGED);

				response.setMID(mid);
				// request.getOptions().setContentFormat(MediaTypeRegistry.TEXT_XML);
				if (options != null) {
					//options.setBlock2(BlockOption.size2Szx(1024), false, 0);
					response.setOptions(options);					
				}
				
				if (!(token == null)) {
					response.setToken(token);
				}
				if (request.getType().equals(CoAP.Type.CON)) {
					CoAP.Type coapType = CoAP.Type.ACK;
					response.setType(coapType);

				}
				LOGGER.info("CoAP Response parameters set");

				return response;

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// store the MId and the Token
			int mid = request.getMID();
			byte[] token = request.getToken();
			/** Construct a requestIndication Object from the coap request */
			RequestIndication requestIndication = new RequestIndication();
			ResponseConfirm responseConfirm = new ResponseConfirm();

			OptionSet options = request.getOptions();
			URI uri = null;
			try {
				uri = new URI(request.getURI());
			} catch (URISyntaxException e) {
				LOGGER.error("URI Syntax error", e);
			}
			String targetID = "";
			if (uri.getPath().split(context + "/")[1] != null) {
				targetID = uri.getPath().split(context)[1];
			}

			String base = "";
			String representation = request.getPayloadString();

			requestIndication.setBase(base);
			requestIndication.setTargetID(targetID);
			requestIndication.setRepresentation(representation);

			// Get the method out of the code of CoAP
			CoAP.Code code = request.getCode();
			String coapMethod = null;
			switch (code) {
			case GET:
				coapMethod = "RETRIEVE";
				break;
			case POST:
				if (request.getPayloadString().isEmpty()) {
					coapMethod = "EXECUTE";
				} else {
					coapMethod = "CREATE";
				}
				break;
			case PUT:
				coapMethod = "UPDATE";
				break;
			case DELETE:
				coapMethod = "DELETE";
				break;
			}
			// Get the Method
			requestIndication.setMethod(coapMethod);

			// Get the request parameters
			String queryString = options.getUriQueryString();

			// add by junhao
			System.out.println("CoAP queryString = " + queryString);
			System.out.println("CoAP queryString = " + queryString);
			System.out.println("CoAP queryString = " + queryString);
			System.out.println("CoAP queryString = " + queryString);
			System.out.println("CoAP queryString = " + queryString);

			Map<String, List<String>> parameters = getParamsFromQuery(queryString);
			if (options.getUriQueryString() != null) {
				requestIndication.setParameters(parameters);
			}
			// set the options from the requestIndication parameters
			if (parameters.containsKey("Authorization")) {
				requestIndication.setRequestingEntity(parameters.get("Authorization").get(0));
			}

			if (parameters.containsKey("authorization")) {
				requestIndication.setRequestingEntity(parameters.get("authorization").get(0));
			}

			// sending the standard request to the Scl and getting a standard
			// response
			if (scl != null) {
				responseConfirm = scl.doRequest(requestIndication);
				LOGGER.info("check point: requestIndication sent and waiting for responseConfirm");
			} else {
				responseConfirm = new ResponseConfirm(StatusCode.STATUS_SERVICE_UNAVAILABLE, "SCL service not installed");
			}
			boolean isEmptyResponse = false;

			// check if we have a payload
			if (responseConfirm.getRepresentation() == null || responseConfirm.getRepresentation().isEmpty()) {
				isEmptyResponse = true;
			}

			double statusCode = getCoapStatusCode(responseConfirm.getStatusCode(), isEmptyResponse);
			LOGGER.info("check point : The code is " + statusCode);

			/**
			 * transformation of the code to create the CoAP response to be
			 * returned
			 */
			ResponseCode resCode = ResponseCode.VALID;

			switch (responseConfirm.getStatusCode()) {
			case STATUS_OK:
				resCode = ResponseCode.CHANGED;
				break;
			case STATUS_DELETED:
				resCode = ResponseCode.DELETED;
				break;
			case STATUS_BAD_REQUEST:
				resCode = ResponseCode.BAD_REQUEST;
				break;
			case STATUS_CREATED:
				resCode = ResponseCode.CREATED;
				break;
			case STATUS_NOT_FOUND:
				resCode = ResponseCode.NOT_FOUND;
				break;
			case STATUS_FORBIDDEN:
				resCode = ResponseCode.FORBIDDEN;
				break;
			case STATUS_METHOD_NOT_ALLOWED:
				resCode = ResponseCode.METHOD_NOT_ALLOWED;
				break;
			case STATUS_UNSUPPORTED_MEDIA_TYPE:
				resCode = ResponseCode.UNSUPPORTED_CONTENT_FORMAT;
				break;
			case STATUS_PERMISSION_DENIED:
				resCode = ResponseCode.UNAUTHORIZED;
				break;
			case STATUS_INTERNAL_SERVER_ERROR:
				resCode = ResponseCode.INTERNAL_SERVER_ERROR;
				break;
			case STATUS_NOT_IMPLEMENTED:
				resCode = ResponseCode.NOT_IMPLEMENTED;
				break;
			case STATUS_AUTHORIZATION_NOT_ADDED:
				resCode = ResponseCode.UNAUTHORIZED;
				break;
			default:
				resCode = ResponseCode.SERVICE_UNAVAILABLE;
				break;

			}
			LOGGER.info("the responseConfirm: " + responseConfirm);

			// creation of the CoAP Response
			Response response = new Response(resCode);

			if (responseConfirm.getRepresentation() != null) {
				// filling in the fields of the Coap response
				if (statusCode != 2.01) {
					response.setPayload(responseConfirm.getRepresentation());
				}
			}

			response.setMID(mid);
			// request.getOptions().setContentFormat(MediaTypeRegistry.TEXT_XML);
			if (!(token == null)) {
				response.setToken(token);
			}
			if (request.getType().equals(CoAP.Type.CON)) {
				CoAP.Type coapType = CoAP.Type.ACK;
				response.setType(coapType);
			}
			LOGGER.info("CoAP Response parameters set");

			return response;
		}
		return null;
	}

	/**
	 * Converts a standard CoAP query String into a protocol independent
	 * parameters map.
	 * 
	 * @param query
	 *            - standard CoAP query String.
	 * @return protocol independent parameters map.
	 */
	public static Map<String, List<String>> getParamsFromQuery(String query) {
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		if (query != null) {
			String[] pairs = query.split("[&]");
			for (String pair : pairs) {
				String[] param = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					key = param[0];
				}
				if (param.length > 1) {
					value = param[1];
				}
				if (parameters.containsKey(key)) {
					parameters.get(key).add(value);
				} else {
					List<String> values = new ArrayList<String>();
					values.add(value);
					parameters.put(key, values);
				}
			}
		}
		return parameters;
	}

	/**
	 * Converts a {@link StatusCode} object into a standard CoAP status code .
	 * 
	 * @param statusCode
	 *            - protocol-independent status code.
	 * @param isEmptyBody
	 *            - request body existence
	 * @return standard CoAP status code.
	 */
	public static double getCoapStatusCode(StatusCode statusCode, boolean isEmptyBody) {
		LOGGER.info("The received code is " + statusCode);
		switch (statusCode) {
		case STATUS_OK:
			double result;
			if (isEmptyBody) {
				result = 2.04;
			} else {
				result = 2.00;
			}
			return result;
		case STATUS_CREATED:
			return 2.01;
		case STATUS_ACCEPTED:
			return 2.02;

		case STATUS_BAD_REQUEST:
			return 4.00;
		case STATUS_EXPIRED:
			return 4.00;
		case STATUS_NOT_ACCEPTABLE:
			return 4.00;
		case STATUS_REQUEST_TIMEOUT:
			return 4.00;
		case STATUS_CONFLICT:
			return 4.00;
		case STATUS_PERMISSION_DENIED:
			return 4.01;
		case STATUS_AUTHORIZATION_NOT_ADDED:
			return 4.01;
		case STATUS_FORBIDDEN:
			return 4.03;
		case STATUS_NOT_FOUND:
			return 4.04;
		case STATUS_METHOD_NOT_ALLOWED:
			return 4.05;
		case STATUS_UNSUPPORTED_MEDIA_TYPE:
			return 4.15;
		case STATUS_INTERNAL_SERVER_ERROR:
			return 5.00;
		case STATUS_NOT_IMPLEMENTED:
			return 5.01;
		case STATUS_BAD_GATEWAY:
			return 5.02;
		case STATUS_SERVICE_UNAVAILABLE:
			return 5.03;
		case STATUS_GATEWAY_TIMEOUT:
			return 5.04;

		default:
			return 6.00;
		}
	}

	public static SclService getScl() {
		return scl;
	}

	public static void setScl(SclService scl) {
		CoapMessageDeliverer.scl = scl;
	}

	/**
	 * �p�⦬��T����Delay
	 * 
	 * @param strPayload
	 * @param strDataSize
	 * @throws JSONException
	 * @throws IOException
	 */
	private static void calculateDelay(String strPayload) throws JSONException, IOException {

		JSONObject jsonTodevice = new JSONObject(strPayload);

		if (!jsonTodevice.has("data")) {
			throw new JSONException("no data property");
		}

		// ���oDevice�e�X�T���ɪ��ɶ�
		if (jsonTodevice.has("timestamp")) {
			String strDataSize = String.valueOf(jsonTodevice.getString("data").getBytes().length);

			long previous_time = Long.parseLong(jsonTodevice.getString("timestamp"));

			// ���o�ثe�ɶ�
			long current_time = new Date().getTime();

			// �N�ɶ��۴�A��XDelay(ms)
			long result = current_time - previous_time;

			// �N���G�g���r�ɸ�
			FileWriter fw = new FileWriter("coap_received_delay_" + strDataSize + "Bytes.txt", true); // True�h��ܥΪ��[���覡�g���ɮ׭즳���e����
			BufferedWriter bw = new BufferedWriter(fw); // �NBufferedWeiter�PFileWrite���󰵳s��
			bw.write(String.valueOf(result));
			bw.write("\r\n");
			bw.flush();
			bw.close();
		}
	}
	private static void recordToken(String strToken, String strPayload) throws JSONException, IOException {

		JSONObject jsonTodevice = new JSONObject(strPayload);

		if (!jsonTodevice.has("data")) {
			throw new JSONException("no data property");
		}

		if (jsonTodevice.has("timestamp")) {
			String strDataSize = String.valueOf(jsonTodevice.getString("data").getBytes().length);
			
			FileWriter fw = new FileWriter("coap_received_token_" + strDataSize + "Bytes.txt", true);
			BufferedWriter bw = new BufferedWriter(fw); 
			bw.write(strToken);
			bw.write("\r\n");
			bw.flush();
			bw.close();
		}
	}
	

	/**
	 * �P�_�O�_�Ojson���c
	 */
	public static boolean isJson(String value) {
		try {
			new JSONObject(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}
}