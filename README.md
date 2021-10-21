# Demo

- Start the application.
- Visit localhost:8080
- Log in with (user, password)

# Usage

- Compile the application
- Overwrite the configuration using a custom `application.yml` file

# Docker
```bash
# Run the following commands to build a docker image
./gradlew build
docker build -t repository/tag .

# Map a custom application.yml into /config and a news folder to /news. For example:
docker run -p 8080:8080 -v path/to/news:/news -v path/to/src/main/resources:/config repository/tag 
```