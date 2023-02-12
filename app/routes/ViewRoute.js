const express = require("express");
const controller = require("../controllers/ViewController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get view count of a post
router.get("/", auth.userAuthorization, controller.getViewCount);

// Add a view
router.post("/", auth.userAuthorization, controller.addView);

module.exports = router;
