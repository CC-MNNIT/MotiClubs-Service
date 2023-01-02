const mongoose = require("mongoose");

const PostSchema = mongoose.Schema(
  {
    message: {
      type: String,
      required: true,
      trim: true,
    },
    time: {
      type: Number,
      required: true,
    },
    club: {
      type: String,
      required: true,
      trim: true,
    },
  },
  { versionKey: false }
);

const Post = mongoose.model("Post", PostSchema, "Posts");

module.exports = Post;
