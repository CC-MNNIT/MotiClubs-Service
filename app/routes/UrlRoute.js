const express = require("express");
const controller = require("../controllers/UrlController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get urls of club by clubId in the query
router.get("/", auth.userAuthorization, controller.getUrls);

// Save urls of club by url objects in body
router.post("/add", auth.urlAuthorization, controller.saveUrl);

// Update urls of club by urls info in the body
router.post("/update", auth.urlAuthorization, controller.updateUrl);

// Delete urls of club by urlIds in the body
router.post("/delete", auth.urlAuthorization, controller.deleteUrl);

module.exports = router;
