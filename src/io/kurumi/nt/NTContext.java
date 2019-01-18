package io.kurumi.nt;

import java.io.*;
import java.util.*;
import cn.hutool.core.io.*;

public class NTContext {

    public NTContext(File dataDir) {
        this.dataDir = dataDir;
    }

    private File dataDir;

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public File getUserDir() {
        return new File(dataDir, "data/users");
    }

    private HashMap<String,NTUser> cache = new HashMap<>();

    public NTUser getUser(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }
        NTUser user = new NTUser(this, name);
        cache.put(name, user);
        return user;
    }

    public NTUser getDefaultUser() {
        return getUser("dafault");
    }

}
