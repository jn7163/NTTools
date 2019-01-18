package io.kurumi.nt.taip;

import cn.xsshome.taip.nlp.*;

public class AIUtil {
    
    public static String id = "2111414508";
    public static String key = "e1Z3XyOYI7VuVQP0";
    
    public static TAipNlp nlp = new TAipNlp(id,key);
    
    public static String nlpTextpolar(String text) {
        
        try {
            
            return nlp.nlpTextpolar(text);
            
        } catch (Exception e) {}
        
        return "1";

    }
    
    
    
}
