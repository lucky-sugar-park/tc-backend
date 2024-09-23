package com.mymes.equip.vplc.utils;

import java.util.Date;

public class TimerUtil {

	public static void waitFor(long waitingTime) {
		long before = new Date().getTime();
		
		while(new Date().getTime() - before < waitingTime);
	}

	public static void waitFor2(long waitingTime) {
		try {
			Thread.sleep(waitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}