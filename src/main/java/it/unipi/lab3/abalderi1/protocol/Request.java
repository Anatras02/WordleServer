package it.unipi.lab3.abalderi1.protocol;

import java.util.HashMap;

public class Request {
    String endpoint;
    HashMap<String, String> params = new HashMap<>();

    private void setEndpoint(String[] requestParts) {
        this.endpoint = requestParts[0];
    }

    private void setParams(String[] requestParts) {
        String[] paramsKeyValue = requestParts[1].split(",");

        for (String paramKeyValue : paramsKeyValue) {
            String[] param = paramKeyValue.split("=");

            if (param.length != 2) {
                throw new IllegalArgumentException("Invalid param: " + paramKeyValue);
            }

            params.put(param[0], param[1]);
        }
    }

    public Request(String request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String[] requestParts = request.split("\\?");

        setEndpoint(requestParts);

        if(requestParts.length > 1) {
            setParams(requestParts);
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public HashMap<String, String> getParams() {
        return new HashMap<>(params);
    }
}
