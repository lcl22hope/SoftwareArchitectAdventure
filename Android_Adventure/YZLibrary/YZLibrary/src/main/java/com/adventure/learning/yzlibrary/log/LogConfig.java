package com.adventure.learning.yzlibrary.log;

import java.util.ArrayList;
import java.util.List;

public abstract class LogConfig {
    // 定义日志显示每行最大的字符数
    static int MAX_LEN = 512;

    static StackTraceFormatter STACK_TRACE_FORMATTER = new StackTraceFormatter(); // 懒汉模式创建单例
    static ThreadFormatter THREAD_FORMATTER = new ThreadFormatter(); // 懒汉模式创建单例

    /**
     * json 序列化功能的注入实现
     * */
    public JsonParser injectJsonParser() {
        return null;
    }

    /**
     * 为了不仅打印字符串信息数据，还能够打印任何形式的数据，需要引入对象序列化功能
     * 这样做的好处：通过定义一个接口，将对象的序列化交给调用者来实现，这样序列化的功能就能和整个框架解耦
     * */
    public interface JsonParser {
        String toJSON(Object stc);
    }

    public LogPrinter[] printers() {
        return null;
    }

    private List<LogPrinter> printers = new ArrayList<>();


    public String getGlobalTag() {
        return "YuzhaoLog";
    };
    public boolean enable() {
        return true;
    }
    public boolean includeThread() {
        return false;
    };
    public int stackTraceDepth() {
        return 5; // 最多打印5层
    }
}
