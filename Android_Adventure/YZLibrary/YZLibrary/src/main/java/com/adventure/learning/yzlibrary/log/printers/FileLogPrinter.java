package com.adventure.learning.yzlibrary.log.printers;

import androidx.annotation.NonNull;

import com.adventure.learning.yzlibrary.log.LogConfig;
import com.adventure.learning.yzlibrary.log.LogItemModel;
import com.adventure.learning.yzlibrary.log.LogPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class FileLogPrinter implements LogPrinter {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    /**
     * 日志文件路径
     */
    private final String logPath;
    /**
     * 日志文件保留时间
     */
    private final long retentionTime;
    /**
     * 日志打印线程
     */
    private volatile PrintWorker printWorker;
    /**
     * 日志写入工具类
     */
    private LogWriter logWriter;
    /**
     * 单例
     */
    public static FileLogPrinter instance;
    /**
     * 单例
     * @param logPath 日志文件路径， 如果是外部路径，需要确保已经有外部存储的读写权限
     * @param retentionTime 日志文件保留时间, 单位毫秒， <=0 表示一直有效，无需清理
     * @return
     */
    public static synchronized FileLogPrinter getInstance(String logPath, long retentionTime) {
        if (instance == null) {
            instance = new FileLogPrinter(logPath, retentionTime);
        }
        return instance;
    }

    private FileLogPrinter(String logPath, long retentionTime) {
        if (logPath != null) {
            this.logPath = logPath.trim();
        } else {
            this.logPath = "/data/data/com.adventure.learning.library/cache/logs";
        }
        this.retentionTime  = retentionTime;
        this.printWorker = new PrintWorker();
        this.logWriter = new LogWriter();
        cleanExpiredLog();
    }
    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        long timeMillis = System.currentTimeMillis();
        if (!printWorker.isRunning()) {
            printWorker.start();
        }
        printWorker.put(new LogItemModel(timeMillis, level, tag, printString));
    }


    void doPrint(LogItemModel log) {
        String lastFileName = logWriter.getPreFileName();
        if (lastFileName == null) {
            String newFileName = genFileName();
            if (logWriter.isReady()) {
                logWriter.close();
            }
            if (!logWriter.ready(newFileName)) {
                return;
            }
        }
        logWriter.append(log.getFlatenedLog());
    }

    /**
     * 清除过期log
     */
    private void cleanExpiredLog() {
        if (retentionTime <= 0) {
            return;
        }
        long currenTimeMillis = System.currentTimeMillis();
        File logDir = new File(logPath);
        if (!logDir.exists()) {
            return;
        }
        File[] files = logDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                long lastModified = file.lastModified();
                if (currenTimeMillis - lastModified > retentionTime) {
                    file.delete();
                }
            }
        }
    }

    private String genFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
        return sdf.format(new Date(System.currentTimeMillis())) + ".log";
    }


    private class PrintWorker implements Runnable {
        private BlockingQueue<LogItemModel> logs = new LinkedBlockingQueue<>();

        private volatile boolean running;
        /**
         * 向队列中添加日志
         * @param log
         */
        void put(LogItemModel log) {
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /**
         * 是否正在运行
         * @return true 在运行
         */
        boolean isRunning() {
            synchronized (this) {
                return running;
            }
        }
        /**
         * 启动线程
         */
        void start() {
            synchronized (this) {
                EXECUTOR_SERVICE.execute(this);
                running = true;
            }
        }



        @Override
        public void run() {
            LogItemModel log;
            try {
                while (true) {
                    log = logs.take();
                    doPrint(log);
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 基于BufferedWriter将log写入文件
     */
    private class LogWriter {
        private String preFileName;
        private File logFile;
        private BufferedWriter bufferedWriter;

        boolean isReady() {
            return bufferedWriter != null;
        }

        String getPreFileName() {
            return preFileName;
        }

        boolean ready(String newFileName) {
            preFileName = newFileName;
            logFile = new File(logPath, newFileName);
            // 创建文件
            if (!logFile.exists()) {
                try {
                    File parent = logFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    preFileName = null;
                    logFile = null;
                    return false;
                }
            }

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (Exception e) {
                e.printStackTrace();
                preFileName = null;
                logFile = null;
                return false;
            }
            return true;
        }
        /**
         * 关闭文件
         * @return
         */
        boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    bufferedWriter = null;
                    preFileName = null;
                    logFile = null;
                }
            }
            return true;
        }
        /**
         * 将 log 写入文件
         * @param flattenedLog 格式化之后的日志
         */
        void append(String flattenedLog) {
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
