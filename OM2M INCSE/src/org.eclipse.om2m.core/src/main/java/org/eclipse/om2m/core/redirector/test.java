package org.eclipse.om2m.core.redirector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {
	
	static int throughput=0;
	
	private static String mqttcmd="tcp port 1883 and not ether src 00:0c:29:be:33:56";
	
	private static String allcmd="(tcp port 1883 and not ether src 00:0c:29:be:33:56) or (tcp port 2014 and not ether src 00:0c:29:be:33:56) or (udp port 5683 and not ether src 00:0c:29:be:33:56) or (tcp port 5222 and not ether src 00:0c:29:be:33:56)";

    public static void main(String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("tshark", "-l", "-i", "ens38", "-f", mqttcmd);
		Process process = pb.start();

		BufferedReader br = null;
		    //tried different numbers for BufferedReader's last parameter
		    br = new BufferedReader(new InputStreamReader(process.getInputStream()), 1);
		    String line = null;
		    while ((line = br.readLine()) != null) {
//line = line.trim().replace(" ","@");
//1@0.000000000@192.168.101.136@→@192.168.101.134@MQTT@1725@Publish@Message@(id=3086)@[nscl/applications/NA/monitor]
		    	String[] data = line.trim().replace(" ","@").split("@");
//		    	System.out.println(data[6]);
		    	throughput+=Integer.parseInt(data[6]);
		    	System.out.println(throughput);
		    	throughput=0;
//		    	total_throughput+=throughput;
		    }
    }

}
