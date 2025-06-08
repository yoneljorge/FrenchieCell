package dev.yonel.services;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class CheckDateTime {

    public static boolean isDateTimeOK() {
        //Hacer la solicitud a WorldTimeAPI para obtener la hora actual
        String apiUrl = "http://worldtimeapi.org/api/ip"; //Basado en la IP local de la máquina
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Leer la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            //Cerrar las conexiones
            in.close();
            connection.disconnect();

            //Parsear la respuesta JSON
            JSONObject jsonResponse = new JSONObject(content.toString());
            String dateTimeStr = jsonResponse.getString("utc_datetime");

            //Convertir la hora obtenida en UTC a LocalDateTiem
            LocalDateTime onlineDateTime = LocalDateTime.parse(dateTimeStr.substring(0, 19)); //Recortar la parte de los milisegundos

            //Obtener la fecha y hora del sistema local
            LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);

            //Comparar la diferencia en segundos entre ambas horas

            long differenceInSeconds = ChronoUnit.SECONDS.between(localDateTime, onlineDateTime);

            // Mostrar la información
            System.out.println("Hora del sistema local (UTC): " + localDateTime);
            System.out.println("Hora del servidor en línea (UTC): " + onlineDateTime);
            System.out.println("Diferencia en segundos: " + differenceInSeconds);

            if (Math.abs(differenceInSeconds) <= 5) {
                System.out.println("La fecha y hora del sistema son correctas.");
                return true;
            } else {
                System.out.println("La fecha y hora del sistema no coinciden con la hora en línea.");
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
