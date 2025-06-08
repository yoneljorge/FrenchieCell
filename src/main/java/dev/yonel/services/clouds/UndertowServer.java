package dev.yonel.services.clouds;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class UndertowServer {


    private Undertow server;
    private String CALLBACK = "";

    public void startServer() {
        server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
                        if (httpServerExchange.getRequestPath().equals(CALLBACK)) {
                            handleCallBack(httpServerExchange);
                        } else {
                            handleRoot(httpServerExchange);
                        }
                    }
                }).build();
    }

    private void handleCallBack(HttpServerExchange exchange) {
        String query = exchange.getQueryString();
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Query: " + query);
        System.out.println("Query: " + query);
    }

    private void handleRoot(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send("<html><body>Welcome to OAuth2 Authorization Server</body></html>");
    }

    public void stopServer() {
        if (server != null) {
            server.stop();
            System.out.println("Undertow server stopped");
        }
    }

    public void setCallBack(String callBack) {
        this.CALLBACK = callBack;
    }
}
