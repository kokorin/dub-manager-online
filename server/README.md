```
docker run -t -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=dmo -e MYSQL_USER=dmo -e MYSQL_PASSWORD=dmo -p 3306:3306 mariadb:10.3.6 --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --skip-character-set-client-handshake
```