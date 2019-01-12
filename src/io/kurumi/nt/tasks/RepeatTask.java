package io.kurumi.nt.tasks;

import io.kurumi.nt.*;
import java.util.*;
import io.kurumi.nt.NTTask.*;
import cn.hutool.json.JSONObject;
import twitter4j.*;

public class RepeatTask extends NTTask {

    public String userId;
    public long accountId;

    @Override
    public NTTask.Type getTaskType() {
        return NTTask.Type.Repeat;
    }

    @Override
    public void exec() throws Exception {

        Twitter api = context.getUser(userId).twiAccounts.get(accountId).createApi();

        int error = 0;

        while (error < 2) {

            long start = System.currentTimeMillis();

            try {

                ResponseList<Status> timelines = api.getHomeTimeline(new Paging().count(100));

                for (Status s : timelines) {
                    
                    
                    
                }
                
            } catch (TwitterException e) {}

            Thread.sleep(60000 - System.currentTimeMillis() - start);

        }

    }
    
    public void analysis(Status s) {
        
        LinkedList<Status> con = new LinkedList<>();
        
        
    }
    
    public void hasMe(Status s) {
        
        
        
    }

    @Override
    public void loadData(JSONObject data) {
        super.loadData(data);
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject()
            ;
    }

}
