package com.liu.Account.commonUtils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author maikle hj
 * @version 创建时间：2014-12-3 上午9:06:42 类说明
 */
public final class LogUtil {
    private static boolean sIsLogEnabled = true;// 是否打开LOG

    // 当前文件名 行号 函数名
    public static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        return toStringBuffer.toString();
    }

    // 当前文件名
    public static String FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getFileName();
    }

    // 当前方法名
    public static String FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    // 当前行号
    public static int LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    // 当前时间
    @SuppressLint("SimpleDateFormat") public static String TIME_() {
        Date now = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    public static void v(String msg) {
        if (!sIsLogEnabled)
            return;
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        String TAG = toStringBuffer.toString();
        Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (!sIsLogEnabled)
            return;
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        String TAG = toStringBuffer.toString();
        Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (!sIsLogEnabled)
            return;
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        String TAG = toStringBuffer.toString();
        Log.i(TAG, msg);

    }

    public static void w(String msg) {
        if (!sIsLogEnabled)
            return;
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        String TAG = toStringBuffer.toString();
        Log.w( TAG, msg);
    }

    public static void e(String msg) {
        if (!sIsLogEnabled)
            return;
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ").append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("()").append("]");
        String TAG = toStringBuffer.toString();
        Log.e(TAG, msg);
    }
}