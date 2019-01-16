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

    private long[] target;

    private ExecutorService exec = Executors.newSingleThreadExecutor();

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

            if (setting.isSendLikeToFriends() || setting.isSendLikeToFollowers()) {

                target = NTApi.longMarge(NTApi.getAllFr(api), NTApi.getAllFo(api));

            } else if (setting.isSendLikeToFriends()) {

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

    public AtomicBoolean stopped = new AtomicBoolean(false);

    public void stop() {

        stopped.set(true);

        stream.shutdown();

        exec.shutdownNow();

    }

    public AtomicBoolean rate = new AtomicBoolean(false);

    @Override
    public void onStatus(final Status status) {

        if (stopped.get()) return;

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

        println();
        println("「流任务 for " + acc.name + "」 收到信息 来自 " + status.getUser().getName() + " :");
        println(status.getText());

        sendLikeIfNeeded(status);
        repeatIfNeeded(status);

    }

    public void sendLikeIfNeeded(Status status) {

        try {

            int sc = 0;




            if (setting.isSendLikeEnable()) {

                if (status.getUser().getId() == acc.accountId) return;

                if (status.isRetweet()) return;

                if (status.isFavorited()) return;


                LinkedHashSet<Status> list;

                if (setting.isSnedLikeToAllContextEnable()) {







                    if (rate.get()) {

                        list = NTApi.getContextStatusWhenSearchRated(api, status, target);

                    } else {

                        list = NTApi.getContextStatus(api, status , target);


                    }

                } else {

                    list = new LinkedHashSet<>();

                    list.add(status);

                }


                for (Status s : list) {

                    if (s.getUser().getId() == acc.accountId) continue;

                    if (s.isRetweet()) continue;

                    if (s.isFavorited()) continue;

                    if (Arrays.binarySearch(target, status.getId()) == -1) continue;

                    try {

                        api.createFavorite(s.getId());

                        sc ++;

                        try {
                            Thread.sleep(233);
                        } catch (InterruptedException e) {}


                    } catch (TwitterException ex)  {

                        if (ex.getStatusCode() == 139) {

                            ex.printStackTrace();

                        }   if (ex.getStatusCode() == 429) {

                            println("「流任务」 打心被限制");

                            break;

                        } 



                    }



                }



            }


            println("「流任务」 打心 x " + sc);

        } catch (final TwitterException exc) {

            if (exc.getStatusCode() == 139) {

                println("已经打心过 请检查是否有其他运行的打心程序");

            } else if (exc.exceededRateLimitation()) {

                exc.printStackTrace();

                println("「流任务」到达Api上限 正在等待 : " + exc.getRateLimitStatus().getSecondsUntilReset() + " 秒");

                rate.set(true);

                doMain(status);

                new Thread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                Thread.sleep(exc.getRateLimitStatus().getSecondsUntilReset() * 1000);
                            } catch (InterruptedException e) {}

                            rate.set(false);

                        }

                    }).start();



                return;

            } else if (exc.getErrorCode() == 429 && exc.getRateLimitStatus() == null) {

                println("「流任务」 打心被限制");

            }

            // exc.printStackTrace();

        }


    }

    public void repeatIfNeeded(Status status) {

        try {

            if (status.getUser().getId() == api.getId()) return;

            Status superStatus;

            if (status.getInReplyToStatusId() != -1) {

                superStatus = api.showStatus(status.getInReplyToStatusId());

            } else {

                superStatus = status.getQuotedStatus();

            }

            if (superStatus.getUser().getId() == api.getId()) return;

            if (!status.getText().equals(superStatus.getText())) return;

            while (superStatus.getInReplyToStatusId() != -1 || superStatus.getQuotedStatusId() != -1) {

                if (superStatus.getUser().getId() == acc.accountId) return;
                
            }


            try {

                NTApi.reply(api, status, status.getText());

                println("已复读 : " + status.getText());

            } catch (TwitterException e) {

                printSplitLine();
                e.printStackTrace();
                printSplitLine();

            }


        } catch (TwitterException e) {

            return;

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
