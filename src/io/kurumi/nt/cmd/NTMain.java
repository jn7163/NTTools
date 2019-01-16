package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.entries.*;
import java.io.*;

public class NTMain extends NTBaseCmd {

    public static void main(String[] args) {

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
                
            }

        };



        menu.start();

    }


}
