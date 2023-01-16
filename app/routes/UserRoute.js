const express = require("express");
const controller = require("../controllers/UserController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get own details
router.get("/", auth.loggedIn, controller.getUser);

// Get user details
router.get("/:email", auth.loggedIn, controller.getUserByEmail);

// Save user details
router.post("/", auth.loggedIn, controller.saveUser);

// Update avatar
router.post("/avatar", auth.loggedIn, controller.updateAvatar);

// Update FCM Token
router.post("/fcmtoken", auth.loggedIn, controller.updateFcmToken);

// Subscribe a club
router.put("/subscribe", auth.loggedIn, controller.subscribe);

// Unsubscribe a club
router.put("/unsubscribe", auth.loggedIn, controller.unsubscribe);

module.exports = router;
