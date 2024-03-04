package com.adventure.learning.yzlibrary.log;

public class StackTraceUtil {

    /**
     * 裁剪堆栈信息
     * @param maxDepth 最大深度
     * @param callStack 堆栈信息
     * @return 裁剪后的堆栈信息
     *
     * */
    private static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack, int maxDepth) {
        //获取堆栈的真实深度
        int realDepth = callStack.length;
        if (maxDepth > 0) {
            realDepth = Math.min(realDepth, maxDepth);
        }
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        //将堆栈的拷贝到一个新的数组
        System.arraycopy(callStack, 0, realStack, 0, realDepth);
        return realStack;
    }

    /**
     * 获取除忽略包之外的堆栈信息
     *
     * */
    private static StackTraceElement[] getRealStackTrace(StackTraceElement[] callStack, String ignorePackageName) {
        // 默认忽略的深度
        int ignoreDepth = 0;
        // 堆栈深度
        int allDepth = callStack.length;
        String className;
        for (int i = allDepth -1; i >= 0; i--) {
            className = callStack[i].getClassName();
            if (ignorePackageName != null && className.startsWith(ignorePackageName)) {
                ignoreDepth = i + 1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(callStack, ignoreDepth, realStack, 0, realDepth);
        return realStack;
    }

    public static StackTraceElement[] getCroppedRealStackTrace(StackTraceElement[] stackTrace, String ignorePackageName, int maxDepth) {
        StackTraceElement[] realStack = getRealStackTrace(stackTrace, ignorePackageName);
        return cropStackTrace(realStack, maxDepth);
    }
}
