package org.eclipse.om2m.core.notifier;

import java.time.LocalTime;

public class test_nscl_time {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long m=0;
		while(m!=25) {
			LocalTime now = LocalTime.now();
			m=now.getMinute();
		}
		int x=30;
		while(x-->0)
			System.out.println(System.currentTimeMillis());
	}
}
