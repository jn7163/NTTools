package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.util.*;

public class NTMenu {

    private NTBaseCmd cmd;
    private NTMenu superMenu;

    private LinkedList<Item> items = new LinkedList<>();

    public Runnable init;
    public Runnable msg;

    public NTMenu(NTBaseCmd cmd) {

        this(cmd, null);

    }

    public NTMenu(NTBaseCmd cmd, NTMenu superMenu) {

        this.cmd = cmd;
        this.superMenu = superMenu;

    }

    public void msg(final String msg) {

        this.msg = new Runnable() {

            @Override
            public void run() {

                cmd.println(msg);

            }

        };

    }

    public void omsg(final String msg) {

        this.msg = new Runnable() {

            @Override
            public void run() {

                cmd.println(msg);
                cmd.printSplitLine();

                NTMenu.this.msg = null;

            }

        };

    }

    public NTMenu clear() {

        items.clear();

        return this;

    }

    public NTMenu item(Item item) {

        items.add(item);

        return this;

    }

    public NTMenu subMenu(String name) {

        NTMenu sub = new NTMenu(cmd, this);

        item(new SubMenuItem(name, sub));

        return sub;

    }
    
    public void start() {
        
        cmd.printSplitLine();
        
        print();
        
    }

    public void print() {

        cmd.clear();

        if (msg != null) {

            msg.run();

        }

        if (superMenu == null) {   

            cmd.println("[0] : 退出程序");

        } else {

            cmd.println("[0] : 返回上级");

        }

        if (init != null) {

            init.run();

        }

        int index = 0;

        for (Item i : items) {

            index ++;

            cmd.println("[" + index + "] : " + i.name);

        }

        cmd.println();

        int choose = cmd.choose();

        if (choose < 0 || choose > index) {

            msg = new Runnable() {

                @Override
                public void run() {

                    cmd.noSuchChoose();
                    cmd.printSplitLine();
                    msg = null;

                }

            };

            print();

            return;

        } else if (choose == 0) {

            if (superMenu == null) {

                System.exit(0);

            } else {

                return;

            }

        } else {

            Item ci = items.get(choose - 1);

            cmd.clear();

            try {

                cmd.printSplitLine();

                if (!ci.run()) {

                    cmd.printSplitLine();
                    
                    print();

                }

            } catch (Exception ex) {

                cmd.clear();

                cmd.printSplitLine();

                ex.printStackTrace();

                cmd.printSplitLine();

                cmd.println("出现错误 要退出吗？");

                if (cmd.confirm()) {

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

        public abstract boolean run()

    }

    private class SubMenuItem extends Item {

        private NTMenu subMenu;

        public SubMenuItem(String menuName, NTMenu subMenu) {

            super(menuName);

            this.subMenu = subMenu;

        }

        @Override
        public boolean run() {

            subMenu.print();

            return false;

        }

    }

}
