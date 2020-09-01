```
docker run -t -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=dmo -e MYSQL_USER=dmo -e MYSQL_PASSWORD=dmo -p 3306:3306 mariadb:10.3.6 --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --skip-character-set-client-handshake
```

# TODO

0. Setup IDEa to load Royale classes (to suppress code errors)

1. Smart Anime polling (take into account anime end date, do not refresh old anime every day)

2. Add scheduled task to pull random anime without episodes (once every minute or 5 minutes)

3. Integrate Google OAuth
