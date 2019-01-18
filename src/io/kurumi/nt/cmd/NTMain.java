package io.kurumi.nt.cmd;

import android.os.*;
import io.kurumi.nt.*;
import io.kurumi.nt.cmd.entries.*;
import io.kurumi.nt.dm.*;
import java.io.*;

public class NTMain extends NTBaseCmd {

    
    public static String clientName = "TwitterAndroid";
    public static String versionName = "6.41.0";
    public static String apiVersion = "5";
    public static String internalVersionName = "7160062-r-930";

    public static String model = Build.MODEL;
    public static String  manufacturer = Build.MANUFACTURER;
    public static String  sdkRelease = Build.VERSION.RELEASE;
    public static String  brand = Build.BRAND;
    public static String  product = Build.PRODUCT;


    private static String androidUA = clientName + "/" + versionName + " (" + internalVersionName + ") " + model + "/" + sdkRelease +" (" + manufacturer+";" + model + ";" + brand + ";" + product + ";0;;0)";
    
    
    public static void main(String[] args) {

        print(androidUA);
        
        File dataDir = new File("/sdcard/AppProjects/NTTools");

        try {

            Class.forName("android.os.Build");

        } catch (ClassNotFoundException ex) {  

            dataDir = new File(".");

        }

        NTContext context = new NTContext(dataDir);
        final NTUser user = context.getDefaultUser();

        printSplitLine();
        println("正在启动NT盒子... (单用户模式)");

        final NTMenu menu = new NTMenu(true);

        menu.init = new Runnable() {

            @Override
            public void run() {
                
                ApiManage.apply(user, menu);
                UserManage.apply(user, menu);
                TaskManage.apply(user,menu);
                BackUpManage.apply(user,menu);
                
                DirectMain.apply(user,menu);
                
               Test.apply(user,menu);
                
            }

        };



        menu.start();

    }


}
