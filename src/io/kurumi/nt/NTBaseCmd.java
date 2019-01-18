package io.kurumi.nt;

import java.util.*;

public abstract class NTBaseCmd extends NTBase {

    public static Scanner session = new Scanner(System.in);

    public static int choose() {

        print("请选择 (输入数字) : ");

        try {

            return session.nextInt();

        } catch (Exception ex) {

            return -1;

        }

    }

    public static void pause() {

        print("任意内容继续... : ");
        session.next();

    }

    public static boolean confirm() {

        return confirm("确定吗 ？");

    }

    public static boolean confirm(String msg) {

        System.out.print(msg + " y/N");
        
        return "y".equals(input().toLowerCase());

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
    
    public static String inputLine(String msg) {

        System.out.print(msg);

        try {

            return session.nextLine();

        } catch (Exception exc) {

            return "";

        }

    }
    
    
    public static long inputLong(String msg) {

        System.out.print(msg);

        try {

            return session.nextLong();

        } catch (Exception exc) {

            return -1L;

        }
        
        }
    

    public static int inputInt(String msg) {

        System.out.print(msg);

        try {

            return session.nextInt();

        } catch (Exception exc) {

            return -1;

        }

    }

}
