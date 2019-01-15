package io.kurumi.nt.tasks;

import io.kurumi.nt.*;
import io.kurumi.nt.NTBase.*;
import twitter4j.*;
import java.util.concurrent.atomic.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

public class StreamTask extends NTBase implements StatusListener,Runnable {

    private TwiAccount acc;
    private Twitter api;
    private TwitterStream stream;
    private StreamSetting setting;
    
    public StreamTask(TwiAccount account) {

        acc = account;

        api = acc.createApi();
        
        stream = new TwitterStreamFactory(acc.createConfig()).getInstance().addListener(this);
        
        setting = StreamSetting.get(acc);
        
    }
    
    public void run() {
        
        if (!setting.isSendLikeEnable()
            && (setting.isSendLikeToFriends() || setting.isSendLikeToFollowers())) {
            
            println(acc.getFormatedName() + ":" + "无启用的功能...");
            
            return;
            
        }
        
        try {
            
            long[] target;
            
            if (setting.isSendLikeToFriends() || setting.isSendLikeToFollowers()) {
                
               target = NTApi.longMarge(NTApi.getAllFr(api),NTApi.getAllFo(api));
                
            } else if(setting.isSendLikeToFriends()) {
                
                target = NTApi.getAllFr(api);
               
                
            } else {
                
                target = NTApi.getAllFo(api);
                
            }
            
            stream.filter(new FilterQuery().follow(target));

        } catch (TwitterException e) {

            if (e.isCausedByNetworkIssue()) {

                println("网络错误...");

            } else if (e.exceededRateLimitation()) {

                RateLimitStatus rate = e.getRateLimitStatus();

                println("到达API上限 请" + rate.getSecondsUntilReset() + "秒后再试 ~");

            }


        }

    }
    
    Executor exec = Executors.newSingleThreadExecutor();

    @Override
    public void onStatus(final Status status) {
        
        if (status.getUser().getId() == acc.accountId) return;
        
        if (status.isRetweet()) return;
        
        exec.execute(new Runnable() {

                @Override
                public void run() {
                    
                    doMain(status);
                    
                }
                
            });

       

    }
    
    public void doMain(Status status) {
        
        try {

            int sc = 0;

            if (setting.isSendLikeEnable()) {

                if (status.isFavorited()) return;

                sc ++;


                api.createFavorite(status.getId());

                if(setting.isSnedLikeToAllContextEnable()) {

                    LinkedList<Status> list = NTApi.getContextStatus(api, status);

                    for (Status s : list) {

                        if (s.getUser().getId() == acc.accountId) continue;

                        if (s.isRetweet()) continue;

                        if (s.isFavorited()) continue;

                        api.createFavorite(s.getId());

                        sc ++;

                    }

                }

            }

            println();
            println("「流任务 for " + acc.name + "」 收到信息 来自 " + status.getUser().getName() + " :");
            println(status.getText());

            println("打心 x " + sc);

        } catch (TwitterException exc) {

            if (exc.exceededRateLimitation()) {

                exc.printStackTrace();
                
                println("「流任务」到达Api上限 正在等待 : ");

                try {
                    Thread.sleep(exc.getRateLimitStatus().getSecondsUntilReset());
                } catch (InterruptedException e) {}

                doMain(status);
                
                return;

            }

            if (exc.getErrorCode() != 139) {

                println("「流人物」打心失败 : 已被限制");

            } else if (exc.isCausedByNetworkIssue()) {
                
                println("「流任务」网络错误....");
                
            }

        }
        
    }

    public void repeatIfNeeded(Status status) {

        Status superStatus =  status.getQuotedStatus();

        if (superStatus == null || !superStatus.getText().equals(status.getText())) return;

        while (superStatus != null) {

            if (superStatus.getUser().getId() == acc.accountId) return;
            superStatus = superStatus.getQuotedStatus();
        }

        try {

            NTApi.reply(api, status, status.getText());

            println("已复读 : " + status.getText());

        } catch (TwitterException e) {

            printSplitLine();
            e.printStackTrace();
            printSplitLine();

        }

    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice p1) {
    }

    @Override
    public void onTrackLimitationNotice(int p1) {
    }

    @Override
    public void onScrubGeo(long p1, long p2) {
    }

    @Override
    public void onStallWarning(StallWarning p1) {
    }

    @Override
    public void onException(Exception err) {

        printSplitLine();
        err.printStackTrace();
        printSplitLine();

    }

}
