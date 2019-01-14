package io.kurumi.nt.tasks;

import io.kurumi.nt.*;
import io.kurumi.nt.NTBase.*;
import twitter4j.*;
import java.util.concurrent.atomic.*;
import java.util.*;

public class StreamTask extends NTBase implements StatusListener {

    private TwiAccount acc;
    private Twitter api;
    private TwitterStream stream;

    public AtomicBoolean likeEnable = new AtomicBoolean(true);
    public AtomicBoolean likeAllContextEnable = new AtomicBoolean(false);
    public AtomicBoolean repeatEnable = new AtomicBoolean(true);

    public StreamTask(TwiAccount account) {

        acc = account;

        api = acc.createApi();
        
        stream = new TwitterStreamFactory(acc.createConfig()).getInstance().addListener(this);
    }
    
    public void run() {

        printSplitLine();

        try {

            stream.filter(new FilterQuery().follow(api.getFriendsIDs(-1).getIDs()));

        } catch (TwitterException e) {

            if (e.isCausedByNetworkIssue()) {

                println("网络错误...");

            } else if (e.exceededRateLimitation()) {

                RateLimitStatus rate = e.getRateLimitStatus();

                println("到达API上限 请" + rate.getSecondsUntilReset() + "秒后再试 ~");

            }


        }

    }
    

    @Override
    public void onStatus(Status status) {
        
        if (status.getUser().getId() == acc.accountId) return;
        
        if (status.isRetweeted()) return;

        println("「" + status.getUser().getName() + "」 (" + status.getUser().getScreenName() + ")\n" + status.getText());

        try {

            if (likeEnable.get()) {

                api.createFavorite(status.getId());
                
                if(likeAllContextEnable.get()) {
                    
                    Status superStatus = status.getQuotedStatus();

                    while(superStatus != null) {
                        
                        api.createFavorite(superStatus.getId());
                        superStatus = superStatus.getQuotedStatus();
                        
                    }
                    
                }

            }

        } catch (TwitterException exc) {

            if (exc.getErrorCode() != 139) {

                printSplitLine();
                exc.printStackTrace();
                printSplitLine();

                likeEnable.set(false);

                println("已停止打心");

            }

        }

        if (repeatEnable.get()) {

            repeatIfNeeded(status);

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
