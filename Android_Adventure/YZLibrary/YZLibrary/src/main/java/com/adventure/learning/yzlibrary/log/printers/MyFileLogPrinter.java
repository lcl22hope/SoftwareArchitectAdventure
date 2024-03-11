package com.adventure.learning.yzlibrary.log.printers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.adventure.learning.yzlibrary.log.LogConfig;
import com.adventure.learning.yzlibrary.log.LogItemModel;
import com.adventure.learning.yzlibrary.log.LogPrinter;
import com.adventure.learning.yzlibrary.log.LogType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实现一个扩展实现文件打印模块
 * 要求：
 * 1. 能够将日志格式化后存储到手机缓存中
 * 2. 能够复用线程防止频繁的创建线程
 * 3. 支持清除过期的日志
 * 提示：
 * 1. 实现LogPrinter接口
 * 2. 可以使用BlockingQueue，防止频繁的创建线程
 * 3. 文件操作可以使用BufferedWriter
 *
 *
 *
 * 需求拆解：
 * 1. 首先需要定义一个实体类用于代表日志，这里我们已经定义好了就是 LogItemModel
 * 2. 要求实现 LogPrinter 接口，这个没有难度
 * 3. 要求能够将日志格式化后存储到📱缓存中，这个 API 我不熟悉，涉及到 File、BufferedWriter
 * 4. BlockingQueue 用于存储日志条目，后台线程会从中取出并处理这些条目，BlockingQueue 是线程安全的，适合生产者、消费者模式
 * 5. 创建后台线程以循环处理 BlockingQueue 中的日志条目
 * 6. 使用 BufferedWriter 写入日志到文件系统
 *  * 6.1 后台线程使用 ExcutorService 来创建一个单线程的执行器，这个线程会循环处理队列中的日志条目。后台线程的任务是在循环中获取 BlockingQueue 中的日志条目，并将日志条目写入到文件中
 * 7. 实现清除过期的日志
 *  * 7.1 定期检查日志文件的最后修改时间，删除那些超过特定时间阈值的日志文件。这个逻辑可以在每次写入日志后执行
 * 8. 编写测试 case
 *  * 8.1 验证日志是否被写入到文件中,验证并发处理文件写入、文件操作、定期清理等场景
 * */
public class MyFileLogPrinter implements LogPrinter {
    /**
     * 日志目录名称
     */
    private static final String LOG_DIRECTORY_NAME = "logs";
    /**
     * 日志目录
     * */
    private static File logDirectory;
    /**
     * 日志过期时间 7天
     * */
    private static final long LOG_EXPIRATION_MILLIS = 7 * 24 * 60 * 60 * 1000L;
    /**
     * 日志队列
     * */
    private BlockingQueue<Runnable> logQueue;
    /**
     * 核心线程数，即使空闲也会保留在线程池中的线程数
     * */
    private static final int corePoolSize = 1;
    /**
     * 线程中允许的最大线程数
     * */
    private static final int maximumPoolSize = 1;
    /**
     * 当线程数大于核心时，这是多余的空闲线程终止前等待任务的最长时间
     * */
    private static long keepAliveTime = 0L;

    private ThreadPoolExecutor executorService;
    /**
     * 日志日期格式化
     * */
    private SimpleDateFormat dateFormat;

    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        LogItemModel log = new LogItemModel(System.currentTimeMillis(), level, tag, printString);
        executorService.execute(new Task(log));
    }


    public MyFileLogPrinter(Context context) {
        // 获取缓存路径 /data/data/com.adventure.learning.yzlibrary/cache
        File cacheDir = context.getCacheDir();
        // 创建日志目录
        logDirectory = new File(cacheDir, LOG_DIRECTORY_NAME);
        // 如果目录不存在，则创建
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        // 创建日志队列
        logQueue = new LinkedBlockingDeque<>();
        // 创建后台线程
        executorService = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.MILLISECONDS,
            logQueue
        );

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        // 创建日志日期格式化
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

        executorService.submit(new Task(new LogItemModel(System.currentTimeMillis(), LogType.V, "MyFileLogPrinter", "MyFileLogPrinter init")));
    }

    static class Task implements Runnable {
        LogItemModel log;
        Task(LogItemModel log) {
            this.log = log;
        }
        @Override
        public void run() {
            processLog();
        }

        /**
         * 从队列中循环取出日志条目，然后写入到文件中，并且清理过期的日志
         * */
        private void processLog() {
            writeLogToFile(this.log);
            clearExpiredLogs();
        }


        /**
         * 写入日志到文件
         * */
        private void writeLogToFile(LogItemModel log) {
            File logFile = new File(logDirectory, "log_" + System.currentTimeMillis() + ".log");
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(log.getFlatenedLog());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 清理过期日志文件
         * */
        private void clearExpiredLogs() {
            File[] files = logDirectory.listFiles();
            if (files != null) {
                long now = System.currentTimeMillis();
                for (File file: files) {
                    if (now - file.lastModified() > LOG_EXPIRATION_MILLIS) {
                        file.delete();
                    }
                }
            }
        }
    }
}
