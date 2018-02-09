package org.celllife.stockout.app;

import android.app.Application;
import android.util.Log;

public class StockApplication extends Application {

    private static StockApplication instance;
    
    public static StockApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
      super.onCreate();
      instance = this;
      Log.i("StockApplication", "Created Stock application");
      printHardwareInfo();
    }

    private void printHardwareInfo() {
        Log.i("StockApplication", 
            "Android Device: "
                +" sdk:" + android.os.Build.VERSION.SDK_INT 
                + " release:"+ android.os.Build.VERSION.RELEASE
                + " incremental:"+android.os.Build.VERSION.INCREMENTAL
                + " Hardware:" 
                + " manufacturer: " + android.os.Build.MANUFACTURER 
                + " brand: " + android.os.Build.BRAND
                + " hardware: " + android.os.Build.HARDWARE 
                + " device: " + android.os.Build.DEVICE 
                + " model: " + android.os.Build.MODEL
                + " product: " + android.os.Build.PRODUCT);
      }
}
