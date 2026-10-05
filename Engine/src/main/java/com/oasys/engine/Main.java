package com.oasys.engine;

import com.oasys.engine.communication.Communication;
import org.apache.commons.cli.*;

public class Main {
    public static int port = 62263;
    public static String host = "localhost";
    public static String playerName = "Engine";
    public static int lobbyId = -1;

    public static void main(String[] args) {
        Main.parseArguments(args);



        Communication.getInstance().setPlayerName(playerName);

        try {
            Communication.getInstance().connect(port, host);
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private static void parseArguments(String[] args) {
        Options options = new Options();

        // Define options
        options.addOption("i", "ip", true, "The server's IP (Default: localhost)");
        options.addOption("p", "port", true, "The server's port (Default: 62263)");
        options.addOption("n", "engineName", true, "The engine participant's name (Default: Engine)");
        options.addOption("l", "lobbyId", true, "The ID of the lobby to join immediately (optional)");
        options.addOption("h", "help", false, "Display help");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("java -jar Engine.jar", options);
                System.exit(0);
            }

            String host = cmd.getOptionValue("i");
            String port = cmd.getOptionValue("p");
            String playerName = cmd.getOptionValue("n");
            String lobbyId = cmd.getOptionValue("l");

            // set attributes
            if (host != null) {Main.host = host;}
            if (playerName != null) {Main.playerName = playerName;}
            if (port != null) {
                try {
                    Main.port = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    throw new ParseException("The port is not a number!");
                }
            }
            if (lobbyId != null) {
                try {
                    Main.lobbyId = Integer.parseInt(lobbyId);
                } catch (NumberFormatException e) {
                    throw new ParseException("The lobbyId is not a number!");
                }
            }

        } catch (ParseException e) {
            System.out.println("Invalid arguments: " + e.getMessage());
            formatter.printHelp("java -jar Engine.jar", options);
            System.exit(1);
        }
    }
}