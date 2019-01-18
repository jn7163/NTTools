package io.kurumi.nt.twitter;

import cn.hutool.core.util.*;
import java.util.*;
import twitter4j.*;

public abstract class TargetAdapter extends StatusAdapter {

    public final long[] target;

    public TargetAdapter(List<User> list) {

        target = new long[list.size()];

        for (int index = 0;index < list.size();index ++) {

            target[index] = list.get(index).getId();

        }

    }

    public TargetAdapter(long... target) {

        this.target = target;

    }

    @Override
    public void onStatus(Status status) {

        Status superStatus = status;

        do {
            long ssid = superStatus.getUser().getId();
            long srid = superStatus.getInReplyToStatusId();

            if (ArrayUtil.contains(target, ssid)) {

                onTargetStatus(status, ssid, superStatus == status);
                return;

            }

            if (ArrayUtil.contains(target, srid)) {

                onTargetStatus(status, srid, superStatus == status);
                return;

            }
            
            superStatus = superStatus.getQuotedStatus();

        } while (superStatus.getQuotedStatus() != null);

    }

    public abstract void onTargetStatus(Status status, long target, boolean reply);

}
