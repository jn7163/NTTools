package io.kurumi.nt.tasks;

import io.kurumi.nt.*;
import java.util.*;

public class StreamSetting {
    
    // 打心设置
    
    private String KEY_SEND_LIKE_ENABLE = "sendLikeEnable";
    private String KEY_SEND_LIKE_ALL_CONTEXT = "sendLikeToAllContext";
    private String KEY_SEND_LIKE_TO_FRIENDS = "snedLikeToFriends";
    private String KEY_SEND_LIKE_TO_FOLLOWERS = "snedLikeToFollowers";
    private String KEY_SEND_LIKE_TO_USERLIST = "sendLikeToUserList";
    private String KEY_USERLIST_LIST = "userListList";
    
    private TwiAccount acc;

    public StreamSetting(TwiAccount acc) {
        this.acc = acc;
    }
    
    public void setSendLikeEnable(boolean enable) {
        
        acc.userData.put(KEY_SEND_LIKE_ENABLE,enable);
        acc.save();
        
    }
    
    public boolean isSendLikeEnable() {

        return acc.userData.getBool(KEY_SEND_LIKE_ENABLE,false);

    }
    
    public void setSnedLikeToAllContextEnable(boolean enable) {

        acc.userData.put(KEY_SEND_LIKE_ALL_CONTEXT,enable);
        acc.save();
        
    }

    public boolean isSnedLikeToAllContextEnable() {

        return acc.userData.getBool(KEY_SEND_LIKE_ALL_CONTEXT,false);

    }
    
    public void setSendLikeToFirends(boolean enable) {
        
        acc.userData.put(KEY_SEND_LIKE_TO_FRIENDS,enable);
        acc.save();
        
    }
    
    public boolean isSendLikeToFriends() {
        
        return acc.userData.getBool(KEY_SEND_LIKE_TO_FRIENDS,true);
        
    }
    
    public void setSendLikeToFollowers(boolean enable) {

        acc.userData.put(KEY_SEND_LIKE_TO_FOLLOWERS,enable);
        acc.save();

    }

    public boolean isSendLikeToFollowers() {

        return acc.userData.getBool(KEY_SEND_LIKE_TO_FOLLOWERS,false);

    }
    
    
    private static HashMap<TwiAccount,StreamSetting> tmp = new HashMap<>();
    
    public static StreamSetting get(TwiAccount acc) {
        
        if (tmp.containsKey(acc)) return tmp.get(acc);
        return tmp.put(acc,new StreamSetting(acc));
        
    }
    
}
