package dev.yonel.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConectionChecker {

    private static final String DEFAULT_HOST = "8.8.8.8"; //Google DNS
    private static final int DEFAULT_PORT = 53;           //Puerto de DNS
    private static final int TIMEOUT = 2000;              //Tiempo de espera en milisegundos

    /**
     * Verifica si hay conexión a Internet.
     *
     * @return true si hay conexión, false de lo contrario.
     */
    public static boolean hasInternetConnection() {
        return hasInternetConnection(DEFAULT_HOST, DEFAULT_PORT, TIMEOUT);
    }

    /**
     * Verifica si hay conexión a Internet con los parámetros dados.
     *
     * @param host    El host al que se intentará conectar (ejemplo: Google DNS).
     * @param port    El puerto al que se intentará conectar (ejemplo: puerto 53 para DNS).
     * @param timeout El tiempo de espera para la conexión en milisegundos.
     * @return true si hay conexión, false de lo contrario.
     */
    public static boolean hasInternetConnection(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true; //Conexión exitosa.
        } catch (IOException e) {
            e.printStackTrace();
            return false; //No hay conexión.
        }
    }

    /**
     * Verifica si la computadora está conectada a una red, pero no necesariamente a Internet.
     *
     * @return true si hay conexión de red, false de lo contrario.
     */
    public static boolean hasNetworkConnection() {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 80);
            Socket socket = new Socket();
            socket.connect(address, TIMEOUT);
            socket.close();
            return true; //Hay conexión de red
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false; //No se pudo resolver la dirección
        } catch (IOException e) {
            e.printStackTrace();
            return false; //No hay conexión de red.
        }
    }
}
