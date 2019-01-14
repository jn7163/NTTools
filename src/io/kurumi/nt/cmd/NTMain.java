package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.io.*;
import io.kurumi.nt.cmd.entries.*;

public class NTMain extends NTBaseCmd {

    public static void main(String[] args) {

        new NTMain().run();

    }

    private File dataDir;

    private NTContext context;
    private NTUser user;

    private NTMenu menu;

    public void run() {

        try {

            Class.forName("android.os.Build");

            dataDir = new File("/sdcard/AppProjects/NTTools");

        } catch (ClassNotFoundException e) {

            dataDir = new File(".");

        }

        context = new NTContext(dataDir);
        user = context.getDefaultUser();

        printSplitLine();
        println("正在启动NT盒子... (单用户模式)");

        menu = new NTMenu(this);

        ApiManage.apply(user,menu);

        menu.start();

    }

    
}
