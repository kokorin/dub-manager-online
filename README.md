```
docker run -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=dmo -e MYSQL_USER=dmo -e MYSQL_PASSWORD=dmo -p 3306:3306 -d mariadb:10.3.6 --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --skip-character-set-client-handshake
```

# Running locally

Add following ENV variables server configuration (or property file):
```properties
# anidb.net integration configuration
dmo.anidb.client=${ANIDB_CLIENT_ID}
dmo.anidb.client-version=${ANIDB_CLIENT_VERSION}
```


# TODO

0. <s>React routing</s>

0. TypeScript REST client generation

0. Smart Anime polling (take into account anime end date, do not refresh old anime every day)

0. Add scheduled task to pull random anime without episodes (once every minute or 5 minutes)

0. Implement anidb API back off policy - don't send new requests if API responds with <error>banned</error> 

0. <s>Integrate Google OAuth</s>

0. Redoc - OpenAPI/Swagger-generated API Reference Documentation
