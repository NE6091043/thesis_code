package org.eclipse.om2m.xmpp.openfire;

import java.io.File;

public class test {

	public static void main(String[] args) {
		
		File configFile = new File(System.getProperty("user.home")+"/Desktop/XMPP");
		System.out.println("Using XMPP config file: " + configFile.getAbsolutePath());

	}

}
