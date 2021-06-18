# Dub Manager Online

The goal of this project is to provide anime fun dub community with anime dub tracking and new episode air date schedule.

Anime-related date is took from [anidb.net API](https://wiki.anidb.net/API)

# Running with docker-compose

## Running on dev machine

The following variables must present either as ENV variables or in `.env` file in project root:
  1. ANIDB_CLIENT_ID 
  2. ANIDB_CLIENT_VERSION
  3. GOOGLE_OAUTH_CLIENT_ID

```sh
docker-compose -f docker-compose.yml up -d
```

## Running in production

The following variables must present in addition to those above either as ENV variables or in `.env` file in 
project root:
  1. HOSTNAME - hostname of your server
  2. CERT_MAIL - email to use to issue let's encrypt certificate

The following variables may present for troubleshooting:
  1. CERT_STAGING - use staging let's encrypt servers, results in not valid certificates, works faster
  2. CERT_DEBUG - run nginx in debug mode
  3. JWT_KEY - key to sign JWT, random key is generated every restart if not specified

```sh
docker-compose -f docker-compose.yml -f docker-compose.hosting.yml up -d
```