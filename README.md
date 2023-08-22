# E-commerce Microservice Project

I successfully developed a comprehensive e-commerce microservice using **Spring Cloud** ,demonstrating my proficiency in modern software architecture. The project comprises multiple specialized services, each tailored to specific functionalities. The **product service** effectively manages product data using **PostgreSQL**, while the **review rating service** stores ratings in **MongoDB**. The **inventory service** manages inventory ,product stock ,order quantity etc. For efficient search capabilities, the **search service** employs **Elasticsearch**. The **user service** handles user data, integrating with **PostgreSQL** for seamless user management. Furthermore, the **order service** efficiently orchestrates orders using **PostgreSQL**. Leveraging additional technologies like **Kafka** and **Redis** further enhances the project's performance and scalability. This project showcases my expertise in microservice design, database management, and the implementation of cutting-edge technologies for optimized e-commerce operations.

# Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

# Diagram & Video.

![E-Store-2](https://github.com/Suryansh-Sharma/E-Store-Microservice/assets/80879578/59531f8f-14ee-4973-aff7-23e7afb95e71)
  
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
 -   Elasticsearch
 -   Apache Kafka
 -   Redis Cloud
    
# Installing

A step by step series of examples that tell you how to get a development environment running:

- Clone the project to your local machine using Git:
    https://github.com/Suryansh-Sharma/E-Store-Microservice.git

- Go to project root directory
    cd E-Store-Microservice

## Project Setup Guide

Follow these steps to set up the project:

1. **Basic Project Setup:**
    - Configure the `application.yml` file for each service to include the necessary configurations.
    
2. **Setting Up Apache Kafka:**
    - Configure Apache Kafka in Inventory, Product, Rating-Review, User, and Elasticsearch-Engine services.
    
3. **Configuring Databases:**
    - Set up PostgreSQL databases in Product, Order, and User services.
    - Configure MongoDB in Review Service and Inventory Service.
    
4. **Setting Up Elasticsearch:**
    - Configure Elasticsearch database in Spring-Elastic-Search-Engine.
    - Choose between local Elasticsearch or Bonsai cloud service.
    
5. **Caching Mechanism:**
    - Configure Redis for caching in Spring-Elastic-Search-Engine.
    
6. **Auth0 Integration:**
    - Add the Auth0 issuer URI and audience in each service for authentication.
    
7. **Gmail Client Configuration:**
    - Configure Gmail Client ID and credentials in the Rating-Review Service.
    
8. **Start Services:**
    - Begin with Service-Registry and Api-Gateway.
    
9. **Finalize Setup:**
    - Start all remaining services and wait for approximately 30 seconds for initialization.

10. **Api Documentation:**
    - All services support's swagger.
    - Postman documentaion link :- https://documenter.getpostman.com/view/19485910/2s8Z6zzBjs

Follow these steps to interact with the project's APIs using Postman:

1. **Get JWT Token from Auth0:**
    - Use request **Get-Jwt-From-Auth0**

2. **Add new user:**
    - Use **Add-New-User-In-Db** endpoint in **user service**.
    - Now use **Update-User-Profile** endpoint in **user service** to update user profile.

3. **Registor as new seller:**
    - Use **Add-New-Seller** endpoint in **inventory service**.

4. **Add new brand:**
    - Use **Add-New-Brand** endpoint in **product service**.

5. **Add new product in project:**
    - Use **Save-New-Product** endpoint in **product service**.
    - Note this endpoint automatically send's data to **inventory service** ,**elastic-search-engine service** ,**review service** through **Apache Kafka**. 

6. **That's all:**
    - All basic steps are done ,now you can use other endpoints very eaisily.
    - Enjoy project.

# Deployment

    To deploy the project on a live system, you can use a cloud service such as Amazon Web Services (AWS) or Azure.

# Built With

-   Spring Boot - The web framework used
-   React - The frontend library used
-   Maven - Dependency Management
-   NodeJS - JavaScript runtime environment

# Authors

Suryansh Sharma - Student - https://github.com/Suryansh-Sharma
    



