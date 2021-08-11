package edu.wm.cs.muse;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	public static void main(String[] args) {
		Result museTestResult = JUnitCore.runClasses(MuseTest.class);
	    for (Failure failure : museTestResult.getFailures()) {
	      System.out.println(failure.toString());
	    }
	  }

}
