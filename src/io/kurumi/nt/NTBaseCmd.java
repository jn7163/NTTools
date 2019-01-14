package io.kurumi.nt;

import java.util.*;

public abstract class NTBaseCmd extends NTBase {
    
    public static Scanner session = new Scanner(System.in);
    
    public static int choose() {

        print("请选择 (输入数字) : ");

        return session.nextInt();

    }

    public static void pause() {

        print("任意内容继续... : ");
        session.next();

    }

    public static boolean confirm() {

        return !"n".equals(input("确定吗 ？ Y/n : ").toLowerCase());

    }

    public static String input() {

        return session.next();

    }

    public static String input(String msg) {

        print("请输入");
        print(msg);
        print(" : ");
        return session.next();

    }

    public static int inputInt(String msg) {

        System.out.print(msg);
        return session.nextInt();

    }
    
}
