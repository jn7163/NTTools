package io.kurumi.nt.dm;

import io.kurumi.nt.*;
import twitter4j.*;
import java.lang.annotation.*;
import io.kurumi.nt.cmd.*;
import io.kurumi.nt.cmd.entries.*;
import twitter4j.conf.*;

public class DirectMain extends NTBaseCmd {

    private TwiAccount acc;
    private Twitter api;
    private TwitterStream stream;

    public DirectMain(TwiAccount account) {

        acc = account;

        api = acc.createApi();

        


    }

    public static void apply(final NTUser user, NTMenu menu) {


        menu.item(new NTMenu.Item("启动机器人") {

                @Override
                public boolean run() {

                //    new DirectMain(UserManage.chooseAccount(user)).run();

                    println("已启动 输入 stop 停止");

                    while (!"stop".equals(input())) {}



                    return false;

}});

}

}
