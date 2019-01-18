package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.util.*;

public class NTMenu extends NTBaseCmd {

    private boolean isTop = false;

    private LinkedList<Item> items = new LinkedList<>();

    public Runnable init;
    private Runnable msg;

    public NTMenu() {}

    public NTMenu(boolean isTop) {

        this.isTop = isTop;

    }

    public void msg(final String msg) {

        this.msg = new Runnable() {

            @Override
            public void run() {

                println(msg);
                printSplitLine();

                NTMenu.this.msg = null;

            }

        };

    }

    public NTMenu clean() {

        items.clear();

        return this;

    }

    public NTMenu item(Item item) {

        items.add(item);

        return this;

    }

    public NTMenu subMenu(String name) {

        NTMenu sub = new NTMenu(false);

        item(new SubMenuItem(name, sub));

        return sub;

    }

    public void start() {

        printSplitLine();

        print();

    }

    public void print() {

        if (msg != null) {

            printSplitLine();
            msg.run();

        }

        if (init != null) {

            clean();
            init.run();

        }

        if (isTop) {   

            println("[0] : 退出程序");

        } else {

            println("[0] : 退出选择");

        }

        int index = 0;

        for (Item i : items) {

            index ++;

            println("[" + index + "] : " + i.name);

        }

        println();

        int choose = choose();

        while (choose < 0 || choose > index) {

            println("输入格式不正确");
            
            choose = choose();


        }

        if (choose == 0) {

            if (isTop) {

                System.exit(0);

            } else {

                return;

            }

        } else {

            Item ci = items.get(choose - 1);

            try {

                if (!ci.run()) {

                    clear();
                    printSplitLine();
                    print();

                }

            } catch (Exception ex) {

                clear();

                printSplitLine();

                ex.printStackTrace();

                printSplitLine();

                println("出现错误 要退出吗？");

                if (confirm()) {

                    System.exit(1);

                } else {

                    print();

                }

            }

        }

    }

    public static abstract class Item {

        private String name;

        public Item(String itemName) {

            name = itemName;

        }

        public abstract boolean run();

    }

    private class SubMenuItem extends Item {

        private NTMenu subMenu;

        public SubMenuItem(String menuName, NTMenu subMenu) {

            super(menuName);

            this.subMenu = subMenu;

        }

        @Override
        public boolean run() {

            clear();
            printSplitLine();

            subMenu.print();

            return false;

        }

    }

}
