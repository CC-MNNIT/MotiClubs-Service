const express = require("express");
const controller = require("../controllers/PostController");
const auth = require("../middlewares/auth");
const router = express.Router();

// Get posts
// Pass club id with query param as {club} if posts of particular club is required
router.get("/", auth.userAuthorization, controller.getPosts);

// Add post to club with id {req.body.club} and notify subscribers
router.post("/", auth.postAuthorization, controller.savePost);

// Delete a post
router.delete("/:post", auth.postAuthorization, controller.deletePost);

// Update a post
router.put("/:post", auth.postAuthorization, controller.updatePost);

module.exports = router;
