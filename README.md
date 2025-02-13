# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Chess Server Design

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYtQAr5y7Drco7jE8H5LH0ALnMCBQJigpQIAePKwvuh6ouisTYrBBSuiypTklSNKvigJpEuGBSWtyvIGoKwowERjqiNhYZuh6uqZLG-rTiGOHqORHLRjAHHxnKKBJvk-5lChPLZrmmASWJP5XAM5bAaUNZBrOzbflev4FNkPb9oOvTKSOowrNMfTTpp86lEunCrt4fiBF4KDoHuB6+Mwx7pJkmD6ReRTUNe0gAKK7iF9Qhc0LQPqoT7dFZjboO2v7iVBlyJXOcnpalsHwR5vrIQVYBoRimEiUxpEsfhHHWQGGlJWgJFMm6fGlCUSAAGZhH6cgwH6hiZW5KAAB7QgxKiVS1uEcCg3DsUG9VxtozVmqobWzfNg0NXOMCdd4wxGNoegGBNol-jlpQoZ5MkIHmOUKTppQ9NpQW6fk-lgKUhlDnZK6eI5G6Qrau7QjAADio6st5p5+eezCym95QVODEXRfYo4JTtyU-hdQIZdjaDZfjuUifB0LIdCpUYWdU1reTsSQ6MqiwqtZH5JakIWKgNAwMAMDILEMAQJ1fPKnz4BILzGPM7TeOpldVNqLJ8kwU9-Qy2o4wVBro4AJLSOMMAAIy9gAzAALE8J6ZAaFZqVMOgIKADZ26pTyawAcqOtmNK9JRiZ9fYDkOfSa6o2u66MBtG6blvWz5+oqWZDt9E7LtuynHujt7oy+39DnroE2A+FA2DcPAbGGEzhgw75n1soplS1A06OY8EhNDl7o57GQKXy8C5RDWgsxRyguf3H0EHE6matqnBcBV7Ci9epk1NYrT+Q8e6NWLcPbOtRzHIAGqUF1PWpH1A184TMAxDAVDAMgK5YVvzG4Z6eo17C3ejAfLJtUrqvNQMA0AoGSBDUcfNeSf0yKyfmmsYAGzlmlEmpQV56luvdEmj0kY9E1jHcoccLb+0oIHeGwcjJj0ISbc2JCzD2QBkXAIlg5oIQgQAKQgDySBoxAjpxAA2OGOQEaXiRs3Skd4WiayxnWRqQ5y7AFYVAOAEAEJQFmAQ6QpDUoSVKMPUezxnbKNUeolYAB1ROLQABCztBEzDDvrQ2pRiEz2gvkPKXCkJeLQOvcq886bhjwhSDiS0ZyNX-rxI+7Uz7dXgbfe+j9n6b23sEsA38tGRPWtErmUAeYgMQftCAh1og0CSFolJ79SR+C0Jkb+iiTFqOgFktqwAbSsgaR2SAMBEHdIGkgWABhuDgBQLaTIYpTHQBQXonxWC3FkLniUZ6OjyEiMob9Uwy5C5OQCF4JRXYvSwGANgcuhB4iJBSInYR5hG5PUqKFcKkVorGH7qghWwwIC826YgQ5ZwSaVKqh-bgeBWahkBVEy0IBgVQHgeLWp-MimHXhSdMQWEB6XB+XgOZqsPHqxWXpChP1egFyAA
