# NewsList

Shows user-specific "news items" after SAML2 login. News items are loaded from CSV files on disk and matched against a SAML2 user attribute.

## Demo (Docker Compose)

The `demo/` folder contains a ready-to-run setup including a test SAML IdP.

```bash
cd demo
docker compose up
```

- Open http://localhost:8080
- Log in with `user1` / `user1pass`

## Configuration

Configuration is done via `application.yml`.

Spring Boot also reads external config from `./application.yml` and `./config/application.yml` relative to the working directory. In the Docker demo, the config is mounted to `/usr/src/newslist/config/application.yml`.

## News CSVs

### Configuration keys

- `news.csv-path`: Directory that contains the `.csv` files (the directory is scanned recursively).
- `news.csv-format`: Apache Commons CSV predefined format name (see `CSVFormat.Predefined`), e.g. `Default`.
- `news.saml2-key`: The SAML2 attribute name whose value is used as the lookup key (demo: `uid`).

### File layout

- Put one or more `.csv` files into the directory configured via `news.csv-path`.
- All `.csv` files found under that path are loaded at startup (sorted by file path/name).

The filename (without `.csv`) becomes the **title** of the news item. Underscores are removed.

Example filenames:

- `News Item 1.csv` → title: `News Item 1`
- `Security_Notice.csv` → title: `SecurityNotice`

### CSV rows (format)

Each CSV record must have **exactly two columns**:

1. **Key**: must match the logged-in user’s SAML2 attribute value (as configured by `news.saml2-key`).
2. **Content**: the text to display for that news item.

Records with anything other than exactly two columns are skipped.

Examples (using `news.csv-format: Default`):

```csv
user1,Welcome to NewsList!
user2,"This message contains a comma, so it is quoted."
```

### Restart required after changes

CSV files are parsed **once on application startup**. If you add/edit/remove CSV files, you must **restart the application** for changes to take effect.

Examples:

- Docker Compose: `docker compose restart newslist`
- Plain Docker run: stop the container and start it again

## Build

```bash
./gradlew bootJar
```

## Docker

### Sample docker-compose.yml

```yaml
services:
	newslist:
		image: ghcr.io/kit-sdq/newslist:latest
		container_name: "newslist"
		restart: unless-stopped
		volumes:
			- ./config/:/usr/src/newslist/config/:ro
```

### Build locally

```bash
docker build -t newslist:local .
```

### Run locally

NewsList runs from `/usr/src/newslist/news.jar` and will pick up external config from `/usr/src/newslist/config/application.yml`.

```bash
docker run -p 8080:8080 \
	-v $(pwd)/demo/application.yml:/usr/src/newslist/config/application.yml:ro \
	-v $(pwd)/demo/news:/usr/src/newslist/config/news:ro \
	newslist:local
```

## Published Docker images

This repository publishes images to GitHub Container Registry (GHCR):

- `ghcr.io/kit-sdq/newslist:latest` is built from the `main` branch.
- Git tags like `v1.2.3` are published as `ghcr.io/kit-sdq/newslist:1.2.3`.