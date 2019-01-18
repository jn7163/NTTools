package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import io.kurumi.nt.bots.*;
import java.util.*;
import io.kurumi.nt.twitter.*;
import twitter4j.*;

public class BotManage extends NTBaseCmd {

    public static void apply(NTUser user, NTMenu menu) {


        makeBotMenu(user, menu.subMenu("机器人"));


    }


    public static LinkedList<StatusListenerBot> statusListenerBotList = new LinkedList<>();
    public static LinkedList<SwitchBot> switchBotList = new LinkedList<>();


    static {

        statusListenerBotList.add(new TextPolatBot());
        statusListenerBotList.add(new UserIdBot());

        switchBotList.add(new ReFrientBot());

    }

    private static void makeBotMenu(final NTUser user, final NTMenu menu) {

        menu.init = new Runnable() {

            @Override
            public void run() {

                for (TwiAccount acc : user.twiAccounts) {

                    makeUserBotMenu(user, menu.subMenu(acc.getFormatedName()), acc);

                }

            }

        };

    }


    private static void makeUserBotMenu(final NTUser user, final NTMenu menu, final TwiAccount acc) {


        menu.init = new Runnable() {

            @Override
            public void run() {

                println("机器人目录 : " + acc.getFormatedName());

                for (final StatusListenerBot bot : statusListenerBotList) {

                    menu.item(new NTMenu.Item("设置" + bot.getBotName() + " 推文Id : " + bot.getStatusId(acc)) {

                            @Override
                            public boolean run() {

                                long id = inputLong("输入新Id : ");

                                if (!confirm()) return false;

                                bot.setStatusId(acc, id);

                                return false;
                            }

                        });

                }

                for (final SwitchBot bot : switchBotList) {

                    menu.item(new NTMenu.Item("设置" + bot.getBotName() + " 开启状态 : " + bot.isEnable(acc)) {

                            @Override
                            public boolean run() {



                                if (confirm("要切换吗？")) {

                                    bot.setEnable(acc, !bot.isEnable(acc));

                                }

                                return false;
                            }

                        });

                }


                menu.item(new NTMenu.Item("启动机器人") {

                        @Override
                        public boolean run() {

                            println("正在启动Bot :");

                            StreamBuilder builder = new StreamBuilder(acc);

                            int count = 0;

                            for (StatusListenerBot bot : statusListenerBotList) {

                                if (bot.getStatusId(acc) == -1) continue;

                                builder.addListener(bot.createAdapter(acc));

                                count ++;

                            }
                            
                            for (SwitchBot bot : switchBotList) {

                                if (!bot.isEnable(acc)) continue;

                                bot.onInit(builder.build(),acc);
                                
                                println("「" + bot.getBotName() + "」 已启动");

                                count ++;

                            }
                            

                            if (count == 0) {

                                println("启动失败 无启用的Bot");
                                return false;

                            }

                            TwitterStream stream = builder.build().filter(new FilterQuery().follow(new long[] { acc.accountId}));

                            println("启动成功 输入 stop 停止");

                            printSplitLine();

                            while (!input().equals("stop")) {}

                            stream.shutdown();

                            return false;
                        }

                    });



            }



        };

    }




}
