package org.eclipse.om2m.core.notifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class test_gscl_time {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
//		long m=0;
//		while(m!=25) {
//			LocalTime now = LocalTime.now();
//			m=now.getMinute();
//		}
//		int x=30;
//		while(x-->0)
//			System.out.println(System.currentTimeMillis());
		//  date -d "$(curl -s --head http://google.com | grep ^Date: | sed 's/Date: //g')" +%s
		//String cmd= "date -d " + '"' + "$(curl -s --head http://google.com | grep ^Date: | sed 's/Date: //g')" + '"' + " +%s";
		String cmd="sh /home/user/time.sh";
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec(cmd);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		line+=buf.readLine();
		long time = System.currentTimeMillis();
		long googletime1=Long.parseLong(line);
		TimeUnit.MILLISECONDS.sleep(350);
		Process pr2 = run.exec(cmd);
		pr2.waitFor();
		BufferedReader buf2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
		String line2 = "";
		line2+=buf2.readLine();
		long googletime2=Long.parseLong(line2);
		System.out.println(googletime1);
		System.out.println(googletime2);
		System.out.println(googletime2-googletime1);
//		System.out.println(time);
	}
}
