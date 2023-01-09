const express = require("express");
const admin = require("../firebase/config");
const userModel = require("../models/UserModel");
const subscriptionModel = require("../models/SubscriptionModel");
const fcmTokenModel = require("../models/FcmTokenModel");
const auth = require("../firebase/auth");
const app = express();

// Get own details
app.get("/user", auth.loggedIn, async (req, res) => {
  // Get email from auth.loggedIn middleware
  const email = req.email;

  const user = await userModel.findOne({ email: email }).exec();

  // Get custom claims
  const adminOf = await getCustomClaims(email);

  // Extract list of clubs that user is admin of from custom claims
  const arr = [];
  Object.keys(adminOf).forEach((key) => {
    if (adminOf[key]) {
      arr.push(key);
    }
  });

  // Append admin array to user model
  const userWithAdminList = { ...user.toJSON(), admin: arr };

  // Remove unwanted property
  delete userWithAdminList._id;

  try {
    res.status(200).send(userWithAdminList);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Get user details
app.get("/user/:email", auth.loggedIn, async (req, res) => {
  try {
    // Get user from email
    const user = await userModel.findOne({ email: req.params.email }).exec();
    const userJson = user.toJSON();

    // Send relevant details
    res.status(200).send({
      name: userJson.name,
      personalEmail: userJson.personalEmail,
      phoneNumber: userJson.phoneNumber,
      avatar: userJson.avatar,
    });
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Save user details
app.post("/user", auth.loggedIn, async (req, res) => {
  // Get user from request body
  const user = new userModel(req.body);
  try {
    await user.save();
    res.status(200).send({ ...req.body, admin: [] });
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Update avatar
app.post("/user/avatar", auth.loggedIn, async (req, res) => {
  // Get email from auth.loggedIn middleware
  const email = req.email;
  try {
    // Get user from email
    const user = await userModel.findOne({ email: email }).exec();

    // Update {avatar: ""}
    await user.updateOne(req.body);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Update FCM Token
app.post("/user/fcmtoken", auth.loggedIn, async (req, res) => {
  // Get email from auth.loggedIn middleware
  const email = req.email;
  try {
    // Find fcmToken document
    const fcmToken = await fcmTokenModel.findOne({ user: email }).exec();

    // Add new doc if not already added else update existing
    if (fcmToken === null) {
      const newToken = new fcmTokenModel({ ...req.body, user: email });
      await newToken.save();
    } else await fcmToken.updateOne(req.body);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Subscribe a club
app.put("/user/subscribe", auth.loggedIn, async (req, res) => {
  // Get email from auth.loggedIn middleware
  const email = req.email;
  try {
    // Find user
    const user = await userModel.findOne({ email: email }).exec();

    // Add club id to subscribed array
    await user.updateOne({ $addToSet: { subscribed: req.body.club } });

    // Get subscription doc for req.body.club
    const subscription = await subscriptionModel.findOne({
      club: req.body.club,
    });

    // Add user email to subscription list
    await subscription.updateOne({ $addToSet: { subscribers: email } });

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Unsubscribe a club
app.put("/user/unsubscribe", auth.loggedIn, async (req, res) => {
  // Get email from auth.loggedIn middleware
  const email = req.email;
  try {
    // Find user
    const user = await userModel.findOne({ email: email }).exec();

    // Remove club id from subscribed array
    await user.updateOne({ $pull: { subscribed: req.body.club } });

    // Get subscription doc for req.body.club
    const subscription = await subscriptionModel.findOne({
      club: req.body.club,
    });

    // Remove user email from subscription list
    await subscription.updateOne({ $pull: { subscribers: email } });

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Utility function to get custom claims of user from email
async function getCustomClaims(email) {
  const user = await admin.auth().getUserByEmail(email);
  return user.hasOwnProperty("customClaims") ? user.customClaims : {};
}

module.exports = app;
