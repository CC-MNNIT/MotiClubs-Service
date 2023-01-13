const express = require("express");
const controller = require("../controllers/ClubController");
const auth = require("../middlewares/auth");
const app = express();

// Get all clubs
app.get("/", auth.loggedIn, controller.getClubs);

module.exports = app;
