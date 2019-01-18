package io.kurumi.nt.taip;

import cn.xsshome.taip.nlp.*;
import cn.hutool.json.*;

public class AIUtil {

    public static String id = "2111414508";
    public static String key = "e1Z3XyOYI7VuVQP0";

    public static TAipNlp nlp = new TAipNlp(id, key);

    public static float nlpTextpolar(String text) throws Exception {


        JSONObject obj = new JSONObject(nlp.nlpTextpolar(text)).getJSONObject("data");

        int polar = obj.getInt("polar");
        float confd = obj.getFloat("confd");


        return polar * confd;

    }

}
