# Interview Developer Task 

## Supply Chain Tree Management API

A backend service for managing hierarchical supply chain relationships using a tree data structure. This API allows users to build, modify, and query supply chain networks where companies are represented as nodes and their business relationships as edges.
## Technology Stack

- Spring Boot - Framework
- Kotlin - Programming language
- PostgreSQL - Database
- JOOQ - Database interaction/ORM
- JSON - Data format for API requests/responses

# Core Features
## Data Management

- Tree Structure: Manages supply chain relationships as a tree (simplified from graph)
- Edge-based Storage: Stores relationships using from_id → to_id connections
- PostgreSQL Backend: Persistent storage with optimized queries

## API Endpoints

- Create Edge - Add new company relationships
- Delete Edge - Remove existing relationships
- Get Tree - Retrieve complete tree structure starting from any node

## Key Capabilities

- Performance Optimized: Handles large supply chain networks efficiently
- Memory Safe: Prevents overflow when processing extensive tree structures
- Error Handling: Graceful handling of duplicates, invalid inputs, and database errors
- Flexible Querying: Generate tree view from any node as root

# How to Run
## ✅ Requirements (Docker way)

To run this application locally with Docker, make sure you have:

- [Docker](https://www.docker.com/products/docker-desktop) installed and running  
- `docker compose` command available (Docker CLI v20.10+)
- java17-jdk installed and added to JAVA_HOME in environment variables

> 💡 You **do not** need to install Gradle, or PostgreSQL manually.
## 🐳 Run the Application with Docker

To build and start the backend service and PostgreSQL database:

### 1. Checkout the code in Folder

 #### ✅ Option 1: Clone using Git (Command Line)

Make sure Git is installed

- Download from: https://git-scm.com/downloads
- After installing, check that it's working:

```bash
git --version
```
   Clone the repository and switch to the project folder

   ```bash
cd path/to/your/projects

git clone https://github.com/pilulica/InterviewDeveloperTask-IvanBogdanovic.git
cd InterviewDeveloperTask-IvanBogdanovic
```

#### ✅ Option 2: Clone using GitHub Desktop (GUI)

- Download GitHub Desktop: https://desktop.github.com/
- Open the app → File > Clone Repository

Paste this URL:
> https://github.com/pilulica/InterviewDeveloperTask-IvanBogdanovic.git

Choose a local path where you want the project saved and click Clone
   
#### ✅ Option 3: Clone using GitHub CLI (gh)
If you have GitHub CLI installed, run:
```bash
gh repo clone pilulica/InterviewDeveloperTask-IvanBogdanovic
cd InterviewDeveloperTask-IvanBogdanovic
```
   
### 2. **Open a terminal in the root folder of the project**  
   _(where `docker-compose.yml` is located)_

### 3. Run the following commands:

```bash
docker compose up -d db

./gradlew clean build

docker compose up -d app
```

##  ✅ Requirements (without Docker)

To run this application locally, make sure you have:

- IntelliJ (optional) 
- java17-jdk installed and added to JAVA_HOME in environment variables
- PostgreSQL (my version 17.5)
## Run the Application Manually

### 1. Checkout the code in Folder(same way as described above)

### 2. Create Database

- Create PostgreSQL db prewave_edge_tree_db and execute script
- Go to folder(checkout code)

```bash
// CREATE DB
createdb -U postgres prewave_edge_tree_db
```

```bash
 // INSERT SCHEME, TABLE AND RECORDS
psql -U postgres -d prewave_edge_tree_db -f docker/db/init/init.sql
```
### 3. Open terminal 
```bash
RUN
./gradlew clean build
```

### 4. Run app from IntelliJ or from terminal with command 

```bash
./gradlew bootRun
```

## 📘 API Documentation

This project includes auto-generated OpenAPI documentation via Swagger UI.

- 📄 Base URL: `http://localhost:8080`
- 🌐 Swagger UI: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- 🧪 You can test all endpoints directly in your browser.

