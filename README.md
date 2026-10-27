# ADVAITHAM_HOTEL_MANAGEMENT
# Hotel Management System

A portfolio-ready hotel operations web application built with **Java 17, Spring Boot, Maven, Thymeleaf, Spring Data JPA, and MySQL**. It includes room management, guest registration, booking creation/cancellation, relational data, Docker, Jenkins CI/CD, and an AWS EC2 deployment path.

> This is a learning/portfolio project, not a production-hardened hotel booking platform. Before production use, add authentication/authorization, CSRF/security review, migrations (Flyway/Liquibase), audit logging, backups, and concurrency-safe booking constraints.

## Features

- Dashboard showing rooms, guests, and booking counts.
- Add and list rooms.
- Register and list guests with unique email addresses.
- Create bookings with guest, room, check-in/check-out, nightly rate, and total price.
- Prevent check-out dates before check-in, past check-in dates, and overlapping active bookings.
- Cancel confirmed bookings.
- MySQL persistence with foreign keys and indexes.
- Environment-based database configuration.
- Multi-stage Docker image and local Docker Compose stack.
- Jenkins pipeline to test/package, build/push a Docker image, and deploy to EC2 over SSH.

## Technology stack

- Java 17
- Spring Boot 3.4.5
- Spring MVC and Thymeleaf
- Spring Data JPA / Hibernate
- MySQL 8.x
- Maven
- Docker / Docker Compose
- Jenkins
- AWS EC2 (Ubuntu)

## Project structure

```text
hotel-management-system/
├── src/main/java/com/example/hotelmanagement/
│   ├── config/                 # Sample starter data
│   ├── controller/             # Dashboard, room, guest, booking web controllers
│   ├── entity/                  # Room, Guest, Booking entities
│   ├── repository/              # Spring Data repositories
│   └── service/                 # Booking validation/business logic
├── src/main/resources/
│   ├── application.properties  # Environment-based configuration
│   └── templates/               # Thymeleaf pages
├── src/test/                    # Unit test
├── sql/schema.sql               # MySQL schema reference
├── deploy/ec2-setup-ubuntu.sh   # Docker installation on EC2
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── .env.example
└── pom.xml
```

## Option A — Run locally with Maven and an installed MySQL server

Prerequisites: JDK 17, Maven 3.9+, and MySQL 8.x.

1. Create a database and application user in MySQL. Use a strong local password and grant the user access only to this database.

```sql
CREATE DATABASE hotel_management_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'hotel_app'@'localhost' IDENTIFIED BY 'replace_with_a_strong_password';
GRANT ALL PRIVILEGES ON hotel_management_db.* TO 'hotel_app'@'localhost';
FLUSH PRIVILEGES;
```

2. From the project root, set environment variables in Git Bash (replace the example password):

```bash
export DB_HOST=localhost
export DB_PORT=3306
export MYSQL_DATABASE=hotel_management_db
export DB_USERNAME=hotel_app
export DB_PASSWORD='replace_with_a_strong_password'
export SERVER_PORT=8080
```

3. Build, test, and run:

```bash
mvn clean test package
mvn spring-boot:run
```

4. Open <http://localhost:8080>.

The application creates/updates tables with Hibernate (`spring.jpa.hibernate.ddl-auto=update`) and adds a few demo rooms and a demo guest only if those tables are empty. Do not use automatic schema updates as a production migration strategy.

## Option B — Run with Docker Compose (recommended for a first local test)

Prerequisites: Docker Desktop with Docker Compose v2.

1. Copy the environment template:

```bash
cp .env.example .env
```

2. Edit `.env` and replace both sample passwords with strong values. Do not commit `.env` to Git.
3. Build and start MySQL and the application:

```bash
docker compose up --build -d
```

4. Check status and logs:

```bash
docker compose ps
docker compose logs -f app
```

5. Open <http://localhost:8080>.
6. Stop the services:

```bash
docker compose down
```

To also remove the local database volume (this permanently deletes the Compose database data):

```bash
docker compose down -v
```

## Database schema

`sql/schema.sql` documents the `rooms`, `guests`, and `bookings` tables and their constraints. It is provided as a schema reference. If you manually run it, ensure your configured MySQL user has permissions on `hotel_management_db`. The application itself uses Hibernate schema update for this demo.

### Relationships

- One guest can have many bookings.
- One room can have many bookings over time.
- A booking references exactly one guest and one room.
- Guest email and room number are unique.
- Booking dates must be in chronological order; service validation checks overlapping bookings.

## GitHub — first upload

Create an empty repository on GitHub (do not initialize it with a README if this local project already has its own). In Git Bash, from this project's root:

```bash
git init
git branch -M main
git add .
git commit -m "Initial commit: Hotel Management System"
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

Replace the URL with your actual repository URL. If `origin` already exists, update it with `git remote set-url origin <URL>` instead of running `git remote add` again. Authenticate through Git Credential Manager or another supported GitHub authentication method. Never put tokens in source code.

Subsequent changes:

```bash
git status
git add .
git commit -m "Describe your change"
git push
```

## Docker image

Build and run the application image (the application needs a reachable MySQL instance):

```bash
docker build -t hotel-management-system:local .
docker run --rm -p 8080:8080 \
  -e DB_URL='jdbc:mysql://host.docker.internal:3306/hotel_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
  -e DB_USERNAME='hotel_app' \
  -e DB_PASSWORD='replace_with_a_strong_password' \
  hotel-management-system:local
```

On Linux, `host.docker.internal` may need an explicit host-gateway mapping; alternatively run the app and database on the same Docker network. Docker Compose is the easier local option.

## AWS EC2 deployment

### Recommended layout

- Jenkins build agent: JDK 17, Maven 3.9+, Docker CLI/daemon, Git, and the Jenkins SSH Agent + Credentials Binding plugins.
- EC2 app host: Ubuntu, Docker Engine, inbound TCP 22 restricted to the Jenkins agent's trusted IP, and inbound TCP 8080 for the demo (restrict to your IP; for production use HTTPS through a reverse proxy/load balancer).
- Database: preferably Amazon RDS for MySQL in a private subnet. Do not expose MySQL port 3306 publicly. For a learning-only deployment, a separate MySQL container is possible but requires persistent storage and backup planning.

### 1. Prepare EC2

Launch an Ubuntu EC2 instance. Configure its security group with SSH (22) restricted to your own/Jenkins trusted source IP and HTTP demo port 8080 restricted to your IP. Avoid `0.0.0.0/0` for SSH. Connect using SSH and run the setup script's commands or transfer and run `deploy/ec2-setup-ubuntu.sh`.

Create `/opt/hotel-management-system/.env` on EC2. This file must contain valid credentials and database settings and should be readable only by the deployment user. Example for an RDS database:

```dotenv
DB_URL=jdbc:mysql://YOUR_RDS_ENDPOINT:3306/hotel_management_db?useSSL=true&serverTimezone=UTC
DB_USERNAME=hotel_app
DB_PASSWORD=REPLACE_WITH_A_STRONG_SECRET
SERVER_PORT=8080
```

For production RDS, configure TLS/CA verification according to your database setup, private networking, security groups, and backups. Do not commit this file. Ensure the remote deployment account can run Docker and read this file.

### 2. Create Docker Hub repository

Create a Docker Hub repository named `hotel-management-system`. For the Jenkins image push, create a Docker Hub access token with the minimum required permissions.

### 3. Configure Jenkins plugins and tools

Install/enable: Pipeline, Git, Credentials Binding, and SSH Agent plugins. Configure JDK 17 and Maven, or ensure the Jenkins agent already has `java`, `mvn`, `git`, `docker`, and `ssh` available. The pipeline uses a Docker daemon on its agent, so the Jenkins user must have permission to access it.

### 4. Add Jenkins credentials

Create these credentials with the exact IDs:

| Credential ID | Kind | Value |
|---|---|---|
| `dockerhub-credentials` | Username with password | Docker Hub username + access token |
| `ec2-deploy-ssh-key` | SSH Username with private key | SSH private key used to connect to EC2 |
| `ec2-host` | Secret text | EC2 public DNS name or IP address |
| `ec2-ssh-user` | Secret text | EC2 SSH username, commonly `ubuntu` |

Do not store database passwords in the Jenkinsfile. The app host's protected `.env` file holds runtime database settings for this demo pipeline.

### 5. Create a Jenkins Pipeline job

1. Push this repository to GitHub.
2. In Jenkins, create **New Item → Pipeline**.
3. Choose **Pipeline script from SCM**, select Git, enter your repository URL, and set the branch to `*/main`.
4. Ensure the Jenkinsfile is at the repository root.
5. Save and select **Build Now**.

Pipeline stages: checkout → Maven test/package → build and push a versioned Docker image → SSH to EC2 and replace the running container.

### 6. Verify deployment

Review the Jenkins console output. Then check on EC2:

```bash
docker ps
docker logs --tail=100 hotel-management-app
```

Open `http://EC2_PUBLIC_IP:8080` only if port 8080 is allowed by your security group. For a durable public service, configure a domain name, HTTPS, a reverse proxy/load balancer, restricted network access, health checks, and monitoring.

### CI/CD notes

- The Jenkins pipeline pushes both the build-number tag and `latest` to Docker Hub, and deploys the build-number tag to EC2.
- The Docker Hub repository must be public for the sample remote `docker pull` step to work without logging in on EC2. If it is private, configure secure Docker Hub authentication on the EC2 host.
- The EC2 host must have `/opt/hotel-management-system/.env` configured before the first pipeline deployment.
- Keep the database separate from the app container. Do not delete the database volume during routine app deployments.
- Consider adding rollback, automated smoke tests, image scanning, and a non-root restricted deployment user before production use.

## Troubleshooting

- **Cannot connect to MySQL:** verify `DB_URL`, credentials, MySQL status, security groups, and that the database allows connections from the app host.
- **Port 8080 is unreachable:** check the EC2 security group, OS firewall, container status, and `docker logs`.
- **Jenkins cannot run Docker:** ensure Docker is installed on the Jenkins agent and the Jenkins service account has access to the Docker daemon.
- **Jenkins SSH fails:** verify the SSH key credential, EC2 username, host, security group, and that the key matches the instance.
- **Docker pull fails on EC2:** verify the image name/tag and that the Docker Hub repository is public or that the EC2 host is authenticated.

## License

For learning and portfolio use. Add a license file if you intend to distribute the project under a specific license.
