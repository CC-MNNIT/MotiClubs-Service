var admin = require("firebase-admin");

var serviceAccount = require("../../firebase_private_key.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

module.exports = admin;
