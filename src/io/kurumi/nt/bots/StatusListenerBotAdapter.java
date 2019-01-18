package io.kurumi.nt.bots;

import io.kurumi.nt.twitter.*;
import twitter4j.*;
import io.kurumi.nt.*;

public class StatusListenerBotAdapter extends StatusAdapter {

    private TwiAccount acc;
    private StatusListenerBot bot;
    private Twitter api;
    
    private long statusId;

    public StatusListenerBotAdapter(TwiAccount acc, StatusListenerBot bot) {
        this.acc = acc;
        this.bot = bot;
        api = acc.createApi();
        statusId = bot.getStatusId(acc);
        
        
        System.out.println("「" + bot.getBotName() + "」"  + "正在监听 : " + statusId);
    }
    
    
    @Override
    public void onStatus(Status status) {
        
        if (status.getInReplyToStatusId() == statusId) {
            
            System.out.println("「" + bot.getBotName() + "」" + "正在处理..");
            
            try {
                
                bot.onStatus(api, status);
                
            } catch (TwitterException e) {
                
                System.out.println("「" + bot.getBotName() + "」"  + "出错 : " + e);
                
            }

        }
        
    }
    
    
    
}
