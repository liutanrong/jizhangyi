package com.liu.Account.commonUtils;

import java.util.Map;
import java.util.Set;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
/**
 * @author liutanrong
 * @email liutanrong0425@163.com
 * 本类是一个SharedPreferences快速储存类
 * PrefsUtils test=new PrefsUtils(MainActivity.this, "test2", MODE_PRIVATE);
test.putString("test", "test1");
test.getString("test");
 * */
public class PrefsUtil {
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    /**
     * @param context 上下文
     * @param prefsname 储存文件的名称
     * @param mode 储存模式
     * */
    public PrefsUtil(Context context, String prefsname, int mode) {

        this.context = context;
        this.prefs = this.context.getSharedPreferences(prefsname, mode);
        this.editor = this.prefs.edit();
    }
    /**
     * @param key 储存的key
     * @param defaultVal 如果没有该key对应的值时取出的值，默认为false
     * */
    public boolean getBoolean(String key, boolean defaultVal){
        return this.prefs.getBoolean(key, defaultVal);
    }
    public boolean getBoolean(String key){
        return this.prefs.getBoolean(key, false);
    }

    /**
     * @param key 储存的key
     * @param defaultVal 如果没有该key对应的值时取出的值，默认为null
     * */
    public String getString(String key, String defaultVal){
        return this.prefs.getString(key, defaultVal);
    }
    public String getString(String key){
        return this.prefs.getString(key, null);
    }
    /**
     * @param key 储存的key
     * @param defaultVal 如果没有该key对应的值时取出的值，默认为0
     * */
    public int getInt(String key, int defaultVal){
        return this.prefs.getInt(key, defaultVal);
    }
    public int getInt(String key){
        return this.prefs.getInt(key, 0);
    }

    /**
     * @param key 储存的key
     * @param defaultVal 如果没有该key对应的值时取出的值，默认为0f
     * */
    public float getFloat(String key, float defaultVal){
        return this.prefs.getFloat(key, defaultVal);
    }
    public float getFloat(String key){
        return this.prefs.getFloat(key, 0f);
    }
    /**
     * @param key 储存的key
     * @param defaultVal 如果没有该key对应的值时取出的值，默认为0l
     * */
    public long getLong(String key, long defaultVal){
        return this.prefs.getLong(key, defaultVal);
    }
    public long getLong(String key){
        return this.prefs.getLong(key, 0l);
    }
    /**
     * 取出所有储存的值到map
     * */
    public Map<String, ?> getAll(){
        return this.prefs.getAll();
    }

    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    public void putString(String key, String value){
        editor=this.prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    public void putInt(String key, int value){
        editor=this.prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    public void putFloat(String key, float value){
        editor=this.prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    public void putLong(String key, long value){
        editor=this.prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    public void putBoolean(String key, boolean value){
        editor=this.prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param key 储存的key
     * @param value 要储存的数据
     * */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putStringSet(String key, Set<String> value){
        editor=this.prefs.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
    /**
     * @param key 储存的key
     * */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal){
        return this.prefs.getStringSet(key, defaultVal);
    }
    /**
     * @param key 储存的key
     * */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key){
        return this.prefs.getStringSet(key, null);
    }
    /**
     * 是否包含key
     * @param key	key关键字
     * @return	包含返回true；反之返回false
     */
    public boolean containsKey(String key){
        return prefs.contains(key);
    }
    /**
     * 删除关键字key
     * @param key		关键字key
     * @return 成功返回true，失败返回false
     */
    public boolean removeKey(String key){
        editor=this.prefs.edit();
        return editor.remove(key).commit();
    }

    /**
     * 清除所有的关键字
     * @return 成功返回true，失败返回false
     */
    public boolean clearValues(){
        editor=this.prefs.edit();
        return editor.clear().commit();
    }
}