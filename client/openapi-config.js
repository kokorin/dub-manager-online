/** @type {import("@rtk-query/codegen-openapi").ConfigFile} */
const config = {
    apiFile: "./src/api/base.ts",
    schemaFile: "../server/src/test/resources/dmo/server/openapi_v1.json",
    outputFile: "./src/api/generated.ts",
    apiImport: "baseApi",
    exportName: "generatedApi",
    hooks: true,
}

module.exports = config;
