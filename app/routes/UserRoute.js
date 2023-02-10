const express = require("express");
const controller = require("../controllers/UserController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Save user details
router.post("/", auth.signUpAuthorization, controller.saveUser);

// Get own details
router.get("/", auth.userAuthorization, controller.getUser);

// Get user details
router.get("/:userId", auth.userAuthorization, controller.getUserByUid);

// Get all admins
router.get("/admins", auth.userAuthorization, controller.getAdmins);

// Update avatar
router.post("/avatar", auth.userAuthorization, controller.updateAvatar);

// Update FCM Token
router.post("/fcmtoken", auth.userAuthorization, controller.updateFcmToken);

// Subscribe a club
router.post("/subscribe", auth.userAuthorization, controller.subscribe);

// Unsubscribe a club
router.post("/unsubscribe", auth.userAuthorization, controller.unsubscribe);

module.exports = router;
