# Branch

Execute this command:

To run the dockers and have each branch have its own database run the following commands in the respective folders:

In the BranchInventory folder:
Navigate to the folder where the branch inventory project is located.
```
cd BranchInventory
```
Clean up old build files to ensure a clean build.
```
./gradlew clean
```
Compile and generate the build of the project.
```
./gradlew build
```
Create a Docker image labeled “specificbranch:latest” so that each branch has its own database.
```
docker build -t specificbranch:latest .
```

After running in the BranchInventory folder go to the GeneralBranch folder:

```
cd GeneralBranch
```
Clean up the build files in the GeneralBranch folder.
```
./gradlew clean
```
Compile and generate the GeneralBranch project build.
```
./gradlew build
```

After running in the GeneralBranch folder go to the root(Branch) folder:

Clean the root project.
```
./gradlew clean
```

Compile and generate the root project build.
```
./gradlew build
```
Upload and run the containers defined in the docker-compose file.
```
docker-compose up
```
---
If you have a docker created from the branch before then follow these commands:

Stop and remove containers and associated volumes.
```
docker-compose down -v
```
Forcibly remove unused Docker resources.
```
docker system prune -f
```
Force the removal of all unused images and containers.
```
docker system prune -af
```
Cleans the project again to avoid conflicts.
```
./gradlew clean
```
Compiles and generates the updated build of the project.
```
./gradlew build
```
Restarts the containers with the new configuration.
```
docker-compose up
```
---
When running docker, check if it is started with:

Shows the running Docker containers to verify that they are active and on which ports.
```
docker ps
```

You will see something similar to this:

Image:
![docker ps](https://i.ibb.co/jksQCCJ6/Screenshot-from-2025-03-01-01-39-02.png)

Text:
```
CONTAINER ID    IMAGE                   COMMAND                 CREATED              STATUS                 PORTS                                           NAMES
8c1595ab8c29    branch_generalbranch    "java -jar app.jar"     About a minute ago   Up About a minute      0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp     branch_generalbranch_1
93dfeadff6cc    postgres:latest         "docker-entrypoint.s…"  About a minute ago   Up About a minute      0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp     branch_postgres_1
```

To do the tests, enter the swagger link:

to create branches:

```
http://localhost:8080/swagger-ui/index.html#/
```

When adding a branch, the localhost from 8000 to 8079 will be added, so when adding a branch you can enter for example this link:
```
http://localhost:8000/swagger-ui/index.html#/
```

But to be more sure on which port it is running:

```
docker ps
```

And it will show you on which port the branch database was created:

Image:

![docker ps](https://i.ibb.co/YTNZ87mH/Screenshot-from-2025-03-01-01-39-21.png)

Text:
```
CONTAINER ID    IMAGE                   COMMAND                 CREATED              STATUS         PORTS                                           NAMES
2afbdbdb0524    specificbranch:latest   "java -jar app.jar"     5 seconds ago        Up 4 seconds   0.0.0.0:8000->8000/tcp, [::]:8000->8000/tcp     specificbranch_branch_12345670
8c1595ab8c29    branch_generalbranch    "java -jar app.jar"     2 minutes ago        Up 2 minutes   0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp     branch_generalbranch_1
93dfeadff6cc    postgres:latest         "docker-entrypoint.s…"  2 minutes ago        Up 2 minutes   0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp     branch_postgres_1
```
---
## Project Infrastructure
![Project-Infrastructure](https://i.ibb.co/ymwc6vvb/Project-Infrastructure.png)

## Use Case Diagram
![Project-Infrastructure](https://i.ibb.co/6RhPchGj/Use-Case-Diagram.png)

## Sequence Diagram
![Project-Infrastructure](https://i.ibb.co/b50RWdBT/Sequence-Diagram.png)

---
This is the link where the diagrams were made in case you can't see the images well.
https://drive.google.com/file/d/1cdwsFgX6Mthoo2TmVSOsGyl0s5SLRtAh/view?usp=sharing