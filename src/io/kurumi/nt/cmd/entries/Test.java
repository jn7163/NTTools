package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import twitter4j.*;

public class Test extends NTBaseCmd {
    
    public static void apply(final NTUser user,NTMenu menu) {
        
        menu.item(new NTMenu.Item("测试1") {

                @Override
                public boolean run() {
                    
                    try {
                    
                    TwiAccount acc =  UserManage.chooseAccount(user);
                    
                    Twitter api = acc.createApi();
                    
                    long id = Long.parseLong(input("status id"));
                    
                    Status status = api.showStatus(id);
                    
                    println("user : " + NTApi.formatUsernName(status.getUser()));
                    println("status : " + status.getText());
                    println("inr : " + status.getInReplyToUserId());
                    println("qu : " + status.getQuotedStatusId());
                    
                    String reply = input("reply");
                    
                    NTApi.reply(api,status,reply);
                    
                    } catch(Exception ex) { ex.printStackTrace(); }
                    
                    return false;
                }
                
            });
        
    }
    
}
