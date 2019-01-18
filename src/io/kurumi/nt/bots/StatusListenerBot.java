package io.kurumi.nt.bots;

import twitter4j.*;
import io.kurumi.nt.*;

public abstract class StatusListenerBot {
    
    public abstract String getBotName();
    public abstract String getConfigKey();
    public abstract void onStatus(Twitter api,Status status) throws TwitterException;
    
    public long getStatusId(TwiAccount acc) {
        
        return acc.userData.getLong(getConfigKey(),-1L);
        
    }
    
    public void setStatusId(TwiAccount acc,long id) {
        
        acc.userData.put(getConfigKey(),id);
        acc.save();
        
    }
    
}
