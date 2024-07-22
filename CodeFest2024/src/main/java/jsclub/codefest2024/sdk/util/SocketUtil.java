package jsclub.codefest2024.sdk.util;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.*;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * It creates a socket connection with the server
 */
public class SocketUtil {
    private static final int TIMEOUT_IN_MINUTES = 1;

    /**
     * It creates a socket connection to the server.
     * 
     * @param url The url of the socket server.
     * @return A socket object
     */
    public static Socket init(String url) {
        OkHttpClient okHttpClient = getHttpClientBuilder();
        IO.setDefaultOkHttpCallFactory((Call.Factory) okHttpClient);
        IO.setDefaultOkHttpWebSocketFactory((WebSocket.Factory) okHttpClient);
        try {
            return IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * It creates a new OkHttpClient.Builder object and sets the timeout for the
     * connection, write and
     * read.
     * 
     * @return A new OkHttpClient object.
     */
    private static OkHttpClient getHttpClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES).writeTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
                .readTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
        return clientBuilder.build();
    }
}
