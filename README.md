# Demo

- Start the application.
- Visit localhost:8080
- Log in with (user1, user1pass)

# Usage

- Compile the application
- Overwrite the configuration using a custom `application.yml` file

# Docker
```bash
# Run the following commands to build a docker image
docker build -t repository/tag .

# Newslist gets executed at /usr/src/newslist/news.jar
# Map a custom application.yml into /usr/src/newslist/config. For example:
docker run -p 8080:8080 -v $(pwd)/demo/:/usr/src/newslist/config/ repository/tag
```