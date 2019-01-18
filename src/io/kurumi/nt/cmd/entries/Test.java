package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import twitter4j.*;
import cn.xsshome.taip.nlp.*;
import io.kurumi.nt.taip.*;
import cn.hutool.core.util.*;
import cn.hutool.json.*;

import cn.hutool.json.JSONObject;

public class Test extends NTBaseCmd {

    public static void apply(final NTUser user, NTMenu menu) {

        menu.item(new NTMenu.Item("测试1") {

                @Override
                public boolean run() {

                    try {

                    } catch (Exception ex) { ex.printStackTrace(); }

                    return false;
                }

            });

    }

    

}
