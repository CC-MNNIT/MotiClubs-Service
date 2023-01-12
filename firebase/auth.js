const admin = require("./config");
require("dotenv").config();
const postModel = require("../models/PostModel");
const clubModel = require("../models/ClubModel");

// Check if user is logged in
async function loggedIn(req, res, next) {
  const token = req.header("Authorization");
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    req.email = decodedToken.email;
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Please log in first" });
    return;
  }
  next();
}

// Check if user is super admin
async function superAdmin(req, res, next) {
  const token = req.header("Authorization");
  if (!token === process.env.SUPER_ADMIN_PASSWORD) {
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
  next();
}

// Check if user is admin of the club
async function isAdmin(req, res, next) {
  const token = req.header("Authorization");
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    req.email = decodedToken.email;
    if (req.method !== "POST") {
      const post = await postModel.findOne({ _id: req.params.post }).exec();
      if (post.toJSON()["adminEmail"] === req.email) {
        next();
        return;
      }
    }
    let clubId = req.body.club;
    const club = await clubModel.findOne({ _id: clubId }).exec();
    const admins = club.toJSON().admins;
    for (let i = 0; i < admins.length; ++i) {
      if (admins[i] === req.email) {
        next();
        return;
      }
    }
    res.status(403).send({ message: "Unauthorized" });
    return;
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
}

module.exports = { loggedIn, superAdmin, isAdmin };
