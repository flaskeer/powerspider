package com.hao.spider.http;

import com.googlecode.jsonrpc4j.JsonRpcClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by donghao on 16/8/16.
 */
public class TestJsonRpc {

    static class Request implements Serializable{
        String hostname;
        String ip;
        String agentVersion;
        String pluginVersion;

        public Request() {
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getAgentVersion() {
            return agentVersion;
        }

        public void setAgentVersion(String agentVersion) {
            this.agentVersion = agentVersion;
        }

        public String getPluginVersion() {
            return pluginVersion;
        }

        public void setPluginVersion(String pluginVersion) {
            this.pluginVersion = pluginVersion;
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("10.16.5.32",6030);
        JsonRpcClient client = new JsonRpcClient();
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        try {
            Request request = new Request();
            request.hostname = "10.30.104.68";
            request.ip = "10.30.104.68";
            request.agentVersion = "5.1.0";
            request.pluginVersion  = "plugin not enables";
            Object response = client.invokeAndReadResponse("Agent.ReportStatus", new Object[]{request},Object.class, out, in);
            System.out.println(response);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
