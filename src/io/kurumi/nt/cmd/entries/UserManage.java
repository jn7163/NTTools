package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;
import twitter4j.*;
import twitter4j.auth.*;
import java.util.*;
import cn.hutool.http.*;
import cn.hutool.core.util.*;

public class UserManage extends NTBaseCmd {

    public static void apply(final NTUser user, final NTMenu menu) {

        final NTMenu userManageMainMenu = menu.subMenu("账号管理");

        userManageMainMenu.init = new Runnable() {

            @Override
            public void run() {

                userManageMainMenu.clean();

                for (Map.Entry<Long,TwiAccount> acc : user.twiAccounts.entrySet()) {

                    NTMenu userManageMenu = userManageMainMenu.subMenu("管理账号 : " + acc.getValue().getFormatedName());
                    buildManageMenu(user, userManageMenu, acc.getValue());

                }

                userManageMainMenu.item(new NTMenu.Item("添加账号") {

                        @Override
                        public boolean run() {

                            if (addUser(user) == null) {

                                userManageMainMenu.omsg("添加失败...");

                            }

                            return false;

                        }

                    });

            }

        };



    }

    public static void buildManageMenu(final NTUser user, final NTMenu menu, final TwiAccount acc) {

        menu.msg = new Runnable() {

            @Override
            public void run() {

                println("管理账号 : ");
                println();
                println("用户ID : " + acc.accountId);
                println("名称 : " + acc.name);
                println("用户名 : " + acc.screenName);
                //    println("邮箱 : " + acc.email);
                println();


            }

        };

        menu.init = new Runnable() {

            @Override
            public void run() {

                menu.clean().item(new NTMenu.Item("刷新账号") {

                        @Override
                        public boolean run() {

                            if (!acc.refresh()) {

                                menu.omsg("刷新失败！ 请检查网络或Token是否失效...");

                            } else {

                                menu.omsg("刷新成功！");

                            }

                            return false;

                        }

                    });

                menu.item(new NTMenu.Item("删除账号") {

                        @Override
                        public boolean run() {

                            if (confirm("真的要删除吗？")) {

                                user.twiAccounts.remove(acc);
                                user.save();

                                menu.omsg("删除账号成功！");

                                return true;

                            }

                            return false;
                        }

                    });

            };

        };

    }


    public static TwiAccount chooseAccount(final NTUser user) {

        NTCD<TwiAccount> cd = new NTCD<TwiAccount>("账号");

        for (Map.Entry<Long,TwiAccount> acc : user.twiAccounts.entrySet()) {

            cd.add(acc.getValue().getFormatedName() , acc.getValue());

        }

        cd.add("添加新账号", new FN<TwiAccount>() {

                @Override
                public TwiAccount invoke() {
                    TwiAccount ntk = addUser(user);
                    if (ntk == null) return chooseAccount(user);
                    else return ntk;
                }

            });

        return cd.invoke();


    }

    public static TwiAccount addUser(final NTUser user) {

        return new NTCD<TwiAccount>("添加账号 : ")

            .add("取消添加", new FN<TwiAccount>() {

                @Override
                public TwiAccount invoke() {

                    clear();
                    printSplitLine();
                    println("取消添加！");

                    return null;
                }
            })
            .add("手动输入Token", new FN<TwiAccount>() {

                @Override
                public TwiAccount invoke() {

                    return addByInputToken(user);

                }

            })
            .add("浏览器OAuth认证", new FN<TwiAccount>() {

                @Override
                public TwiAccount invoke() {

                    return addByBrowserOAuth(user);

                }


            })
            .invoke();

    }

    private static TwiAccount addByInputToken(NTUser user) {

        ApiToken token = ApiManage.chooseApi(user);

        clear();

        printSplitLine();

        println("添加账号 (手动输入Token) :");
        println();

        String accToken = input("AccToken");
        String accSecToken = input("AccSecToken");


        if (!confirm()) {

            return null;

        }

        TwiAccount acc = new TwiAccount(user, token.apiToken, token.apiSecToken, accToken, accSecToken);

        if (!acc.refresh()) {


            println("网络错误或Token无效！");

            if (confirm("是否重试？")) return addByInputToken(user);

            return null;

        }

        user.twiAccounts.put(acc.accountId, acc);
        user.save();

        return acc;

    }

    private static TwiAccount addByBrowserOAuth(NTUser user) {

        ApiToken token = ApiManage.chooseApi(user);

        clear();

        printSplitLine();

        println("添加账号 (浏览器OAuth) :");
        println();


        Twitter api = token.createApi();

        try {

            RequestToken req = api.getOAuthRequestToken();

            println("请认证后输入跳转到的地址 (本地Url) :");
            println(req.getAuthenticationURL());

            String url = input("请输入Url : ");
            HashMap<String, String> params = HttpUtil.decodeParamMap(StrUtil.subAfter(url, "?", true), "UTF-8");

            String requestToken = params.get("oauth_token");
            String oauthVerifier = params.get("oauth_verifier");

            // println("verifier : " + oauthVerifier);

            if (oauthVerifier == null) {

                println("无效的url！");

                if (confirm("是否重试？")) {

                    return addByBrowserOAuth(user);

                }

                return null;

            }

            AccessToken accToken = api.getOAuthAccessToken(req, oauthVerifier);

            TwiAccount newAcc = new TwiAccount(user, token.apiToken, token.apiSecToken, accToken.getToken(), accToken.getTokenSecret());
            newAcc.refresh();
            println("认证成功 ！ 登录的账号 : " + newAcc.name + " (@" + newAcc.screenName + ")");

            user.twiAccounts.put(newAcc.accountId, newAcc);
            user.save();

            return newAcc;


        } catch (TwitterException e) {

            if (e.isCausedByNetworkIssue()) {

                println("网络连接失败..");

            } else {

                println("认证失败..");

            }

            if (confirm("要重试吗？")) {

                return addByBrowserOAuth(user);

            }

            return null;

        }


    }


}
