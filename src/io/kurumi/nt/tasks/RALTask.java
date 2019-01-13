package io.kurumi.nt.tasks;

import io.kurumi.nt.*;
import io.kurumi.nt.NTTask.*;
import twitter4j.*;
import java.util.concurrent.atomic.*;
import java.util.*;

public class RALTask extends NTTask implements StatusListener {

    private TwiAccount acc;
    private Twitter api;
    private TwitterStream stream;

    public AtomicBoolean likeEnable = new AtomicBoolean(true);
    public AtomicBoolean repeatEnable = new AtomicBoolean(true);

    public RALTask(TwiAccount account) {

        acc = account;

        api = acc.createApi();
        stream = new TwitterStreamFactory(acc.createConfig()).getInstance().addListener(this);
    }

    @Override
    public void onStatus(Status status) {

        if (status.getUser().getId() == acc.accountId) return;

        println(status.getUser().getName() + " : " + status.getText());

        try {

            if (likeEnable.get()) {

                api.createFavorite(status.getId());

            }

        } catch (TwitterException exc) {

            printSplitLine();
            println("Error :");
            exc.printStackTrace();
            printSplitLine();

            likeEnable.set(false);

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

        }

        try {

            api.updateStatus(new StatusUpdate(status.getText()).inReplyToStatusId(status.getId()));

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

    @Override
    public NTTask.Type getTaskType() {
        return NTTask.Type.RepeatAndLike;
    }

    @Override
    public void exec() {

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


}
