const admin = require("./config");
require("dotenv").config();

async function loggedIn(req, res, next) {
  const token = req.header("Authorization");
  try {
    const decodedToken = await admin.auth().verifyIdToken(token);
    req.email = decodedToken.email;
  } catch (error) {
    console.log(error);
  }
  next();
}

async function superAdmin(req, res, next) {
  const token = req.header("Authorization");
  req.superAdmin = token === process.env.SUPER_ADMIN_PASSWORD;
  next();
}

async function setAdmin(email, club) {
  try {
    const user = await admin.auth().getUserByEmail(email);
    await admin.auth().setCustomUserClaims(user.uid, { [club]: true });
  } catch {
    return false;
  }
  return true;
}

async function removeAdmin(email, club) {
  try {
    const user = await admin.auth().getUserByEmail(email);
    await admin.auth().setCustomUserClaims(user.uid, { [club]: false });
  } catch {
    return false;
  }
  return true;
}

async function isAdmin(email, club) {
  try {
    const user = await admin.auth().getUserByEmail(req.email);
    req.admin = user.customClaims[club];
  } catch (error) {
    req.admin = false;
  }
  next();
}

module.exports = { loggedIn, superAdmin, setAdmin, removeAdmin, isAdmin };
