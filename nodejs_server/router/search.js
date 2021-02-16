"use strict";

var express = require('express');
var router = express.Router();

const search_service=require("../services/search");

router.get("/:place", search_service.plan);

module.exports = router;