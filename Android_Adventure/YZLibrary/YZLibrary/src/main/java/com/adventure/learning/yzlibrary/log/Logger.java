package com.adventure.learning.yzlibrary.log;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

//TODO:
// 1. 打印堆栈信息
// 2. File输出
// 3. 控制台输出

public class Logger {
    private static final String IGNORE_PACKAGE_NAME;

    static {
        String className = Logger.class.getName();
        IGNORE_PACKAGE_NAME = className.substring(0, className.lastIndexOf('.') + 1);
    }
    public static void v(Object... args) {
        log(LogType.V, args);
    }
    public static void d(Object... args) {
        log(LogType.D, args);
    }
    public static void i(Object... args) {
        log(LogType.I, args);
    }
    public static void w(Object... args) {
        log(LogType.W, args);
    }
    public static void e(Object... args) {
        log(LogType.E, args);
    }
    public static void a(Object... args) {
        log(LogType.A, args);
    }

    public static void vt(String tag, Object... args) {
        log(LogType.V, tag, args);
    }

    public static void dt(String tag, Object... args) {
        log(LogType.D, tag, args);
    }

    public static void it(String tag, Object... args) {
        log(LogType.I, tag, args);
    }

    public static void wt(String tag, Object... args) {
        log(LogType.W, tag, args);
    }

    public static void et(String tag, Object... args) {
        log(LogType.E, tag, args);
    }

    public static void at(String tag, Object... args) {
        log(LogType.A, tag, args);
    }

    public static void log(@LogType.TYPE int type, Object... args) {
        log(type, LogManager.getInstance().getConfig().getGlobalTag(), args);
    }

    public static void log(@LogType.TYPE int type, String tag, Object... args) {
        log(LogManager.getInstance().getConfig(), type, tag, args);
    }

    public static void log(@NonNull LogConfig config, @LogType.TYPE int type, @NonNull String tag, Object... args) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = LogConfig.THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTraceInfo = LogConfig.STACK_TRACE_FORMATTER.format(StackTraceUtil.getCroppedRealStackTrace(
                new Throwable().getStackTrace(),
                IGNORE_PACKAGE_NAME,
                config.stackTraceDepth()
            ));
            sb.append(stackTraceInfo).append("\n");
        }
        String body = parseBody(args, config);
        sb.append(body);
        Log.println(type, tag, sb.toString());

        List<LogPrinter> printers = config.getPrinters()!= null ? config.getPrinters() : null;
        if (printers == null) {
            return;
        }
        for (LogPrinter printer : printers) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, LogConfig config) {
        if (config.injectJsonParser() != null) {
            return config.injectJsonParser().toJSON(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object content : contents) {
            sb.append(content.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
