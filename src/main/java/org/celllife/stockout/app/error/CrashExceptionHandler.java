package org.celllife.stockout.app.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Build;
import android.util.Log;

public class CrashExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			Log.e("CrashExceptionHandler", "Logging uncaught exception...");
			String errorReport = getErrorReport(thread, ex);
			Log.e("CrashExceptionHandler", errorReport);
		} catch (Throwable t) {
			Log.e("CrashExceptionHandler", "Exception caught while handling exception: ", t);
		} finally {
			Log.e("CrashExceptionHandler", "Exiting app");
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	protected String getErrorReport(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		StringBuilder errorReport = new StringBuilder();
		errorReport.append("************ APPLICATION EXCEPTION ********").append("\n");
		errorReport.append("Thread: ").append(thread).append("\n");
		errorReport.append("************ DEVICE INFORMATION ***********").append("\n");
		errorReport.append("Brand: ").append(Build.BRAND).append("\n");
		errorReport.append("Device: ").append(Build.DEVICE).append("\n");
		errorReport.append("Model: ").append(Build.MODEL).append("\n");
		errorReport.append("Id: ").append(Build.ID).append("\n");
		errorReport.append("Product: ").append(Build.PRODUCT).append("\n");
		errorReport.append("************ FIRMWARE *********************").append("\n");
		errorReport.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n");
		errorReport.append("Release: ").append(Build.VERSION.RELEASE).append("\n");
		errorReport.append("Incremental: ").append(Build.VERSION.INCREMENTAL).append("\n");
		errorReport.append("************ CAUSE OF ERROR ***************").append("\n");
		errorReport.append(stackTrace.toString());
		return errorReport.toString();
	}
}
