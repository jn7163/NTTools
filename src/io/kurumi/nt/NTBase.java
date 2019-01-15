package io.kurumi.nt;

import java.util.*;
import java.io.*;
import cn.hutool.system.*;

public abstract class NTBase {

    public static void print(String msg) {

        System.out.print(msg);

    }

    public static void println(String msg) {

        System.out.println(msg);

    }

    public static void println() {

        System.out.println();

    }

    public static void printSplitLine() {

        println("------------------------");

    }

    public static void noSuchChoose() {

        printSplitLine();

        println("没有那样的选项");

    }

    public static void clear() {

        try {

            try {

                Class.forName("com.aide.ui.AIDEApplication");

                return;

            } catch (ClassNotFoundException e) {

            }

            if (SystemUtil.getOsInfo().isLinux()) {

                System.out.print("\033[H\033[2J");
                System.out.flush();

            } else {

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            }

        } catch (Exception e) {
            
            e.printStackTrace();
            
        }

    }


}
