package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.util.*;

public class NTCD<R> extends FN<R> {

    private String chn;

    public NTCD(String chn) {

        this.chn = chn;

    }

    private LinkedHashMap<String,FN<R>> items = new LinkedHashMap<>();

    public NTCD<R> add(String name, FN<R> item) {

        items.put(name, item);

        return this;

    }
    
    public NTCD<R> add(String name, final R item) {

        return add(name, new FN<R>() {
                @Override
                public R invoke() {
                    return item;
                }
            });

    }

    @Override
    public R invoke() {
        
        switch (items.size()) {

            case 0:return null;
            case 1:return items.values().iterator().next().invoke();

        }

        println("请选择" + chn + ":");

        int index = 0;

        LinkedList<Map.Entry<String,FN<R>> > list = new LinkedList<Map.Entry<String,FN<R>>>(items.entrySet());

        for (Map.Entry<String,FN<R>> item :list) {

            index ++;

            println("[" + index + "] : " + item.getKey());

        }

        int choose = choose();

        if (choose < 1 || choose > index) {

            noSuchChoose();
            return invoke();

        }

        return list.get(choose - 1).getValue().invoke();

    }


}
