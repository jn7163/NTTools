package io.kurumi.nt;

import twitter4j.*;
import java.util.*;

public class NTApi {

    public static String formatUsernName(User u) {
        
        return "[ " + u.getName() + "] (@" + u.getScreenName() + ")";
        
    }
    
    public static LinkedList<Status> getContextStatus(Twitter api, Status status) throws TwitterException {

        Status top = status;

        while (top.getInReplyToStatusId() != -1 || top.getQuotedStatusId() != -1) {

            if (top.getInReplyToStatusId() != -1) {

                top = api.showStatus(top.getInReplyToStatusId());

            } else {

                top = top.getQuotedStatus();

            }

        }

        return loopReplies(api, top);

    }

    public static LinkedList<Status> loopReplies(Twitter api, Status s) throws TwitterException {
        
        LinkedList<Status> list = new LinkedList<>();

        list.add(s);

        for (Status ss : getReplies(api, s)) {

            list.addAll(loopReplies(api, ss));

        }

        return list;

    }

    public static LinkedList<Status> getReplies(Twitter api, Status status) throws TwitterException {

        LinkedList<Status> list = new LinkedList<>();

        QueryResult resp = api.search(new Query()
                                      .query("to:" + status
                                             .getUser()
                                             .getScreenName())
                                      .sinceId(status.getId())
                                      .resultType(Query.RECENT));



        for (Status s : resp.getTweets()) {

            if (s.getInReplyToStatusId() == status.getId()
                || s.getQuotedStatusId() == status.getId()) list.add(s);

        }

        while (resp.hasNext()) {

            resp = api.search(resp.nextQuery());

            for (Status s : resp.getTweets()) {

                if (s.getInReplyToStatusId() == status.getId()
                    || s.getQuotedStatusId() == status.getId()) list.add(s);

            }

        }

        return list;


    }

    public static LinkedList<UserList> getLists(Twitter api) throws IllegalStateException, TwitterException {

        LinkedList<UserList> list = new LinkedList<>();

        ResponseList<UserList> ownlists = api.getUserLists(api.getId());
        list.addAll(ownlists);

        PagableResponseList<UserList> sublist = api.getUserListSubscriptions(api.getId(), -1);
        list.addAll(sublist);

        if (sublist.hasNext()) {

            sublist  = api.getUserListSubscriptions(api.getId(), sublist.getNextCursor());
            list.addAll(sublist);

        }

        return list;

    }

    public static void reply(Twitter api, Status status, String contnent) throws TwitterException {

        if (status.getQuotedStatusId() == -1) {

            api.updateStatus(new StatusUpdate(contnent).inReplyToStatusId(status.getId()));

        }

        StringBuilder replyBuilder = new StringBuilder("@" + status.getUser().getScreenName() + " ");

        Status superStatus = status.getQuotedStatus();

        while (superStatus != null) {

            replyBuilder.append("@" + superStatus.getUser().getScreenName() + " ");

        }

        api.updateStatus(new StatusUpdate(replyBuilder.append(contnent).toString()).inReplyToStatusId(status.getId()));

    }

}
