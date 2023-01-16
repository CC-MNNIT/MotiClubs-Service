const express = require("express");
const controller = require("../controllers/ClubController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get all clubs
router.get("/", auth.loggedIn, controller.getClubs);

module.exports = router;
