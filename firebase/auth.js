const admin = require("./config");
require("dotenv").config();

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
    req.admin = user.customClaims[req.params.club];
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
