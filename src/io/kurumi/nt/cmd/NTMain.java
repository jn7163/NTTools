package io.kurumi.nt.cmd;

import cn.hutool.core.util.*;
import cn.hutool.http.*;
import io.kurumi.nt.*;
import io.kurumi.nt.tools.*;
import java.io.*;
import java.util.*;
import twitter4j.*;
import twitter4j.auth.*;

import twitter4j.Status;
import io.kurumi.nt.tasks.*;

public class NTMain {

    static File dataDir = new File(".");

    static NTContext context;
    static NTUser user;

    public static void main(String[] args) {

        printSplitLine();

        println("NT盒子正在启动...");

        println("以默认用户登录  ");

        context = new NTContext(dataDir);
        user = context.getDefaultUser();

        mainMenu();

    }

    static void mainMenu() {

        printSplitLine();

        println("0. 结束程序");
        println("1. Api管理");
        println("2. 账号管理");
        println("3. 查看日志");
        println("4. 所有工具");

        switch (choose()) {

            case 0 : System.exit(0);return;
            case 1 : ApiManage.main(); return;
            case 2 : AccountManage.main();return;
            case 3 : {
                    printSplitLine();
                    println(context.getLog());
                    mainMenu();
                    return;
                }
            case 4 : Tools.main();return;

            default : {

                    noSuchChoose();
                    mainMenu();

                }

        }

    }

    static class Tools {

        static void main() {

            printSplitLine();
            println("工具箱 : ");
            println();

            println("0.返回主菜单");

            println("1.取得ID信息 (带关系)");
            println("2." + NTTask.Type.RepeatAndLike.name + "(" + NTTask.Type.RepeatAndLike.desc + ")");
            switch (choose()) {

                case 0:mainMenu();return;
                case 1:getBL();return;
                case 2:repeatAndLike();return;
                
                default : {
                        noSuchChoose();
                        main();
                        return;
                    }

            }


        }

        static void repeatAndLike() {
            
            TwiAccount acc = AccountManage.chooseAccount();

            if (acc == null) {

                println("还没有添加用户 ~");
                main();
                return;

            }
            
            printSplitLine();
            
            println("请选择模式 :");
            println();
            println("0.返回工具菜单");
            println("1.仅打心");
            println("2.仅复读");
            println("3.打心和复读");
            println();
            println("注意 打心失败会停止打心");
            
            RALTask task = new RALTask(acc);
            
            switch(choose()) {
                
                case 0:main();return;
                case 1:task.repeatEnable.set(false);break;
                case 2:task.likeEnable.set(false);break;
                case 3:break;
                
                default: {
                    
                    noSuchChoose();
                    main();
                    return;
                    
                }
                
            }
            
            task.exec();

        }

        static void getBL() {

            TwiAccount acc = AccountManage.chooseAccount();

            if (acc == null) {

                println("还没有添加用户 ~");
                main();
                return;

            }

            Twitter api = acc.createApi();

            String sn = input("输入目标ID : ");

            try {

                User u = api.showUser(sn);

                printSplitLine();
                println("取得结果 :");

                println(u.getName() + " (@" + u.getScreenName() + ")");
                println("描述 : " + u.getDescription());
                println("正在关注 : " + u.getFriendsCount());                println("关注者 : " + u.getFollowersCount());
                println("账号创建 : " + u.getCreatedAt().toLocaleString());

                Relationship fs = api.showFriendship(u.getId(), api.getId());

                if (fs.isSourceBlockingTarget()) println("block了你");
                if (fs.isSourceFollowedByTarget()) println("被你关注");
                if (fs.isSourceFollowingTarget()) println("关注了你");
                if (fs.isSourceMutingTarget()) println("mute了你");
                if (fs.isSourceNotificationsEnabled()) println("打开了对你的通知");

            } catch (TwitterException e) {

                e.printStackTrace();

            }

            main();

        }

        static void scan() {


            TwiAccount acc = AccountManage.chooseAccount();

            if (acc == null) {

                println("还没有添加用户 ~");
                main();
                return;

            }

            BlockScanner task = new BlockScanner(acc);
            task.exec();

            main();

        }

        static void cleanFollowers(TwiAccount acc) {

            Twitter api = acc.createApi();

            try {

                boolean hasNext = true;
                long cursor = -1;

                int index = inputInt("输入开启位置 默认1 : ");
                int z = 0;



                while (hasNext) {

                    IDs ids = api.getFollowersIDs(cursor);

                    long[] all = ids.getIDs();


                    for (;index - z - 1 < all.length;index ++) {

                        long id = all[index - 1];

                        User fo = api.showUser(id);

                        int weight = 0;

                        String log = "正在检查 [" + index + "]: " + fo.getName() + "(@" + fo.getScreenName() + ")";

                        if (fo.getStatusesCount() == 0) {
                            weight ++;
                            log += "\n没有任何推文";

                        }
                        if (fo.getDescription().trim().equals("")) {
                            weight ++;
                            log += "\n简介为空";
                        }
                        if (fo.isDefaultProfileImage()) {
                            weight ++;
                            log += "\n是默认头像";
                        }
                        if (fo.isProtected()) {
                            weight ++;
                            log += "\n是锁推用户";
                        }
                        if (fo.getFollowersCount() < 50 || fo.getFriendsCount() < 20) {
                            weight ++;
                            log += ("\n关注的人 : " + fo.getFriendsCount() + " 关注者 : " + fo.getFollowersCount());
                        }
                        if (fo.getFavouritesCount() == 0) {
                            weight ++;
                            log += ("\n没有任何打心");
                        }

                        printSplitLine();
                        println(log);


                        if (weight > 3) {

                            println("是否强制取关 ？");

                            if (confirm()) {

                                api.createBlock(fo.getId());
                                api.destroyBlock(fo.getId());

                                println("成功");

                            }

                        }




                    }

                    hasNext = ids.hasNext();

                    if (hasNext) {

                        cursor = ids.getNextCursor();
                        z += all.length ;

                    }

                }


            } catch (Exception e) {

                System.err.println(e);

            }

        }

    }

    static class AccountManage {

        static TwiAccount chooseAccount() {

            if (user.twiAccounts.size() == 0) return null;

            printSplitLine();

            println("选择账号 : ");

            int count = 0;


            LinkedList<TwiAccount> list =  new LinkedList<TwiAccount>(user.twiAccounts.values());

            for (TwiAccount acc : list) {

                count ++;
                println(count + ".[" + acc.name + "](@" + acc.screenName + ")");

            }

            int choose = choose();

            if (choose > count || choose < 1) {

                noSuchChoose();
                return chooseAccount();

            }

            return list.get(count - 1);


        }

        static void main() {

            printSplitLine();

            println("用户管理 :");
            println("0.返回主菜单");
            println("1.添加用户");

            int count = 1;

            LinkedList<TwiAccount> list =  new LinkedList<TwiAccount>(user.twiAccounts.values());

            for (TwiAccount acc : list) {
                count ++;
                println(count + ".管理用户[" + acc.name + "](@" + acc.screenName + ")");

            }

            println((count + 1) + ".刷新所有用户");

            int choose = choose();

            switch (choose) {

                case 0: mainMenu();return;
                case 1: addUser();return;

            }

            if (choose > count + 1 || choose < 0) {

                noSuchChoose();
                main();

            }

            if (choose == count + 1) {

                refreshAll();
                return;

            }

            acc = list.get(count - 2);

            manageAccount();

        }

        static TwiAccount acc;

        static void manageAccount() {

            printSplitLine();
            println("管理用户 : " + acc.name);
            println("用户名 : " + acc.screenName);
            println("账号ID : " + acc.accountId);

            println("0.返回用户管理");
            println("1.刷新用户");
            println("2.删除用户");

            switch (choose()) {

                case 0:main();return;
                case 1:refreshSingle(acc);manageAccount();return;
                case 2:removeAcc(acc);manageAccount();return;

            }

        }

        static void removeAcc(TwiAccount acc) {

            printSplitLine();
            println("要移除账号 : " + acc.name + "(@" + acc.screenName + ") 吗？");

            if (confirm()) {

                user.twiAccounts.remove(acc.accountId);
                user.save();
                println("已移除 ！");

            }

        }

        static void refreshAll() {

            printSplitLine();
            println("正在刷新所有...");

            for (TwiAccount acc : user.twiAccounts.values()) {

                refreshSingle(acc);

            }

            println("全部刷新完成..");
            main();

        }

        static void refreshSingle(TwiAccount acc) {

            printSplitLine();

            if (acc.refresh()) {

                println("[" + acc.name + "](@" + acc.screenName + ") 刷新成功");

            } else {

                println("[" + acc.name + "](@" + acc.screenName + ") 刷新失败！");
                println("可能是Token失效或无法连接Twitter 是否移除？");

                if (confirm()) {

                    user.twiAccounts.remove(acc.accountId);

                    println("已移除 : [" + acc.name + "](@" + acc.screenName + ")");

                }

            }

            user.save();




        }

        static void addUser() {

            printSplitLine();
            println("正添加用户  请选择Api :");

            println("0.退出添加用户");
            println("1.缺省 : Twitter for 奈间");
            println("2.Twidere for Android");

            int count = 2;

            for (ApiToken apiToken : user.apiTokens) {

                count ++;
                print(count + "." + apiToken.apiName);

            }

            int index = choose();

            if (index > count || index < 0) {

                noSuchChoose();
                addUser();

            }

            switch (index) {

                case 0 : main();return;
                case 1 :index = -1;break;
                case 2 :index = -2;break;
                default : index = index - 3;break;

            }

            token = user.getToken(index);

            typeChoose();

        }

        static ApiToken token;

        static void typeChoose() {

            printSplitLine();

            println("正在添加用户 :");
            println("选择的Api : " + token.apiName);
            println();
            println("0.返回上一步");
            println("1.继续添加 (手动输入Token)");
            println("2.继续添加 (浏览器OAuth认证)");
            println("3.取消添加");

            switch (choose()) {
                case 0 : addUser();return;
                case 1 : addByInput();return;
                case 2 : addByOAuth() ;return;
                case 3 : main();return;
            }

        }

        static void addByInput() {

            printSplitLine();
            println("选择的Api : " + token.apiName);
            println("选择的添加方式 : 手动输入Token");
            println();
            if (!confirm()) { typeChoose(); return; }

            String accToken = input("请输入 accToken : ");
            String accSecToken = input("请输入 accSecToken : ");

            printSplitLine();

            println("accToken : " + accToken);
            println("accSecToken : " + accSecToken);

            if (!confirm()) addByInput();

            TwiAccount acc = new TwiAccount(user, token.apiToken, token.apiSecToken, accToken, accSecToken);

            if (!acc.refresh()) {

                println("认证失败... 请检查网络或Token");
                typeChoose();
                return;

            }

            println("认证成功 ！ 登录的账号 : " + acc.name + " (@" + acc.screenName + ")");

            user.twiAccounts.put(acc.accountId, acc);
            user.save();
            main();

        }

        static void addByLogin() {

            printSplitLine();
            println("选择的Api : " + token.apiName);
            println("选择的添加方式 : 账号密码登录");
            println();
            if (!confirm()) { typeChoose(); return; }

            String name = input("请输入用户名(id) : ");
            String pswd = input("请输入密码 : ");

            printSplitLine();

            println("用户名 : " + name);
            println("密码 : " + pswd);

            if (!confirm()) addByInput();

            Twitter api = token.createApi();

            try {
                AccessToken accToken = api.getOAuthAccessToken(name, pswd);
                TwiAccount newAcc = new TwiAccount(user, token.apiToken, token.apiSecToken, accToken.getToken(), accToken.getTokenSecret());
                newAcc.accountId = accToken.getUserId();
                newAcc.screenName = accToken.getScreenName();
                println("认证成功 ！ 登录的账号 : " + newAcc.name + " (@" + newAcc.screenName + ")");

                user.twiAccounts.put(newAcc.accountId, newAcc);
                user.save();
                main();
            } catch (TwitterException e) {
                e.printStackTrace();
                println("认证失败... 请检查网络或账号密码");
                typeChoose();
                return;

            }


        }

        static void addByOAuth() {

            printSplitLine();
            println("选择的Api : " + token.apiName);
            println("选择的添加方式 : 浏览器OAuth验证");
            println();
            if (!confirm()) { typeChoose(); return; }

            Twitter api =  token.createApi();

            try {
                RequestToken req = (api.getOAuthRequestToken());

                println("请认证并复制认证后的地址 :");
                println(req.getAuthorizationURL());

                String url = input("请输入Url : ");



                HashMap<String, String> params = HttpUtil.decodeParamMap(StrUtil.subAfter(url, "?", true), "UTF-8");

                String requestToken = params.get("oauth_token");
                String oauthVerifier = params.get("oauth_verifier");

                println("verifier : " + oauthVerifier);

                if (oauthVerifier == null) {

                    println("无效的url！");
                    typeChoose();
                    return;

                }

                AccessToken accToken = api.getOAuthAccessToken(req, oauthVerifier);

                TwiAccount newAcc = new TwiAccount(user, token.apiToken, token.apiSecToken, accToken.getToken(), accToken.getTokenSecret());
                newAcc.refresh();
                println("认证成功 ！ 登录的账号 : " + newAcc.name + " (@" + newAcc.screenName + ")");

                user.twiAccounts.put(newAcc.accountId, newAcc);
                user.save();
                main();

            } catch (TwitterException e) {

                e.printStackTrace();
                typeChoose();
                return;

            }

        }

    }



    static class ApiManage {

        static void main() {

            printSplitLine();

            println("自定义Api : ");

            println("0.返回上级");
            println("1.新建Api");
            println("2.Api帮助");

            int count = 2;

            for (ApiToken apiToken : user.apiTokens) {

                count ++;

                println(count + ".管理Api[" + apiToken.apiName + "]");

            }

            int choose = choose();

            switch (choose) {

                case 0 : mainMenu();return;
                case 1 : newApi();return;
                case 2 : help();return;

            }

            if (choose > count || choose < 0) {

                noSuchChoose();
                main();

            } else {

                manageIndex = count - 3;
                manage();

            }



        }

        static void newApi() {

            printSplitLine();

            println("新建Api :");

            String apiName = input("输入Api的名称 : ");
            String apiToken = input("输入ApiToken : ");
            String apiSecToken = input("输入ApiSecToken :");

            printSplitLine();

            println("名称 : " + apiName);
            println("ApiToken : " + apiToken);
            println("ApiSecToken : " + apiSecToken);

            if (confirm()) {

                user.apiTokens.add(new ApiToken(apiName, apiToken, apiSecToken));
                user.save();
                main();

            } else {

                newApi();

            }

        }

        static void help() {

            printSplitLine();
            println("用于自定义推文显示的来源");
            println("可以在 develop.twitter.com 申请！");
            pause();
            main();

        }

        static int manageIndex;

        static void manage() {

            printSplitLine();

            ApiToken token = user.apiTokens.get(manageIndex);

            println("管理Api : " + token.apiName);
            println("ApiToken : " + token.apiToken);
            println("ApiSecToken : " + token.apiSecToken);
            println();
            println("0.返回Api管理");
            println("1.修改Api名称");
            println("2.修改ApiToken");
            println("3.修改ApiSecToken");
            println("4.删除Api");

            switch (choose()) {


                case 0 : main();return;
                case 1 : {
                        printSplitLine();
                        String newName = input("输入新名称 : ");
                        printSplitLine();
                        println("新名称 : " + newName);
                        while (!confirm()) {
                            newName = input("输入新名称 : ");
                            printSplitLine();
                            println("新名称 : " + newName);
                        }
                        token.apiName = newName;
                        user.save();
                        manage();
                        return;

                    }
                case 2 : {
                        printSplitLine();
                        String newToken = input("输入新Token : ");
                        printSplitLine();
                        println("新Token : " + newToken);
                        while (!confirm()) {
                            newToken = input("输入新Token : ");
                            printSplitLine();
                            println("新Token : " + newToken);
                        }
                        token.apiToken = newToken;
                        user.save();
                        manage();
                        return;

                    }
                case 3 : {
                        printSplitLine();
                        String newSecToken = input("输入新SecToken : ");
                        printSplitLine();
                        println("新SecToken : " + newSecToken);
                        while (!confirm()) {
                            newSecToken = input("输入新SecToken : ");
                            printSplitLine();
                            println("新SecToken : " + newSecToken);
                        }
                        token.apiSecToken = newSecToken;
                        user.save();
                        manage();
                        return;

                    }

                case 4 : {

                        println("即将删除Api : " + token.apiName);

                        if (confirm()) {

                            user.apiTokens.remove(token);
                            user.save();

                            println("删除成功！");

                            main();

                        }

                    }

            }

        }


    }

    static Scanner session = new Scanner(System.in);

    static int choose() {

        print("请选择 (输入数字) : ");

        return session.nextInt();

    }

    static void pause() {

        print("任意内容继续... : ");
        session.next();

    }

    static boolean confirm() {

        return !"n".equals(input("确定吗 ？ Y/n : ").toLowerCase());

    }

    static String input() {

        return session.next();

    }

    static String input(String msg) {

        System.out.print(msg);
        return session.next();

    }

    static int inputInt(String msg) {

        System.out.print(msg);
        return session.nextInt();

    }

    static void print(String msg) {

        System.out.print(msg);

    }

    static void println(String msg) {

        System.out.println(msg);

    }

    static void println() {

        System.out.println();

    }

    static void printSplitLine() {

        println("------------------------");

    }

    static void noSuchChoose() {

        printSplitLine();

        println("没有那样的选项");

    }


}
