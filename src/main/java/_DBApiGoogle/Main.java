package _DBApiGoogle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner busqueda = new Scanner(System.in);
        System.out.println("Escribe lo que deseas buscar:");
        String query = busqueda.next();
        try {
            String googleScholarData = GoogleScholarAPI.searchGoogleScholar(query);

            // Analiza los datos de Google Scholar y obtén títulos y autores.
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(googleScholarData);

            // Verifica si la respuesta contiene resultados en "organic_results".
            if (jsonObject.containsKey("organic_results")) {
                JSONArray resultsArray = (JSONArray) jsonObject.get("organic_results");

                // Recorre los resultados y extrae títulos y autores.
                for (Object resultObject : resultsArray) {
                    JSONObject result = (JSONObject) resultObject;
                    String title = (String) result.get("title");
                    String author = (String) result.get("snippet"); // Puedes ajustar este campo según corresponda.

                    // Luego, almacena los datos en la base de datos.
                    try (Connection connection = DatabaseConnection.getConnection()) {
                        String insertQuery = "INSERT INTO resultados_google_scholar (titulo, autor) VALUES (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, title);
                        preparedStatement.setString(2, author);
                        preparedStatement.executeUpdate();
                        
                        System.out.println("Título: " + title);
                        System.out.println("Autor: " + author);
                        System.out.println("=====================================================");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No se encontraron resultados en Google Scholar.");
                System.out.println("Respuesta completa de Google Scholar: " + jsonObject.toString());
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        busqueda.close();
    }
};