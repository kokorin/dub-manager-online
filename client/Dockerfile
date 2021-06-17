FROM node:16-alpine AS build

WORKDIR client
# Caching layers
COPY *.json /client/
RUN npm install

# Build
COPY src /client/src
COPY public /client/public
RUN npm run build

FROM alpine
COPY --from=build /client/build /build
#VOLUME /usr/share/nginx

ENTRYPOINT cp -r /build/* /usr/share/nginx/html
