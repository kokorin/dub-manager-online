const {createProxyMiddleware} = require("http-proxy-middleware");

const backend = {
    target: "http://localhost:8080",
    // Do not change HOST header, otherwise Spring won't detect real host correctly
    changeOrigin: false,
};

module.exports = function (app) {
    app.use("/api", createProxyMiddleware(backend));
    app.use("/login/oauth2", createProxyMiddleware(backend));
    app.use("/oauth2", createProxyMiddleware(backend));
};
