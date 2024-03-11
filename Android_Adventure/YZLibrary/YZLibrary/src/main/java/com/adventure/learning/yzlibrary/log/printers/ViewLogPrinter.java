package com.adventure.learning.yzlibrary.log.printers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventure.learning.yzlibrary.R;
import com.adventure.learning.yzlibrary.log.LogConfig;
import com.adventure.learning.yzlibrary.log.LogItemModel;
import com.adventure.learning.yzlibrary.log.LogPrinter;
import com.adventure.learning.yzlibrary.log.LogType;
import com.adventure.learning.yzlibrary.log.ViewLogPrinterProvider;

import java.util.ArrayList;
import java.util.List;

public class ViewLogPrinter implements LogPrinter {
    private LogAdapter adapter;
    private RecyclerView recyclerView;

    private ViewLogPrinterProvider viewLogPrinterProvider;

    public ViewLogPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        this.recyclerView = new RecyclerView(activity);
        this.adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewLogPrinterProvider = new ViewLogPrinterProvider(rootView, recyclerView);
    }
    /**
     * 获取 viewLogPrinterProvider, 通过viewLogPrinterProvider 可以控制 log 视图的展示与隐藏
     * */
    public ViewLogPrinterProvider getViewLogPrinterProvider() {
        return viewLogPrinterProvider;
    }

    /**
     * 将 log 可视化输出
     * */
    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        //将 log 展示添加到 recyclerView
        adapter.addItem(new LogItemModel(System.currentTimeMillis(), level, tag, printString));
        // 滚动到对应的未知
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private LayoutInflater inflater;
        private List<LogItemModel> logList = new ArrayList<>();

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(LogItemModel item) {
            logList.add(item);
            notifyItemInserted(logList.size() - 1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.log_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            LogItemModel item = logList.get(position);
            int color = getHighLightColor(item.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);


            holder.tagView.setText(item.getFlattended());
            holder.messageView.setText(item.log);
        }
        /**
         * 根据 log
         * */
        private int getHighLightColor(int logLevel) {
            int highLight;
            switch (logLevel) {
                case LogType.D:
                    highLight = R.color.log_d;
                    break;
                case LogType.I:
                    highLight = R.color.log_i;
                    break;
                case LogType.W:
                    highLight = R.color.log_w;
                    break;
                case LogType.E:
                    highLight = R.color.log_e;
                    break;
                case LogType.A:
                    highLight = R.color.log_a;
                    break;
                default:
                    highLight = R.color.log_v;
                    break;
            }
            return highLight;
        }

        @Override
        public int getItemCount() {
            return logList.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
