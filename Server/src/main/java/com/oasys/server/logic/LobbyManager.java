package com.oasys.server.logic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.oasys.server.data.Lobby;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LobbyManager {
    private static final Gson gson = new Gson();

    public static ArrayList<Lobby> loadLobbies() {
        ArrayList<Lobby> lobbies = new ArrayList<>();

        try (FileReader reader = new FileReader("lobbies.json")) {
            // Parse the JSON array from the file
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            // Loop through the JsonArray and convert each element into a Lobby object
            for (JsonElement element : jsonArray) {
                Lobby lobby = gson.fromJson(element, Lobby.class);
                lobbies.add(lobby);
            }
        } catch (IOException e) {
            System.err.println("Error reading lobbies file: " + e.getMessage());
        }

        return lobbies;
    }

    public static void clearLobbiesFile() {
        try (FileWriter writer = new FileWriter("lobbies.json")) {
            // Write an empty array to clear the file
            writer.write("[]");
        } catch (IOException e) {
            System.err.println("Error clearing lobbies file: " + e.getMessage());
        }
    }

    public static Lobby findLobbyById(int lobbyId) {
        return loadLobbies().stream()
                .filter(lobby -> lobby.getLobbyId() == lobbyId)
                .findFirst()
                .orElse(null);
    }

    public static void updateLobby(Lobby lobby) {
        ArrayList<Lobby> lobbies = loadLobbies();

        for (int i = 0; i < lobbies.size(); i++) {
            if (lobby.getLobbyId() == lobbies.get(i).getLobbyId()) {
                lobbies.set(i, lobby);

                try (FileWriter writer = new FileWriter("lobbies.json")) {
                    gson.toJson(lobbies, writer);
                    System.out.println("Lobbies successfully written to file!");
                } catch (IOException e) {
                    System.err.println("Error writing lobbies to file: " + e.getMessage());
                }
                System.out.println("Lobbies after updating: " + LobbyManager.loadLobbies());

                break;
            }
        }


        //System.out.println("Lobby " + lobby.getLobbyId() + " wurde aktualisiert.");
    }
}
