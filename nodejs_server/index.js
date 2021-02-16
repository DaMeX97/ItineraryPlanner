//Load HTTP module
const http = require("http");
const hostname = '127.0.0.1';
const port = 3000;
const cors = require('cors')

const express = require('express');
const app = express();

app.use(cors())

var jsdom = require("jsdom");
const { JSDOM } = jsdom;
const { window } = new JSDOM();
const { document } = (new JSDOM('')).window;
global.document = document;

var $ = jQuery = require('jquery')(window);

const search_routes = require("./router/search");
app.use("/search", search_routes);

app.listen(port, () => {
    console.log(`Itinerary BE listening on port ${port}!`);
});