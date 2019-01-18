package io.kurumi.nt.bots;

import io.kurumi.nt.twitter.*;
import twitter4j.*;
import io.kurumi.nt.*;

public class StatusListenerBotAdapter extends TargetAdapter {

    private TwiAccount acc;
    private StatusListenerBot bot;
    private Twitter api;
    
    private long statusId;

    public StatusListenerBotAdapter(TwiAccount acc, StatusListenerBot bot) {
        super(acc.accountId);
        this.acc = acc;
        this.bot = bot;
        api = acc.createApi();
        statusId = bot.getStatusId(acc);
    }
    
    @Override
    public void onTargetStatus(Status status, long target, boolean reply) {
        
        if (status.getInReplyToStatusId() == statusId) {
            
            try {
                
                bot.onStatus(api, status);
                
            } catch (TwitterException e) {
                
                System.out.println("Bot出错 : " + e);
                
            }

        }
        
    }
    
    
    
}
