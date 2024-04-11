package com.example.crescendo;
import android.os.Handler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class LLM {
    static BufferedReader br;
    public static String chatGPT(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "REPLACE WITH REAL KEY WHEN READY";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            String question = "Using the following artists, give me THREE UNIQUE artists similar to the artists provided. Do not provide ANY text but the three artsists. Give only one sentence with 3 artists TOTAL. Respond in the same FORMAT AS FOLLOWS. Include 'and' before the third artists: " + prompt;
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT

            try {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
    /*

    EXAMPLE CODE FOR HOW TO USE API

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println(LLM.chatGPT("Drake, Kendrick Lamar, Kanye"));

        }
    });
    thread.start();

     */
}