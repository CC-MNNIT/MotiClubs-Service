const express = require("express");
const clubModel = require("../models/ClubModel");
const subscriptionModel = require("../models/SubscriptionModel");
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
  // Create club object from req body
  const club = new clubModel(req.body);

  try {
    // Save club
    await club.save();

    // Create subscription doc for new Club
    const subscription = new subscriptionModel({
      club: club._id,
      subscribers: [],
    });

    // Save subscription doc
    await subscription.save();

    res.status(200).send(club.toJSON());
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

app.delete("/admin/delete_club", auth.superAdmin, async (req, res) => {
  try {
    const club = await clubModel.findById(req.query.club).exec();
    await club.remove();
    res.status(200).send({});
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Assign admin role to user with email {req.body.email}
app.post("/admin/add_admin", auth.superAdmin, async (req, res) => {
  // Extract email and club id from body
  const email = req.body.email;
  const club = req.body.club;

  // Null check
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }
  try {
    // Add email to admins array in club model with _id=club
    await clubModel.updateOne({ _id: club }, { $addToSet: { admins: email } });

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send({ message: "Failed" });
  }
});

// Unassign admin role to user with email {req.body.email}
app.post("/admin/remove_admin", auth.superAdmin, async (req, res) => {
  // Extract email and club id from body
  const email = req.body.email;
  const club = req.body.club;

  // Null check
  if (!email || !club) {
    res.status(400).send({ message: "Invalid request" });
    return;
  }

  try {
    // Remove email to admins array from club model with _id=club
    await clubModel.updateOne({ _id: club }, { $pull: { admins: email } });

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send({ message: "Failed" });
  }
});

module.exports = app;
