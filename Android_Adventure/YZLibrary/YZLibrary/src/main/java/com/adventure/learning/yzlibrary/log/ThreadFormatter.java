package com.adventure.learning.yzlibrary.log;

public class ThreadFormatter implements LogFormatter<Thread> {
    @Override
    public String format(Thread thread) {
        return "Thread: " + thread.getName();
    }
}
