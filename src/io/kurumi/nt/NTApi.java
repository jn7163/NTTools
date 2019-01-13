package io.kurumi.nt;

import twitter4j.*;
import org.apache.http.*;

public class NTApi {
    
    public static void reply(Twitter api,Status status,String contnent) throws TwitterException {

        StringBuilder replyBuilder = new StringBuilder("@" + status.getUser().getScreenName() + " ");

        Status superStatus = status.getQuotedStatus();

        while (superStatus != null) {

            replyBuilder.append("@" + superStatus.getUser().getScreenName() + " ");

        }

        api.updateStatus(new StatusUpdate(replyBuilder.append(contnent).toString()).inReplyToStatusId(status.getId()));

    }

    public static void directReply(Twitter api,Status status,String contnent) throws TwitterException {
        
        api.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + contnent).inReplyToStatusId(status.getId()));

    }
    
}
