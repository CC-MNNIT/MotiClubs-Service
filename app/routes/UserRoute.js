const express = require("express");
const controller = require("../controllers/UserController");
const auth = require("../middlewares/auth");
const app = express();

// Get own details
app.get("/", auth.loggedIn, controller.getUser);

// Get user details
app.get("/:email", auth.loggedIn, controller.getUserByEmail);

// Save user details
app.post("/", auth.loggedIn, controller.saveUser);

// Update avatar
app.post("/avatar", auth.loggedIn, controller.updateAvatar);

// Update FCM Token
app.post("/fcmtoken", auth.loggedIn, controller.updateFcmToken);

// Subscribe a club
app.put("/subscribe", auth.loggedIn, controller.subscribe);

// Unsubscribe a club
app.put("/unsubscribe", auth.loggedIn, controller.unsubscribe);

module.exports = app;
