package io.kurumi.nt.cmd;

import io.kurumi.nt.*;
import java.util.*;

public class NTCD<R> extends NTBaseCmd {

    private String chn;

    public NTCD(String chn) {

        this.chn = chn;

    }

    private LinkedHashMap<String,Func<R>> items = new LinkedHashMap<>();

    public NTCD<R> add(String name, Func<R> item) {

        items.put(name, item);

        return this;

    }

    public R invoke() {

        clear();

        switch (items.size()) {

            case 0:return null;
            case 1:return items.values().iterator().next().invoke();

        }

        println("请选择" + chn + ":");

        int index = 0;

        for (Map.Entry<String,Func<R>> item :items.entrySet()) {

            index ++;

            println("[" + index + "] : " + item.getKey());

        }

        int choose = choose();

        if (choose < 1 || choose > index) {

            noSuchChoose();
            return invoke();

        }

        return new LinkedList<Func<R>>(items.values()).get(index - 1).invoke();

    }


}
