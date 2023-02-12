const express = require("express");
const controller = require("../controllers/UrlController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get urls of club by clubId in the query
router.get("/", auth.userAuthorization, controller.getUrls);

// Save / Update urls of club by url objects in body
router.post("/", auth.urlAuthorization, controller.saveUrl);

module.exports = router;
