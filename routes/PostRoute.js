const express = require("express");
const postModel = require("../models/PostModel");
const userModel = require("../models/UserModel");
const clubModel = require("../models/ClubModel");
const auth = require("../firebase/auth");
const notify = require("../firebase/notification");
const app = express();

// Get posts
// Pass club id with query param as {club} if posts of particular club is required
app.get("/posts", auth.loggedIn, async (req, res) => {
  // Create filter object
  const clubFilter = req.query.club ? { club: req.query.club } : {};

  // Fetch posts from most to least recent with filter
  const posts = await postModel.find(clubFilter).sort({ time: -1 });

  try {
    res.status(200).send(posts);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Add post to club with id {req.body.club} and notify subscribers
app.post("/posts", auth.isAdmin, async (req, res) => {
  try {
    // Create Post object
    // message is in req body
    // club is in req body
    // adminEmail if forwarded by auth.isAdmin middleware
    const post = new postModel({
      message: req.body.message,
      time: Date.now(),
      club: req.body.club,
      adminEmail: req.email,
    });

    await post.save();

    // Get admin details
    const user = await userModel.findOne({ email: req.email }).exec();
    const userJson = user.toJSON();

    // Get club details
    const club = await clubModel.findOne({ _id: req.body.club }).exec();
    const clubJson = club.toJSON();

    // Send response to user
    res.status(200).send(post.toJSON());

    // Notify subscribers for new post
    await notify(req.body.club, {
      ...post.toJSON(),
      adminName: userJson.name,
      adminAvatar: userJson.avatar,
      clubName: clubJson.name,
    });
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

// Delete a post
app.delete("/posts/:post", auth.isAdmin, async (req, res) => {
  try {
    const post = await postModel.deleteOne({ _id: req.params.post }).exec();
    res.status(200).send({});
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

// Update a post
app.put("/posts/:post", auth.isAdmin, async (req, res) => {
  try {
    await postModel
      .updateOne(
        { _id: req.params.post },
        { message: req.body.message, time: Date.now() }
      )
      .exec();

    // Get updated post
    const post = await postModel.findOne({ _id: req.params.post });
    const postJson = post.toJSON();

    // Get admin details
    const user = await userModel.findOne({ email: req.email }).exec();
    const userJson = user.toJSON();

    // Get club details
    const club = await clubModel.findOne({ _id: postJson.club }).exec();
    const clubJson = club.toJSON();

    // Send response to user
    res.status(200).send({});

    // Notify subscribers for new post
    await notify(postJson.club, {
      ...post.toJSON(),
      adminName: userJson.name,
      adminAvatar: userJson.avatar,
      clubName: clubJson.name,
    });
  } catch (error) {
    console.log(error);
    res.status(500).send({});
  }
});

module.exports = app;
