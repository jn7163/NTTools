package io.kurumi.nt.cmd.entries;

import io.kurumi.nt.*;
import io.kurumi.nt.cmd.*;

public class UserManage extends NTBaseCmd {
    
    public static void apply(final NTUser user,final NTMenu menu) {
        
        final NTMenu userManageMenu = menu.subMenu("账号管理");
        
        userManageMenu.init = new Runnable() {

            @Override
            public void run() {
               
                userManageMenu.clear().item(new NTMenu.Item("添加账号") {
                    
                        @Override
                        public boolean run() {
                            
                            
                            
                            return false;
                            
                        }
                        
                    });
                
            }
            
        };
        
    }
    
}
