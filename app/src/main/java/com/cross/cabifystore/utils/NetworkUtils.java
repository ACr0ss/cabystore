package com.cross.cabifystore.utils;

import java.net.InetSocketAddress;
import java.net.Socket;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkUtils {
    public static Single<Boolean> hasInternetConnection() {
        return Single.fromCallable(() -> {
            try {
                int timeoutMs = 1500;
                Socket socket = new Socket();
                InetSocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

                socket.connect(socketAddress, timeoutMs);
                socket.close();

                return true;
            } catch (Exception e) {
                return false;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


}
