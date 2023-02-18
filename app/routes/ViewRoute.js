const express = require("express");
const controller = require("../controllers/ViewController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get list of views of a post
router.get("/", auth.userAuthorization, controller.getViews);

// Add a view
router.post("/", auth.userAuthorization, controller.addView);

module.exports = router;
