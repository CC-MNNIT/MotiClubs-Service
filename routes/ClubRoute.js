const express = require("express");
const clubModel = require("../models/ClubModel");
const auth = require("../firebase/auth");
const app = express();

app.get("/clubs", auth.loggedIn, async (req, res) => {
  if (!req.email) {
    res.status(403).send({ message: "Please log in first" });
    return;
  }
  const clubs = await clubModel.find({});
  try {
    res.send(clubs);
  } catch (error) {
    res.status(500).send(error);
  }
});

app.post("/clubs", auth.superAdmin, async (req, res) => {
  if (!req.superAdmin) {
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
  const club = new clubModel(req.body);
  try {
    await club.save();
    res.send(club);
  } catch (error) {
    res.status(500).send(error);
  }
});

app.post("/clubs/add_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({});
    return;
  }
  const set = await auth.setAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

app.post("/clubs/remove_admin", auth.superAdmin, async (req, res) => {
  const email = req.body.email;
  const club = req.body.club;
  if (!email || !club) {
    res.status(400).send({});
    return;
  }
  const set = await auth.removeAdmin(email, club);
  res
    .status(set ? 200 : 500)
    .send(set ? { email, club } : { message: "Failed" });
});

module.exports = app;
