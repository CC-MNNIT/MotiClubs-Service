const express = require("express");
const controller = require("../controllers/UserController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get own details
router.get("/", auth.userAuthorization, controller.getUser);

// Get user details
router.get("/:email", auth.userAuthorization, controller.getUserByEmail);

// Save user details
router.post("/", auth.userAuthorization, controller.saveUser);

// Update avatar
router.post("/avatar", auth.userAuthorization, controller.updateAvatar);

// Update FCM Token
router.post("/fcmtoken", auth.userAuthorization, controller.updateFcmToken);

// Subscribe a club
router.put("/subscribe", auth.userAuthorization, controller.subscribe);

// Unsubscribe a club
router.put("/unsubscribe", auth.userAuthorization, controller.unsubscribe);

module.exports = router;
