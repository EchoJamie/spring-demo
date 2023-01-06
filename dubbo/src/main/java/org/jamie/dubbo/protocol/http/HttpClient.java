package org.jamie.dubbo.protocol.http;

import org.apache.commons.io.IOUtils;
import org.jamie.dubbo.protocol.Invocation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author jamie
 * @version 1.0.0
 * @description Http请求连接器
 * @date 2023/01/02 13:02
 */
public class HttpClient {

    public String send(String hostName, Integer port, Invocation invocation) throws IOException {
        URL url = new URL("http", hostName, port, "/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);

        oos.writeObject(invocation);
        oos.flush();
        oos.close();

        InputStream inputStream = httpURLConnection.getInputStream();
        String result = IOUtils.toString(inputStream);
        return result;
    }
}
