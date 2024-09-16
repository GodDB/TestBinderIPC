package com.example.shared;

import com.example.shared.ICallback;

interface IExampleService {
    void registerCallback(ICallback callback);
    void sendMessageToServer(String message);
}