package com.example.crescendo;
import android.os.Handler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LLM {
    static String apiKey = "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";
    static boolean keyIn = true;
    static BufferedReader br;
    public static String chatGPT(String prompt) {
        if (!keyIn) {
            return "API KEY NOT IN";
        }
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            String question = "Using the following artists, write a paragraph recommending 3 new artists and explanations for each choice. ANSWER IN LESS THAN 60 WORDS: " + prompt;
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

    public static String chatGPTTaste(ArrayList<Artist> artists) {
        if (!keyIn) {
            return "API KEY NOT IN";
        }
        StringBuilder artistNames = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            artistNames.append(artists.get(i).getArtistName()).append(", ");
        }

        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            String question = "Given the following artists, describe my taste in music. Answer starting with 'Your taste in music is'. DO NOT STRAY FROM THIS FORMAT. After the taste, tell me my 'musical spirit animal'. ANSWER IN LESS THAN 60 WORDS. ANSWER IN ONE PARAGRAPH:" + artistNames;
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
            String similarArtists = (LLM.chatGPT("Drake, Kendrick Lamar, Kanye"));
        }
    });
    thread.start();

     */
}