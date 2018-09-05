package com.hy.winUtil_gui;

import java.util.List;


import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
  
  
  
public class KeyboardHook implements Runnable{  
  
    private static HHOOK hhk;  
    private static LowLevelKeyboardProc keyboardHook;  
    final static User32 lib = User32.INSTANCE; 
    
    private boolean [] on_off=null;  //开关
    
	private String path = "C:\\Users\\Administrator\\Desktop\\icon\\";
	private boolean ding = false;
	
	public KeyboardHook(boolean [] on_off){ 
        this.on_off = on_off;
    }
    
    private boolean LB = false;
    private boolean MB = false;
	
	
    public void run() {
    	final ImageApp imageApp = ImageApp.getImageApp();
    	final List<String> fileName = Win_Dos.getFileName();
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        
        //键盘监听
        keyboardHook = new LowLevelKeyboardProc() {  
            public synchronized LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT event) {  
            	if (on_off[0] == false) {
            		System.exit(0);  
            	}
            	imageApp.setVisible(imageApp.isFlag()); //是否显示
	            	if(WinUser.WM_KEYDOWN == wParam.intValue() && event.vkCode == 117){
	            		MB = true;
	            	}else if(WinUser.WM_KEYUP == wParam.intValue() && event.vkCode == 117){
	            		MB = false;
	            	}
	            	if(WinUser.WM_KEYDOWN == wParam.intValue() && event.vkCode == 118){
	            		LB = true;
	            	}else if(WinUser.WM_KEYUP == wParam.intValue() && event.vkCode == 118){
	            		LB = false;
	            	}
	            	if(LB && MB){ //当中键和左键按住的时候
	            		imageApp.setDing(ding=!ding);
	            	}
            	if (nCode >= 0 &&  (WinUser.WM_SYSKEYDOWN == wParam.intValue() || WinUser.WM_KEYDOWN == wParam.intValue())) {  //WM_SYSKEYDOWN 系统按键      WM_KEYDOWN 普通按键
            	
//            		if(event.vkCode == 117){imageApp.setDing(ding=!ding);} //  定住窗口
            		if(event.vkCode == 119){exe("cmd.exe /C "+path+"/myeclipse.lnk");} //F8
            		if(event.vkCode == 120){exe("cmd.exe /C "+path+"/腾讯QQ.lnk");} //F9
            		if(event.vkCode == 121){exe("cmd.exe /C "+path+"/微信.lnk");} //F10
            		if(imageApp.isFlag()){ //图形显示判断是否开启
            			//对应1-9数字
	            		for (int i = 0; i < 9; i++) {
							if(event.vkCode == i+49){
								exe("cmd.exe /C "+ path + "/"+fileName.get(i));
							}
						}
            		}
                
            	}
                return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(Pointer.nativeValue(event.getPointer())));
            }  
        };
        hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardHook, hMod, 0);  //0为线程标示符  , 0则与当前现存线程
        int result;  
        MSG msg = new MSG();  
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {  
            if (result == -1) {
                System.err.println("error in get message");  
                break;  
            } else {  
                System.err.println("got message");  
                lib.TranslateMessage(msg);  
                lib.DispatchMessage(msg);  
            }  
        }  
        lib.UnhookWindowsHookEx(hhk);  
    }  
  
    
    public void exe(String path){
    	Runtime rt = Runtime.getRuntime();
    	try {
			rt.exec(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    

}  