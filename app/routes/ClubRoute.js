const express = require("express");
const controller = require("../controllers/ClubController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get all clubs
router.get("/", auth.userAuthorization, controller.getClubs);

// Update club avatar
router.put("/:club", auth.clubAuthorization, controller.updateClub);

// Get subscriber count
router.get(
    "/subscribers-count/:club",
    auth.userAuthorization,
    controller.subscriberCount
);

module.exports = router;
