const mongoose = require("mongoose");

const FcmTokenSchema = new mongoose.Schema(
  {
    user: {
      type: String,
      required: true,
      trim: true,
    },
    token: {
      type: String,
      required: true,
      trim: true,
    },
  },
  { versionKey: false }
);

const FcmToken = mongoose.model("FcmToken", FcmTokenSchema, "FcmTokens");

module.exports = FcmToken;
