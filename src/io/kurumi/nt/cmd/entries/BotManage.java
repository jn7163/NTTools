package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import io.kurumi.nt.bots.*;
import java.util.*;
import io.kurumi.nt.twitter.*;
import twitter4j.*;

public class BotManage extends NTBaseCmd {
    
    public static void apply(NTUser user,NTMenu menu) {
        
        makeBotMenu(user,menu.subMenu("机器人"));
        
    }
    
    public static LinkedList<StatusListenerBot> statusListenerBotList = new LinkedList<>();

    static {
        
        statusListenerBotList.add(new UserIdBot());
        
    }
    
    private static void makeBotMenu(final NTUser user, final NTMenu menu) {
        
        menu.init = new Runnable() {

            @Override
            public void run() {
                
                final TwiAccount acc = UserManage.chooseAccount(user);
                
                printSplitLine();
                
                println("机器人目录 : " + acc.getFormatedName());
                
                for (final StatusListenerBot bot : statusListenerBotList) {
                    
                    menu.item(new NTMenu.Item("设置" + bot.getBotName() + " 推文Id : " + bot.getStatusId(acc)) {

                            @Override
                            public boolean run() {
                                
                                long id = inputLong("输入新Id : ");
                                
                                bot.setStatusId(acc,id);
                                
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
                                
                                builder.addListener(new StatusListenerBotAdapter(acc,bot));
                                
                                count ++;
                                
                            }
                            
                            if (count == 0) {
                                
                                println("启动失败 无启用的Bot");
                                return false;
                                
                            }
                            
                            TwitterStream stream = builder.build().sample();
                            
                            println("启动成功 输入 stop 停止");
                            
                            printSplitLine();

                            while(!input().equals("stop")) {}

                            stream.shutdown();
                            
                            return false;
                        }
                    });
                
                
            }

            
            
        };
        
    }
    
    
  
    
}
