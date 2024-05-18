<div align="center">
    <img src="https://readme-typing-svg.demolab.com?font=Poetsen+One&size=25&duration=5000&pause=1000&color=F5EBF0FF&background=00000000&center=true&vCenter=true&random=false&width=438&height=51&lines=THE+PRICE+IS+RIGHT" alt="Typing SVG">
</div> 

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs24-group-18_server&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=sopra-fs24-group-18_server)
[![Test Coverage](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs24-group-18_server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sopra-fs24-group-18_server)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs24-group-18_server&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sopra-fs24-group-18_server)

## Introduction 🗒️
Welcome to our Price Guessing Game💰, an engaging and interactive experience where players put their pricing prowess to the test! In this game, participants need to guess the price of various items based on the displayed pictures within a set time limit, whoever gets the closest guess wins. Whether you're a solo player or enjoying the game with friends, our two primary play modes - Guessing and Budget - offer diverse challenges for all.

Our game also contains exciting tools designed to enhance the entertainment value and competitiveness. Drawing inspiration from real item images and prices sourced from eBay, our aim is not only to provide entertainment but also to offer players valuable insights into market trends 📈.

Join us in this thrilling adventure where fun meets learning! 🎉

## Contents 📂
- [Technologies](#technologies)
- [High-level Components](#high-levelcomponents)
- [Launch & Deployment](#launchanddeployment)
- [Roadmap](#roadmap)
- [Authors and Acknowledgment](#authorsandacknowledgment)
- [License](#license)

## Technologies 🔗
<a name="technologies"></a>
- [Springboot](https://spring.io/) - A powerful Java framework utilized to develop microservices.
- [RESTful Web Service](https://spring.io/guides/gs/rest-service) - Principles for seamless communication between client and server components.
- [Gradle](https://gradle.org/) - A fast, dependable, and adaptable open-source build automation tool with an elegant and extensible declarative build language.
- [H2 Database](https://www.h2database.com/html/main.html) - A lightweight yet robust database solution for efficient data management.
- [Ebay Developers Program](https://developer.ebay.com/) - The external API which enriches our application with real-time market data.
- [Github Projects](https://docs.github.com/en/issues/planning-and-tracking-with-projects/learning-about-projects/about-projects) - Project management, facilitating task tracking and team coordination.
- [Google Cloud](https://cloud.google.com/) - Deployment platform.
- [SonarCloud](https://sonarcloud.io/) - Ensuring the quality of our code and the coverage of tests.

## High-level components 📌
<a name="high-levelcomponents"></a>
### 1. User

The User component represents the core participants of our game – the players. Beyond standard functionalities like login and registration, users can initiate game sessions, prepare for gameplay, submit answers, purchase tools, and earn points throughout the game. This component encapsulates all user-related interactions within the system.

[User](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/User.java)
[User Service](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/UserService.java)

### 2. Room

The Room component serves as the container for all in-game activities, defining the fundamental parameters of gameplay such as game mode, player count, and point allocation. Within a room, a specific game mode is selected, and multiple rounds of gameplay can unfold, fostering a dynamic and immersive gaming environment.

[Room](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Room.java)
[Room Service](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/RoomService.java)

### 3. Question

Questions form the backbone of gameplay, encapsulating vital information such as item images, price ranges, and tool effects tailored to individual players and game rooms. Each question represents a pivotal moment in the game, providing players with essential data to make informed guesses. A game comprises multiple questions, each containing one or several items for assessment.

[Question](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Question.java)
[Question Service](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/QuestionService.java)

### 4. Answer

The Answer component manages the receipt and processing of players' guessed prices, incorporating logic to calculate earned scores based on various game modes and room conditions. Players can submit only one answer per question, and once all answers within a round are collected, the accumulated scores are disseminated for ranking within the room.

[Answer](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Answer.java)
[Answer Service](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/AnswerService.java)
 ### 5. EbayService

The EbayService component facilitates interaction with the eBay API to retrieve item information, necessitating configurations such as credentials, search scopes, and request types. This service acts as the bridge between the game and external data sources, enriching the gaming experience with real-world market insights and item details.

[EbayService](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/EbayAPIService.java)

## Launch & Deployment :technologist:
<a name="launchanddeployment"></a>
### 1. Gradle Setup

To build and develop the application locally, you'll need to set up Gradle. You can use the Gradle Wrapper provided in the project:
-   macOS &  Linux: : `./gradlew`
-   Windows: `./gradlew.bat`

For more detailed information, refer to the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### 2. Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

### 3. API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

### 4. Test
You can run the tests using the following command:

```bash
./gradlew test
```

### 5. Ebay API
The application utilizes the eBay API. 

For more details, refer to the [eBay API documentation](https://developer.ebay.com/api-docs/buy/browse/resources/item_summary/methods/search#s0-1-22-6-7-7-6-SearchforItemsbyCategory-1)

You may need to apply for authorization to use the eBay API, which you can do [here](https://developer.ebay.com/)


### 6. Database
The application uses the H2 database. Configuration details can be found [here](https://github.com/sopra-fs24-group-18/sopra-fs24-group-18-server/blob/main/src/main/resources/application.properties)


### 7. Deployment on Google Cloud
The application is hosted on [The Price Is Right](https://sopra-fs24-group-18-client.oa.r.appspot.com/)

The Google Cloud Projects are as following:

[Server Project](https://console.cloud.google.com/appengine/services?serviceId=default&project=sopra-fs24-group-18-server)
[Client Project](https://console.cloud.google.com/appengine/services?serviceId=default&project=sopra-fs24-group-18-client)

To join the projects, please contact lu.li@uzh.ch.

## Roadmap :bulb:
<a name="roadmap"></a>
### 1. Expand User Tools :placard:
Our current set of user tools is limited, but there's significant potential for enhancement. For instance, tools to extend the guessing time, or tools allow collaboration among players.

### 2. More Game Modes :roller_coaster:
While our project currently offers two game modes, there's room for expansion to enrich the gaming experience. For example, modes that allow players to choose specific item categories for guessing, or modes that contain more competitive challenges.

### 3. Enhance Features :star2:
Although our project primarily focuses on the core gameplay experience, there's potential to incorporate additional features to further elevate user engagement. Such as background music (BGM) to enhance the atmosphere, customizable game room backgrounds to personalize the experience, and built-in communication tools to facilitate interaction among players during gameplay sessions.


## Authors and acknowledgment 💪
<a name="authorsandacknowledgment"></a>
### Contributors

- **Bingjie Xue** - [Github](https://github.com/binxxxxx)
- **Yanyang Gong** - [Github](https://github.com/YanYang-G0001)
- **Yunfan Zhou** - [Github](https://github.com/saluttolove)
- **Lu Li** - [Github](https://github.com/luliuzh)
- **Zien Zeng** - [Github](https://github.com/Unusz)

### Supervision

- **Nils Grob** - [Github](https://github.com/homo-iocus)

## License
<a name="license"></a>
[MIT License](LICENSE)

❗
The developers should also pay attention to the [API License Agreement of Ebay](https://developer.ebay.com/join/api-license-agreement) .