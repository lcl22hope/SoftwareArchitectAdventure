package com.adventure.learning.yzlibrary.log;

public interface LogFormatter<T>{
    // 定义一个 format 方法
    String format(T data);
}
