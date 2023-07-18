# E-commerce Microservice and React Project

This project is an e-commerce platform that uses a combination of seven Spring microservices and a React frontend.

# Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

# Diagram & Video.

![MicroserviceArchitecture](https://user-images.githubusercontent.com/80879578/218806340-6df6aa4b-d3d8-4782-922b-6f413943b332.png)
  
Video
 
https://user-images.githubusercontent.com/80879578/218830144-16307ea1-5ded-4d9b-88d1-11efa65282e4.mp4
 
# Prerequisites

What things you need to install the software and how to install them:

 -   Java 8 or above
 -   Maven
 -   NodeJS
 -   npm
 -   Postgresql
 -   MongoDb
 -   RabbitMq
    
# Installing

A step by step series of examples that tell you how to get a development environment running:

- Clone the project to your local machine using Git:
    https://github.com/Suryansh-Sharma/E-Store-Microservice.git

- Go to project root directory
    cd E-Store-Microservice
- Open Backend Folder in your favourite IDE Intellij/Eclipse.

    Configure PostgresQl database username and password in Product,Inventory,Order,User services.

    Configure MongoDb in ReviewAndQuestion service.
    
    Add Auth0 client-id in all service .
    
    Configure Gmail Client id and credentials in ReviewAndQuestion Service.
- Build project. 
    
    Run Service-Registory first , then run all service one by one. 
    
    You can view Api-Documentation,Example in Backend/E-Store-Microservice Folder ReadMe

- Now Go to Frontend React Folder.
    
    cd Frontend/e-store-react
    
    Use npm install to intall all modules.
    
    Type 'npm start' in terminal to run project.

# Deployment

    To deploy the project on a live system, you can use a cloud service such as Amazon Web Services (AWS) or Azure.

# Built With

-   Spring Boot - The web framework used
-   React - The frontend library used
-   Maven - Dependency Management
-   NodeJS - JavaScript runtime environment

# Authors

Suryansh Sharma - Student - https://github.com/Suryansh-Sharma
    
# Acknowledgments

    Hat tip to anyone whose code was used.
    Sardesh Sharma.
    Amazing tutorials by Programming Techie , Daily code buffer , Java Techie , Learn Code With Durgesh.
    Inspiration.
    etc

