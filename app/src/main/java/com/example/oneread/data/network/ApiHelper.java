package com.example.oneread.data.network;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApiHelper {

    private IServiceAPI iServiceAPI;

    @Inject
    public ApiHelper(IServiceAPI iServiceAPI) {
        this.iServiceAPI = iServiceAPI;
    }
}
