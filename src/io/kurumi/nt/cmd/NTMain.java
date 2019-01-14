package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.io.*;
import io.kurumi.nt.cmd.entries.*;

public class NTMain extends NTBaseCmd {

    public static void main(String[] args) {
     
        File dataDir = new File("/sdcard/AppProjects/NTTools");
        
        try {

            Class.forName("android.os.Build");

        } catch (ClassNotFoundException e) {

            dataDir = new File(".");

        }

        NTContext context = new NTContext(dataDir);
        NTUser user = context.getDefaultUser();

        printSplitLine();
        println("正在启动NT盒子... (单用户模式)");

        NTMenu menu = new NTMenu(true);

        ApiManage.apply(user, menu);

        menu.start();

    }

    
}
