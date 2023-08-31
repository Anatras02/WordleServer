package it.unipi.lab3.abalderi1.protocol;

import java.util.HashMap;

/**
 * La classe {@code Request} rappresenta una richiesta nel protocollo.
 * Fornisce funzionalità per gestire l'endpoint e i parametri associati.
 */
public class Request {
    String endpoint;
    HashMap<String, String> params = new HashMap<>();

    /**
     * Imposta l'endpoint della richiesta.
     *
     * @param requestParts Parti della richiesta divise.
     */
    private void setEndpoint(String[] requestParts) {
        this.endpoint = requestParts[0];
    }

    /**
     * Imposta i parametri associati alla richiesta.
     *
     * @param requestParts Parti della richiesta divise.
     */
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

    /**
     * Costruttore per inizializzare la richiesta.
     *
     * @param request La stringa della richiesta.
     */
    public Request(String request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String[] requestParts = request.split("\\?");

        setEndpoint(requestParts);

        if (requestParts.length > 1) {
            setParams(requestParts);
        }
    }

    /**
     * Restituisce l'endpoint della richiesta.
     *
     * @return La stringa dell'endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Restituisce il valore di un parametro specifico tramite chiave.
     *
     * @param key La chiave del parametro.
     * @return Il valore del parametro.
     */
    public String getParam(String key) {
        return params.get(key);
    }

    /**
     * Restituisce tutti i parametri associati alla richiesta.
     *
     * @return Una mappa HashMap dei parametri.
     */
    public HashMap<String, String> getParams() {
        return new HashMap<>(params);
    }
}
