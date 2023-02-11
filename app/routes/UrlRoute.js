const express = require("express");
const controller = require("../controllers/UrlController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get urls of club by clubId in the body
router.get("/", auth.urlAuthorization, controller.getUrls);

// Save url of club by clubId in the body
router.post("/", auth.urlAuthorization, controller.saveUrl);

// Update url of club by url info in the body
router.post("/:urlId", auth.urlAuthorization, controller.updateUrl);

// Delete url of club by clubId in the body
router.delete("/:urlId", auth.urlAuthorization, controller.deleteUrl);

module.exports = router;
