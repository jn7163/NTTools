package io.kurumi.nt.tools;

import io.kurumi.nt.*;
import twitter4j.*;
import java.util.*;
import java.lang.reflect.*;
import io.kurumi.nt.NTTask.*;
import java.io.*;

public class BlockScanner extends NTTask {

    @Override
    public NTTask.Type getTaskType() {
        return null;
    }

    private TwiAccount acc;
    private Twitter api;

    LinkedList<Long> blockedme = new LinkedList<>();
    LinkedList<Long> unblocked = new LinkedList<>();

    public BlockScanner(TwiAccount acc) {
        this.acc = acc;
        this.api = acc.createApi();
    }

    @Override
    public void exec() {

        printSplitLine();
        
        println("正在开始 :");

        Twitter api =  acc.createApi();

        try {

            IDs idl = api.getFriendsIDs(-1);

            println("已取得Fo列表 :");

            long[] ids = idl.getIDs();
            

            for (long id : ids) {

                try {

                    search(id);

                } catch (TwitterException ex) {

                    if (ex.exceededRateLimitation()) {

                        
                        printSplitLine();

                        println(" -  正在取得列表 :");

                        if (blockedme.size() == 0) {

                            println(" -  还没有");

                        } else {

                            long[] q = new long[blockedme.size()];

                            for (int index = 0;index < blockedme.size();index ++) {

                                q[index] = blockedme.get(index);

                            }

                            ResponseList<User> users = api.lookupUsers(q);

                            for (User u : users) {

                                println("B咱的人 : " + u.getName() + " (@" + u.getScreenName() + ")");

                            }

                        }


                        sleep(ex.getRateLimitStatus().getSecondsUntilReset());
                        
                        search(id);
                        

                    } else {

                        ex.printStackTrace();
                        break;

                    }


                }




            }


            printSplitLine();

            println("正在取得列表 :");

            long[] q = new long[blockedme.size()];

            for (int index = 0;index < blockedme.size();index ++) {

                q[index] = blockedme.get(index);

            }

            ResponseList<User> users = api.lookupUsers(q);

            for (User u : users) {

                println("B咱的人 : " + u.getName() + " (@" + u.getScreenName() + ")");

            }

        } catch (TwitterException ex) {

            if (ex.exceededRateLimitation()) {

                sleep(ex.getRateLimitStatus().getSecondsUntilReset());
                exec();

            } else {

                ex.printStackTrace();

            }

        }

    }

    public void search(long id) throws TwitterException {

        IDs fidl = api.getFriendsIDs(id, -1);

        long[] fid = fidl.getIDs();

        for (long sid : fid) {

            if (blockedme.contains(sid) || unblocked.contains(sid)) {

                continue;

            }

            if (Arrays.binarySearch(api.getBlocksIDs(sid).getIDs(), api.getId()) != -1) {

                blockedme.add(sid);
                
                println("检测到 :" + sid);

            }

        }


    }
    
    public void sleep(int sec) {
        
        for (int index = 0;index < sec;index ++) {
            
            int after = sec - index;
           
            println("api上限 : 需要等待" + (after / 60) + "分" + " (" + after + "秒)");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}

        }
        
    }


}
