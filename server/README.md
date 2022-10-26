# OAuth2

This app uses OAuth2.

Read about OAuth resource-server multi-tenancy in Spring:

https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver-multitenancy

## Google Auth Provider

Create client ID and secret: https://console.cloud.google.com/apis/credentials

## GitHub Auth Provider

Create client ID and secret: https://github.com/settings/applications/new

# Running DB locally

Shell
```
docker run -e MYSQL_RANDOM_ROOT_PASSWORD=yes \
           -e MYSQL_DATABASE=dmo \
           -e MYSQL_USER=dmo \
           -e MYSQL_PASSWORD=dmo \
           -e MYSQL_CHARSET=utf8mb4 \
           -e MYSQL_COLLATION=utf8mb4_general_ci \
           -p 3306:3306 \
           -d yobasystems/alpine-mariadb \
           --skip-character-set-client-handshake
```

PowerShell
```
docker run -e MYSQL_RANDOM_ROOT_PASSWORD=yes `
           -e MYSQL_DATABASE=dmo `
           -e MYSQL_USER=dmo `
           -e MYSQL_PASSWORD=dmo `
           -e MYSQL_CHARSET=utf8mb4 `
           -e MYSQL_COLLATION=utf8mb4_general_ci `
           -p 3306:3306 `
           -d yobasystems/alpine-mariadb `
           --skip-character-set-client-handshake
```

# JDK Version 11

# Build & configuration

## IntelliJ IDEA

This project uses [maven-git-versioning-extension](https://github.com/qoomon/maven-git-versioning-extension)
to set project version automatically.
Check its [intellij-setup](https://github.com/qoomon/maven-git-versioning-extension#intellij-setup) documentation.

# Running locally

Add following ENV variables server configuration (or property file):
```properties
# anidb.net integration configuration
dmo.anidb.client.name=${ANIDB_CLIENT_ID}
dmo.anidb.client-version=${ANIDB_CLIENT_VERSION}
```

To get client ID and client version register on [anidb.net](https://anidb.net/) and add your
[project and client](https://anidb.net/perl-bin/animedb.pl?show=client) 

