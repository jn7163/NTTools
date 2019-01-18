package io.kurumi.nt.twitter;

import io.kurumi.nt.*;
import io.kurumi.nt.NTBase.*;
import twitter4j.*;
import java.util.concurrent.atomic.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;
import cn.hutool.core.util.*;

public class StreamTask extends NTBase implements Runnable {

    private TwiAccount acc;
    private Twitter api;
    private long id;
    private TwitterStream stream;

    public StreamTask(TwiAccount account,UserList specialList) throws TwitterException {

        acc = account;

        api = acc.createApi();

        id = api.getId();
        
        stream = new TwitterStreamFactory(acc.createConfig()).getInstance();

        long[] target = NTApi.longMarge(NTApi.getAllFo(api), NTApi.getAllFr(api));

        LinkedList<User> specList =  NTApi.getListUsers(api,specialList.getId());
        specList.add(api.verifyCredentials());
        
        stream.addListener(new TargetAdapter(specList) {

                @Override
                public void onTargetStatus(Status status,long target, boolean reply) {
                    
                    if (status.isRetweet()) return;
                    if (status.isFavorited()) return;
                    
                    try {
                        
                        api.createFavorite(status.getId());
                        
                        println("「流任务」 打心 : " + status.getUser().getName());
                        println(status.getText());
                        
                    } catch (TwitterException e) {}

                }
                
            });
        
        stream.addListener(new TargetAdapter(target) {

                @Override
                public void onTargetStatus(Status status, long target, boolean reply) {
                    
                    if (status.isRetweet()) return;
                    
                    try {
                        procrssRepeat(status);
                    } catch (TwitterException e) {}

                }

                
            });
        
    }
    
    private void procrssRepeat(Status status) throws TwitterException {
        
        boolean isRepeat = false;
        Status superStatus = status;

        while(superStatus.getQuotedStatusId() != -1) {
           
            superStatus = status.getQuotedStatus();
            
            if (superStatus.getText().equals(status.getText())) {
                
                if (superStatus.getUser().getId() == id) return;
                
                isRepeat = true;
                
            }
            
        }
        
        long inr = superStatus.getInReplyToUserId();
        
        if (superStatus == status && inr != -1 && inr != id) {
            
            Status inrs = api.showStatus(inr);
            
            if (inrs.getText().equals(status.getText())) {
                
                if (inrs.getUser().getId() == id) return;
                
                isRepeat = true;
                
            }
            
        }
        
        if (isRepeat) {
            
            NTApi.reply(api,status,status.getText());
            println("「流任务」 复读 : " + status.getUser().getName());
            println(status.getText());
            
        }
        
    }

    @Override
    public void run() {
        stream.sample();
    }
    
    public void stop() {
        stream.shutdown();
    }
    

}
