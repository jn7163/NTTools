package io.kurumi.nt;

import java.util.*;
import java.io.*;
import cn.hutool.system.*;

public abstract class NTBase {

    public static void print(String msg) {

        System.out.print("  " + msg);

    }

    public static void println(String msg) {

        System.out.println("  " + msg);

    }

    public static void println() {

        System.out.println();

    }

    public static void printSplitLine() {

        System.out.println("------------------------");

    }

    public static void noSuchChoose() {

        printSplitLine();

        println("没有那样的选项");

    }
    
    public static void clear() {
        
        try {
            
            if (SystemUtil.getOsInfo().isLinux()) {

                Runtime.getRuntime().exec("clear").waitFor();

            } else {

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            }
            
        } catch (IOException e) {} catch (InterruptedException e) {}

    }
   
    
}
