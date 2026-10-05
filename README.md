# Oasys – Camel Up

Oasys is a Java client-server application developed as a university software engineering team project. It implements a game server, a JavaFX observer interface and an automated engine for a digital interpretation of the board game *Camel Up*.

The project focuses on network communication, game-state synchronization and the visualization of a running match. It is published as a portfolio snapshot of the completed academic project.

> **Independent academic project:** This repository is not affiliated with, endorsed by or sponsored by the publisher or rights holders of *Camel Up*. The game name is used only to describe the ruleset implemented by the coursework project.

## Architecture

| Module | Purpose |
| --- | --- |
| `Server` | Hosts games, manages lobbies and players, validates actions and distributes game-state updates. |
| `Observer` | Provides the JavaFX interface for joining a server and following the board, bets, players and game progress. |
| `Engine` | Connects as an automated participant and selects valid actions from the current game state. |

The modules communicate over TCP using JSON packets. Shared packet and data models represent lobbies, players, camels, bets, dice rolls and board spaces.

## Features

- configurable game lobbies and player management
- synchronized board and match-state visualization
- stage and final betting views
- spectator tiles, dice rolls and camel movement
- recent-game and leaderboard data
- automated engine participant
- JSON-based client-server protocol
- unit and integration tests for packet conversion, timers and observer event handling

## Technology

- Java 21
- JavaFX 21 and FXML
- Maven
- Gson and Jackson
- TCP sockets
- JUnit 5, Mockito and TestFX

## Build

### Requirements

- JDK 21
- Maven 3.9 or newer

Build all three modules from the repository root:

```bash
mvn clean package
```

The JavaFX integration tests require a graphical display. On a headless Linux system, run the build through Xvfb:

```bash
xvfb-run -a mvn clean verify
```

## Run

Start the server and its administration interface:

```bash
java -jar Server/target/Server-1.0-SNAPSHOT.jar
```

Start the observer in another terminal:

```bash
java -jar Observer/target/Observer-1.0-SNAPSHOT.jar
```

The default server address is `localhost:62263`. An automated engine participant can be started with:

```bash
java -jar Engine/target/Engine-1.0.jar --ip localhost --port 62263 --engineName Engine
```

The server creates `lobbies.json` and `playerListSave.json` as local runtime files. They are intentionally excluded from version control.

## Marie Müller's Contribution

This application was developed collaboratively by a university project team. Marie Müller worked primarily on:

- the JavaFX observer interface and visual redesign of its join, game-list and board views
- integration of board, camel, dice and interface graphics
- parts of the server configuration and player-management interface
- observer and server tests, including packet conversion and messaging scenarios

The repository contains the integrated team result because these components depend on the shared server, protocol and game logic.

## Repository Scope

This public portfolio version contains the application source code and required resources. Internal meeting notes, time sheets, university documents, IDE metadata, generated build artifacts and the original repository history are intentionally excluded.

## Rights and Usage

No open-source license is granted for the project-specific source code or visual assets. The repository is provided for portfolio and demonstration purposes. Third-party libraries remain subject to their respective licenses.
