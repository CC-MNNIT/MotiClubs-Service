const mongoose = require("mongoose");

const UserSchema = new mongoose.Schema({
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
  graduationYear: {
    type: Number,
    required: true,
  },
  course: {
    type: String,
    enum: ["B. Tech", "M. Tech", "MCA", "MBA", "PhD"],
  },
  personalEmail: {
    type: String,
    required: true,
    unique: true,
  },
  phoneNumber: {
    type: String,
    required: true,
    unique: true,
  },
});

const User = mongoose.model("User", UserSchema, "Users");

module.exports = User;
