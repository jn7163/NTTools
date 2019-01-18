package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import twitter4j.*;
import io.kurumi.nt.cmd.*;
import java.util.*;

public class UserListManage extends NTBaseCmd {
    
    public static UserList chooseOwnUserList(TwiAccount acc) throws IllegalStateException, TwitterException {
        
        LinkedList<UserList> all = NTApi.getLists(acc.createApi());

        NTCD<UserList> cd =  new NTCD<UserList>("自己的列表");

        for (UserList l : all) {
            
            cd.add(l.getName(),l);
            
        }
        
        return cd.invoke();
        
    }
    
   
}
