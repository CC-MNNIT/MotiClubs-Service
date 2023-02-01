const admin = require("../config/firebase");
require("dotenv").config();
const postModel = require("../models/PostModel");
const clubModel = require("../models/ClubModel");

// Check if user is logged in
async function userAuthorization(req, res, next) {
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

// Check if user is authorized to add / update post
async function postAuthorization(req, res, next) {
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
    const isAdmin = await clubAdminCheck(req.email, req.body.club);
    if (isAdmin) {
      next();
      return;
    }
    res.status(403).send({ message: "Unauthorized" });
    return;
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
}

// Check if user is authorized to work update club
const clubAuthorization = async (req, res, next) => {
  const token = req.header("Authorization");
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    req.email = decodedToken.email;
    const isAdmin = await clubAdminCheck(req.email, req.params.club);
    if (isAdmin) {
      next();
      return;
    }
    res.status(403).send({ message: "Unauthorized" });
    return;
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
};

// Check is the user with email (email) is admin of club with id clubId
const clubAdminCheck = async (email, clubId) => {
  try {
    const club = await clubModel.findOne({ _id: clubId }).exec();
    const admins = club.toJSON()["admins"];
    for (let i = 0; i < admins.length; ++i)
      if (admins[i] === email) return true;
    return false;
  } catch (error) {
    console.log(error);
    return false;
  }
};

module.exports = {
  userAuthorization,
  superAdmin,
  postAuthorization,
  clubAuthorization,
};
