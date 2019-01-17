package io.kurumi.nt.dm;

import io.kurumi.nt.*;
import twitter4j.*;
import java.lang.annotation.*;
import io.kurumi.nt.cmd.*;
import io.kurumi.nt.cmd.entries.*;
import twitter4j.conf.*;

public class DirectMain extends NTBaseCmd implements Runnable,UserStreamListener {

    private TwiAccount acc;
    private Twitter api;
    private TwitterStream stream;

    public DirectMain(TwiAccount account) {

        acc = account;

        api = acc.createApi();

        stream = new TwitterStreamFactory(acc.createConfig()).getInstance().addListener(this);

      
        
    }
    
    public static void apply(final NTUser user,NTMenu menu) {
        
        
        menu.item(new NTMenu.Item("启动机器人") {

                @Override
                public boolean run() {
                   
                    new DirectMain(UserManage.chooseAccount(user)).run();
                    
                    println("已启动 输入 stop 停止");
                    
                    while(!"stop".equals(input())) {}
                    
                    
                    
                    return false;
                }
                
            });
        
    }
    
    @Override
    public void run() {
        
        stream.user();
        
    }

    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
    }

    @Override
    public void onFriendList(long[] friendIds) {
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
    }

    @Override
    public void onFollow(User source, User followedUser) {
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser) {
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        
        println("from : " + NTApi.formatUsernName(directMessage.getSender()));
        println(directMessage.getText());
        
        try {
            api.sendDirectMessage(directMessage.getSenderId(), "hello！");
        } catch (TwitterException e) {
            
            e.printStackTrace();
            
        }

    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list) {
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
    }

    @Override
    public void onUserProfileUpdate(User updatedUser) {
    }

    @Override
    public void onUserSuspension(long suspendedUser) {
    }

    @Override
    public void onUserDeletion(long deletedUser) {
    }

    @Override
    public void onBlock(User source, User blockedUser) {
    }

    @Override
    public void onUnblock(User source, User unblockedUser) {
    }

    @Override
    public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
    }

    @Override
    public void onFavoritedRetweet(User source, User target, Status favoritedStatus) {
    }

    @Override
    public void onQuotedTweet(User source, User target, Status quotingTweet) {
    }

    @Override
    public void onException(Exception ex) {
    }
    
    @Override
    public void onStatus(Status status) {
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
    }

    @Override
    public void onStallWarning(StallWarning warning) {
    }
    
}
