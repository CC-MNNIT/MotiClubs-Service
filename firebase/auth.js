const admin = require("./config");
require("dotenv").config();
const postModel = require("../models/PostModel");

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
    const user = await admin.auth().getUserByEmail(decodedToken.email);
    req.email = decodedToken.email;
    if (req.method !== "POST") {
      const post = await postModel.findOne({ _id: req.params.post }).exec();
      if (post.toJSON()["adminEmail"] === decodedToken.email) {
        next();
        return;
      }
    }
    let club = req.body.club;
    req.admin = user.customClaims[club];
    if (!req.admin) {
      res.status(403).send({ message: "Unauthorized" });
      return;
    }
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
  next();
}

module.exports = { loggedIn, superAdmin, isAdmin };
