package com;

import java.lang.reflect.Method;

public class Util2 {
    public static int getEMUILevel(){
        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[] {
                    String.class
            });
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[] {
                    "ro.build.hw_emui_api_level"
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emuiApiLevel;
    }
}
