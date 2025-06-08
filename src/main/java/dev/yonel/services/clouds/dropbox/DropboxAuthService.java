package dev.yonel.services.clouds.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

import dev.yonel.services.ConectionChecker;
import io.undertow.Undertow;
import io.undertow.util.Headers;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class DropboxAuthService {
    private static final String APP_KEY = "a0hirkui2s0vs2f";
    private static final String APP_SECRET = "zd9u6qjjr9fd0pt";
    private static final String REDIRECT_URI = "http://localhost:8080/dropbox-auth-finish";
    SessionStore sessionStore;

    private String authorizationCode = null;
    private CountDownLatch latch = null;
    private int TIEMPO_ESPERA = 120; // Si en 2 minutos no ha inciado sesión pues que se despida
    private boolean TIEMPO_AGOTADO = false;

    /**
     * Variable para almacenar la instancia de esta clase.
     */
    private static DropboxAuthService instance;

    private DropboxAuthService() {
        sessionStore = new SessionStore();
    }

    private static synchronized void createInstance() {
        instance = new DropboxAuthService();
    }

    public static DropboxAuthService getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public String getAccessToken() {
        String accessToken = getSavedAccessToken();

        if (ConectionChecker.hasInternetConnection()) {
            if (!isTokenValid()) {
                // Limpiamos el archivo de tokens
                sessionStore.clear();

                accessToken = authorizeDropbox();
                if (accessToken != null) {
                    saveAccessToken(accessToken);

                    // Como ya hemos actualizado esta información no hay necesidad de volver a
                    // hacerlo por lo que la igualamos desde aqui.
                    isTokenValidVerified = true;
                    tokenValid = true;
                }
            }
        }

        return accessToken;
    }

    private String getSavedAccessToken() {
        return sessionStore.getAccessToken();
    }

    private void saveAccessToken(String accessToken) {
        sessionStore.saveAccessToken(accessToken);
    }

    /**
     * Método con el que obteners el token de acceso para la cuenta de dropbox
     * 
     * @param tiempoAgotado método que se va a ejecutar en caso de que el tiempo se
     *                      agote
     * 
     * @return el token de acceso
     * @throws Exception
     */
    private String authorizeDropbox() {
        // En caso de que se halla establecido la bandera tiempoAgotado en true se
        // restablece
        TIEMPO_AGOTADO = false;

        latch = new CountDownLatch(1);

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/FrenchieCell")
                .withUserLocale(Locale.getDefault().toString())
                .build();

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);

        // Crear un servidor Undertow para escuchar el redireccionamiento
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(exchange -> {

                    String query = exchange.getQueryString();
                    System.out.println("Solicitud recibida con query: " + query);

                    if (query != null && query.contains("code=")) {
                        String[] params = query.split("&");
                        for (String param : params) {
                            if (param.startsWith("code=")) {
                                authorizationCode = param.split("=")[1];
                                break;
                            }
                        }
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Autorización completada. Puede cerrar esta ventana.");
                        exchange.endExchange();
                        System.out.println("Código de autorización recibido: " + authorizationCode);

                        // Liberar el latch para continuar el flujo.
                        latch.countDown();
                    } else {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Error: No se encontró el código de autorización.");
                        exchange.endExchange();
                        System.out.println("No se recibió código de autorización.");

                        // Liberar el latch para continuar el flujo.
                        latch.countDown();
                    }

                }).build();

        server.start();
        System.out.println("Servidor Undertow iniciado en http://localhost:8080");

        // Crear la URL de autorización con redireccionamiento
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI, new SessionStore())
                .build();

        String authorizeUrl = webAuth.authorize(authRequest);

        // Abrir el navegador automáticamente
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(URI.create(authorizeUrl));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor abre la siguiente URL en tu navegador: ");
            System.out.println(authorizeUrl);
        }

        // Esperar hasta que el código de autorización sea recibido
        try {
            TIEMPO_AGOTADO = latch.await(TIEMPO_ESPERA, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!TIEMPO_AGOTADO) {
            System.out.println("Tiempo de espera agotado. No se recibió el codigo de autorización.");
            TIEMPO_AGOTADO = true;
        }

        server.stop(); // Detener el servidor después de recibir el código
        System.out.println("Servidor Undertow detenido.");

        String token = getAccessTokenFromCode(webAuth, REDIRECT_URI);
        System.out.println(token);

        return token;
    }

    // Finalizar el proceso de autorización y obtener el token de acceso
    private String getAccessTokenFromCode(DbxWebAuth webAuth, String redirectUri) {
        DbxAuthFinish authFinish;
        try {
            authFinish = webAuth.finishFromCode(authorizationCode, redirectUri);
            return authFinish.getAccessToken();
        } catch (DbxException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Variable booleana en la que vamos a almacenar si ya se ha verificado que el
     * token sea válido para no volver a
     * verificarlo.
     */
    private boolean isTokenValidVerified = false;
    /**
     * Variable en la que vamos a almacenar la validez del token.
     */
    private boolean tokenValid = false;

    public boolean isTokenValid() {

        if (!isTokenValidVerified) {
            final String ACCESS_TOKEN = sessionStore.getAccessToken();

            if (ACCESS_TOKEN != null) {
                DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/FrenchieCell").build();
                DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

                if (!ACCESS_TOKEN.isEmpty()) {
                    try {
                        // Intentamos obtener la información de la cuenta asociada al token
                        FullAccount account = client.users().getCurrentAccount();
                        System.out.println(
                                "El token es válido. Información de la cuenta: " + account.getName().getDisplayName());
                        tokenValid = true;
                        isTokenValidVerified = true;
                    } catch (DbxException e) {
                        e.printStackTrace();
                        System.out.println("Error: el token no es válido o ha caducado");
                        tokenValid = false;
                        isTokenValidVerified = true;
                    }
                }
            } else {
                tokenValid = false;
            }
        }
        return tokenValid;
    }

    public boolean isTiempoAgotado() {
        return TIEMPO_AGOTADO;
    }
}