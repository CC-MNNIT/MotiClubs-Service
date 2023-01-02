const express = require("express");
const postModel = require("../models/PostModel");
const auth = require("../firebase/auth");
const app = express();

app.get("/posts", auth.loggedIn, async (req, res) => {
  const posts = await postModel.find({}).sort({ time: -1 });
  try {
    res.status(200).send(posts);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

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

app.post("/posts/:club", auth.isAdmin, async (req, res) => {
  const post = new postModel({
    message: req.body.message,
    time: Date.now(),
    club: req.params.club,
  });
  try {
    await post.save();
    res.status(200).send(post.toJSON());
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

module.exports = app;
