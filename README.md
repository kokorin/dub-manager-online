```
docker run -t -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=dmo -e MYSQL_USER=dmo -e MYSQL_PASSWORD=dmo -p 3306:3306 mariadb:10.3.6 --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --skip-character-set-client-handshake
```

# Running locally

Add following ENV variables server configuration (or property file):
```properties
# anidb.net integration configuration
dmo.anidb.client=${ANIDB_CLIENT_ID}
dmo.anidb.client-version=${ANIDB_CLIENT_VERSION}
# configure Spring Boot to serve static files compiles by client module (and source files for source maps)
spring.resources.static-locations=${PROJECT_PATH}/client/target/javascript/bin/js-debug,${PROJECT_PATH}/client/
```


# TODO


1. Smart Anime polling (take into account anime end date, do not refresh old anime every day)

2. Add scheduled task to pull random anime without episodes (once every minute or 5 minutes)

3. Integrate Google OAuth
