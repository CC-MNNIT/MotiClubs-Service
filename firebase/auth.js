const admin = require("./config");
require("dotenv").config();

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

async function superAdmin(req, res, next) {
  const token = req.header("Authorization");
  if (!token === process.env.SUPER_ADMIN_PASSWORD) {
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
  next();
}

async function isAdmin(req, res, next) {
  const token = req.header("Authorization");
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    const user = await admin.auth().getUserByEmail(decodedToken.email);
    req.admin = user.customClaims[req.params.clubId];
    if (!req.admin) throw new Error("Unauthorized");
  } catch (error) {
    console.log(error);
    res.status(403).send({ message: "Unauthorized" });
    return;
  }
  next();
}

module.exports = { loggedIn, superAdmin, isAdmin };
