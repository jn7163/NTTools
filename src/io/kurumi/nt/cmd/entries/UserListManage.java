package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import twitter4j.*;
import io.kurumi.nt.cmd.*;
import java.util.*;

public class UserListManage extends NTBaseCmd {

    public static UserList chooseUserList(TwiAccount acc) {

        try {
            LinkedList<UserList> all = NTApi.getLists(acc.createApi());

            NTCD<UserList> cd =  new NTCD<UserList>("列表");

            for (UserList l : all) {

                cd.add(l.getName(), l);

            }

            return cd.invoke();

        } catch (TwitterException e) { return null; }

    }


}
