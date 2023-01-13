const express = require("express");
const controller = require("../controllers/PostController");
const auth = require("../middlewares/auth");
const app = express();

// Get posts
// Pass club id with query param as {club} if posts of particular club is required
app.get("/", auth.loggedIn, controller.getPosts);

// Add post to club with id {req.body.club} and notify subscribers
app.post("/", auth.isAdmin, controller.savePost);

// Delete a post
app.delete("/:post", auth.isAdmin, controller.deletePost);

// Update a post
app.put("/:post", auth.isAdmin, controller.updatePost);

module.exports = app;
