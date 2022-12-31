const express = require("express");
const clubModel = require("../models/ClubModel");
const admin = require("../firebase/config");
const auth = require("../firebase/auth");
const app = express();

app.get("/clubs", auth.loggedIn, async (req, res) => {
  const clubs = await clubModel.find({});
  try {
    res.status(200).send(clubs);
  } catch (error) {
    res.status(500).send(error);
  }
});

app.post("/clubs", auth.superAdmin, async (req, res) => {
  const club = new clubModel(req.body);
  try {
    await club.save();
    res.status(200).send(club);
  } catch (error) {
    res.status(500).send(error);
  }
});

app.post("/clubs/add_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }
  const set = await setAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

app.post("/clubs/remove_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }
  const set = await removeAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

async function setAdmin(email, club) {
  try {
    const user = await admin.auth().getUserByEmail(email);
    await admin
      .auth()
      .setCustomUserClaims(user.uid, { ...user.customClaims, [club]: true });
  } catch {
    return false;
  }
  return true;
}

async function removeAdmin(email, club) {
  try {
    const user = await admin.auth().getUserByEmail(email);
    await admin
      .auth()
      .setCustomUserClaims(user.uid, { ...user.customClaims, [club]: false });
  } catch {
    return false;
  }
  return true;
}

module.exports = app;
