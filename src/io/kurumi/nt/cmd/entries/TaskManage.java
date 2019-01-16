package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import java.util.*;
import io.kurumi.nt.tasks.*;

public class TaskManage extends NTBaseCmd {
    
    public static void apply(final NTUser user,final NTMenu menu) {
        
        for (Map.Entry<Long,TwiAccount> acc : user.twiAccounts.entrySet()) {
            
            NTMenu ustsm = menu.subMenu("流任务 : " + acc.getValue().getFormatedName());
            buildUSTSM(user,ustsm,acc.getValue());

        }
        
       
        
        menu.item(new NTMenu.Item("流任务 : 全部启动") {

                @Override
                public boolean run() {
                  
                    clear();

                    printSplitLine();

                    println("正在启动流任务...");
                    println("输入 stop 停止...");
                    
                    for (Map.Entry<Long,TwiAccount> acc : user.twiAccounts.entrySet()) {
                        
                        new Thread(new StreamTask(acc.getValue())).start();
                        
                    }
                   
                    while(!"stop".equals(input())) {}

                    menu.omsg("已停止任务...");
                    
                    return false;
                }
                
            });
            
          
        
    }
    
    private static void buildUSTSM(final NTUser user,final NTMenu menu,final TwiAccount acc) {
        
        menu.init  = new Runnable() {

            @Override
            public void run() {
              
                buildSLSM(user,menu.subMenu("打心设置"),acc);
                menu.subMenu("复读设置");
                menu.item(new NTMenu.Item("启动任务") {

                        @Override
                        public boolean run() {
                            
                            StreamTask task = new StreamTask(acc);

                            clear();

                            printSplitLine();

                            println("正在启动流任务...");
                            println("输入 stop 停止...");

                            new Thread(task).start();

                            while(!"stop".equals(input())) {}

                            menu.omsg("已停止任务...");
                            
                            return false;
                            
                        }
                        
                    });
                
            }
            
        };
        
        
        
    }
    
  
    
    private static void buildSLSM(NTUser user,final NTMenu menu,final TwiAccount acc) {
        
        menu.init = new Runnable() {

            @Override
            public void run() {
                
                final StreamSetting setting = StreamSetting.get(acc);

                menu.item(new NTMenu.Item("打心开启 : " + boolToChs(setting.isSendLikeEnable())) {

                        @Override
                        public boolean run() {
                            
                            boolean target = !setting.isSendLikeEnable();
                            
                            clear();

                            printSplitLine();
                            
                            if (confirm("是否" + boolToChs(target))) {
                                
                                setting.setSendLikeEnable(target);
                                
                            }
                            
                            return false;
                            
                        }
                        
                    });
                    
                
                menu.item(new NTMenu.Item("对正在关注打心 (时间线) : " + boolToChs(setting.isSendLikeToFriends())) {

                        @Override
                        public boolean run() {

                            boolean target = !setting.isSendLikeToFriends();
                          
                            clear();

                            printSplitLine();
                            
                            if (confirm("是否" + boolToChs(target))) {

                                setting.setSendLikeToFirends(target);

                            }

                            return false;

                        }

                    });
                    
                menu.item(new NTMenu.Item("对关注者打心 : " + boolToChs(setting.isSendLikeToFollowers())) {

                        @Override
                        public boolean run() {

                            boolean target = !setting.isSendLikeToFollowers();

                            clear();

                            printSplitLine();
                            
                            if (confirm("是否" + boolToChs(target))) {

                                setting.setSendLikeToFollowers(target);

                            }

                            return false;

                        }

                    });
                    
                menu.item(new NTMenu.Item("上下文打心 : " + boolToChs(setting.isSnedLikeToAllContextEnable())) {

                        @Override
                        public boolean run() {

                            boolean target = !setting.isSnedLikeToAllContextEnable();

                            clear();

                            printSplitLine();
                            
                            if (confirm("是否" + boolToChs(target))) {

                                setting.setSnedLikeToAllContextEnable(target);

                            }

                            return false;

                        }

                    });
                
                
            }
            
        };
        
    }
    
    private static String boolToChs(boolean bool) {
        
        if (bool) return "开启";
        else return "关闭";
        
    }
    
}
