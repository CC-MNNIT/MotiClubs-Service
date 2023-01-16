const mongoose = require("mongoose");

const UserSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      trim: true,
    },
    registrationNumber: {
      type: String,
      required: true,
      trim: true,
      unique: true,
    },
    email: {
      type: String,
      required: true,
      unique: true,
    },
    course: {
      type: String,
      enum: ["B.Tech", "M.Tech", "MCA", "MBA", "PhD"],
    },
    phoneNumber: {
      type: String,
      required: true,
      unique: true,
    },
    avatar: {
      type: String,
    },
    subscribed: {
      type: Array,
      default: [],
    },
  },
  { versionKey: false }
);

const User = mongoose.model("User", UserSchema, "Users");

module.exports = User;
