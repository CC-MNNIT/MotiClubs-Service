const express = require("express");
const postModel = require("../models/PostModel");
const userModel = require("../models/UserModel");
const auth = require("../firebase/auth");
const notify = require("../firebase/notification");
const app = express();

// Get all posts
app.get("/posts", auth.loggedIn, async (req, res) => {
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
    const user = await userModel.findOne({ email: req.email });
    const post = new postModel({
      message: req.body.message,
      time: Date.now(),
      club: req.params.club,
      adminName: user.name,
      adminEmail: user.email,
      adminPhone: user.phoneNumber,
    });
    await post.save();
    await notify(req.params.club, post.toJSON());
    res.status(200).send(post.toJSON());
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

module.exports = app;
