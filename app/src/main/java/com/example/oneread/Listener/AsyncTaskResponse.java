package com.example.oneread.Listener;

public interface AsyncTaskResponse {
    void processFinish(String output);

    void downloadFinish(String output);
}
