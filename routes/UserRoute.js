const express = require("express");
const admin = require("../firebase/config");
const userModel = require("../models/UserModel");
const auth = require("../firebase/auth");
const app = express();

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

async function getAdminList(email) {
  const user = await admin.auth().getUserByEmail(email);
  return user.hasOwnProperty("customClaims") ? user.customClaims : {};
}

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

app.put("/user", auth.loggedIn, async (req, res) => {
  const email = req.email;
  try {
    if (email !== req.body.email) {
      res.status(401).send({ message: "Primary email can't be updaed" });
      return;
    }
    const user = await userModel.findOne({ email: email }).exec();
    await user.update(req.body);
    res.status(200).send({ ...req.body, admin: [] });
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

module.exports = app;
