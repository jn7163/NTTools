package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.util.*;

public class NTCM<D> extends NTBaseCmd {

    private String chn;
    
    public NTCM(String chn) {
        
        this.chn = chn;
        
    }
    
    private LinkedHashMap<String,D> items = new LinkedHashMap<>();

    public NTCM<D> add(String name, D item) {

        items.put(name, item);

        return this;

    }
    
    public D chooseItem() {
        
        clear();
        
        switch (items.size()) {
            
            case 0:return null;
            case 1:return items.values().iterator().next();
            
        }
        
        println("请选择" + chn + ":");
        
        int index = 0;
        
        for (Map.Entry<String,D> item :items.entrySet()) {
            
            index ++;
            
            println("[" + index + "] : " + item.getKey());
            
        }
        
        int choose = choose();
        
        if (choose < 1 || choose > index) {
            
            noSuchChoose();
            return chooseItem();
            
        }
        
        return new LinkedList<D>(items.values()).get(index - 1);
        
    }


}
