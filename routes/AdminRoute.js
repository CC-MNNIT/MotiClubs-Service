const express = require("express");
const clubModel = require("../models/ClubModel");
const subscriptionModel = require("../models/SubscriptionModel");
const admin = require("../firebase/config");
const auth = require("../firebase/auth");
const app = express();

// Get list of all clubs
app.get("/admin/get_club", auth.superAdmin, async (req, res) => {
  const clubs = await clubModel.find({});
  try {
    res.status(200).send(clubs);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Add a club
app.post("/admin/add_club", auth.superAdmin, async (req, res) => {
  const club = new clubModel(req.body);
  try {
    await club.save();
    const subscription = new subscriptionModel({
      club: club._id,
      subscribers: [],
    });
    await subscription.save();
    res.status(200).send(club.toJSON());
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Assign admin role to user with email {req.body.email}
app.post("/admin/add_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }
  await clubModel.updateOne({ _id: club }, { $addToSet: { admins: email } });
  const set = await setAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

// Unassign admin role to user with email {req.body.email}
app.post("/admin/remove_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }
  await clubModel.updateMany({ _id: club }, { $pull: { admins: email } });
  const set = await removeAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

// Utility function to add admin role to custom claims of user
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

// Utility function to remove admin role from custom claims of user
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
