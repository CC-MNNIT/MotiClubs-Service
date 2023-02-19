const express = require("express");
const controller = require("../controllers/ClubController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get all clubs
router.get("/", auth.userAuthorization, controller.getClubs);

// Update club details
router.put("/:clubId", auth.clubAuthorization, controller.updateClub);

// Get subscribers
router.get(
    "/subscribers/:clubId",
    auth.userAuthorization,
    controller.subscribers
);

module.exports = router;
