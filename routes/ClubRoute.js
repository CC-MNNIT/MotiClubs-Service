const express = require("express");
const clubModel = require("../models/ClubModel");
const auth = require("../firebase/auth");
const app = express();

app.get("/clubs", auth.loggedIn, async (req, res) => {
  const clubs = await clubModel.find({});
  try {
    res.status(200).send(clubs);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

module.exports = app;
