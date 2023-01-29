const express = require("express");
const controller = require("../controllers/ClubController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get all clubs
router.get("/", auth.loggedIn, controller.getClubs);

// Update club avatar
router.post("/avatar", auth.isAdmin, controller.updateAvatar);

// Get subscriber count
router.get(
  "/subscribers-count/:club",
  auth.loggedIn,
  controller.subscriberCount
);

module.exports = router;
