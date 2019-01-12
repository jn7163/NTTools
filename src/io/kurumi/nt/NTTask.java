package io.kurumi.nt;

import java.util.*;
import cn.hutool.json.*;
import io.kurumi.nt.tasks.*;

public abstract class NTTask implements TaskOut {

    public NTContext context;
    public String taskName;

    public abstract Type getTaskType();
    public abstract void exec() throws Exception;

    private TaskOut taskOut = new TaskOut() {

        @Override
        public void print(String msg) {
            System.out.print(msg);
        }

        @Override
        public void println() {
            System.out.println();
        }

        @Override
        public void println(String msg) {
            System.out.println(msg);
        }

        @Override
        public void printSplitLine() {

            println("------------------------");

        }

    };

    public void setTaskOut(TaskOut out) {
        taskOut = out;
    }

    @Override
    public void print(String msg) {
        taskOut.print(msg);
    }

    @Override
    public void println() {
        taskOut.println();
    }

    @Override
    public void println(String msg) {
        taskOut.println(msg);
    }

    @Override
    public void printSplitLine() {
        taskOut.printSplitLine();
    }


    public void loadData(JSONObject data) {

        taskName = data.getStr("taskName");

    }

    public JSONObject toJSONObject() {

        return new JSONObject()
            .put("taskName", taskName)
            .put("taskType", getTaskType());

    }

    static enum Type {

        Repeat("repeat","复读任务","如果时间线上有复读的推文,那么程序就会自动跟随复读..."),
        ScanBlockedMe("scanbm","搜索B我的人","搜索所有我Fo的人的Fo");

        public final String type;
        public final String name;
        public final String desc;

        public Type(String type,String name, String desc) {
            this.type = type;
            this.name = name;
            this.desc = desc;
        };



    }

}
