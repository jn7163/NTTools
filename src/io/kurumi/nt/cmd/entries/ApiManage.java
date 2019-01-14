package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;

public class ApiManage extends NTBaseCmd {
    
    public static ApiToken chooseApi(final NTUser user) {

        NTCM<ApiToken> cm = new NTCM<ApiToken>("Api")
        .add(ApiToken.defaultToken.apiName,ApiToken.defaultToken)
        .add(ApiToken.twidereToken.apiName,ApiToken.twidereToken);
        
        for (ApiToken token : user.apiTokens) {
            
            cm.add(token.apiName,token);
            
        }
        
        return cm.chooseItem();
        
        
    }
  
    public static void apply(final NTUser user, NTMenu menu) {


        final NTMenu apiManageMainMenu = menu.subMenu("Api管理");

        apiManageMainMenu.init = new Runnable() {

            @Override
            public void run() {

                apiManageMainMenu.clear().item(new NTMenu.Item("新建Api") {

                        @Override
                        public boolean run() {

                            String apiName = input("Api名称");
                            String apiToken = input("ApiToken");
                            String apiSecToken = input("ApiSecToken");

                            if (confirm()) {

                                user.apiTokens.add(new ApiToken(apiName, apiToken, apiSecToken));
                                user.save();
                                apiManageMainMenu.omsg("添加成功！");

                            }

                            return false;

                        }

                    });

                for (ApiToken apiToken : user.apiTokens) {

                    NTMenu apiManageMenu = apiManageMainMenu.subMenu("管理Api : " + apiToken.apiName);

                    buildApiManageMenu(user,apiManageMenu, apiToken);

                }


            }

        };


    }

    public static void buildApiManageMenu(final NTUser user,final NTMenu menu, final ApiToken token) {

        menu.msg = new Runnable() {

            @Override
            public void run() {

                println("管理Api : " + token.apiName);

                println();

                println("ApiToken : " + token.apiToken);
                println("ApiSecToken : " + token.apiSecToken);

                println();

            }

        };

        menu.item(new NTMenu.Item("修改Api名称") {

                @Override
                public boolean run() {


                    String newName = input("新ApiName");

                    if (confirm()) {

                        token.apiName = newName;
                        user.save();

                    }

                    return false;


                }

            });

        menu.item(new NTMenu.Item("修改ApiToken") {

                @Override
                public boolean run() {


                    String newToken = input("新ApiToken");
                    String newSecToken = input("新ApiSecToken");

                    if (confirm()) {

                        token.apiToken = newToken;
                        token.apiSecToken = newSecToken;
                        user.save();

                    }

                    return false;

                }

            });

        menu.item(new NTMenu.Item("删除该Api (您将失去这个API 真的很久！)") {

                @Override
                public boolean run() {

                    if (confirm()) {

                        user.apiTokens.remove(token);
                        user.save();

                        menu.omsg("删除成功！");

                    }

                    return true;

                }

            });

    }


    
    
}
