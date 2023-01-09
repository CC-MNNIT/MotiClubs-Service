const express = require("express");
const postModel = require("../models/PostModel");
const userModel = require("../models/UserModel");
const auth = require("../firebase/auth");
const notify = require("../firebase/notification");
const app = express();

// Get all posts
app.get("/posts", auth.loggedIn, async (req, res) => {
  // Fetch all posts from most to least recent
  const posts = await postModel.find({}).sort({ time: -1 });

  try {
    res.status(200).send(posts);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Get posts of club with id {club}
app.get("/posts/:club", auth.loggedIn, async (req, res) => {
  // Fetch posts belonging to the club with id req.params.club
  // Posts are sorted from most to least recent
  const posts = await postModel
    .find({ club: req.params.club })
    .sort({ time: -1 });

  try {
    res.status(200).send(posts);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Add post to club with id {club} and notify subscribers
app.post("/posts/:club", auth.isAdmin, async (req, res) => {
  try {
    // Create Post object
    // message is in req body
    // club is in req param
    // adminEmail if forwarded by auth.isAdmin middleware
    const post = new postModel({
      message: req.body.message,
      time: Date.now(),
      club: req.params.club,
      adminEmail: req.email,
    });

    await post.save();

    // Send response to user
    res.status(200).send(post.toJSON());

    // Notify subscribers for new post
    await notify(req.params.club, post.toJSON());
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

module.exports = app;
