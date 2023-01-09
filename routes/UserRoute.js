const express = require("express");
const admin = require("../firebase/config");
const userModel = require("../models/UserModel");
const subscriptionModel = require("../models/SubscriptionModel");
const fcmTokenModel = require("../models/FcmTokenModel");
const auth = require("../firebase/auth");
const app = express();

// Get own details
app.get("/user", auth.loggedIn, async (req, res) => {
  const email = req.email;
  const user = await userModel.findOne({ email: email }).exec();
  const adminOf = await getAdminList(email);
  const arr = [];
  Object.keys(adminOf).forEach((key) => {
    if (adminOf[key]) {
      arr.push(key);
    }
  });
  const userWithAdminList = { ...user.toJSON(), admin: arr };
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
    const user = await userModel.findOne({ email: req.params.email }).exec();
    const userJson = user.toJSON();
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
  const email = req.email;
  try {
    const user = await userModel.findOne({ email: email }).exec();
    await user.updateOne(req.body);
    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Update FCM Token
app.post("/user/fcmtoken", auth.loggedIn, async (req, res) => {
  const email = req.email;
  try {
    const fcmToken = await fcmTokenModel.findOne({ user: email }).exec();
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
  const email = req.email;
  try {
    const user = await userModel.findOne({ email: email }).exec();
    await user.updateOne({ $addToSet: { subscribed: req.body.club } });
    const subscription = await subscriptionModel.findOne({
      club: req.body.club,
    });
    await subscription.updateOne({ $addToSet: { subscribers: email } });
    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Unsubscribe a club
app.put("/user/unsubscribe", auth.loggedIn, async (req, res) => {
  const email = req.email;
  try {
    const user = await userModel.findOne({ email: email }).exec();
    await user.updateOne({ $pull: { subscribed: req.body.club } });
    const subscription = await subscriptionModel.findOne({
      club: req.body.club,
    });
    await subscription.updateOne({ $pull: { subscribers: email } });
    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Utility function to get list of clubs whose admin is the user with email {email}
async function getAdminList(email) {
  const user = await admin.auth().getUserByEmail(email);
  return user.hasOwnProperty("customClaims") ? user.customClaims : {};
}

module.exports = app;
