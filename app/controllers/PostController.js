const notify = require("../utility/notification");
const service = require("../services/PostService");
const userModel = require("../models/UserModel");
const clubModel = require("../models/ClubModel");

const getPosts = async (req, res) => {
  try {
    const posts = await service.getPosts(req.query.club);
    res.status(200).send(posts);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const savePost = async (req, res) => {
  try {
    const message = req.body.message;
    const clubId = req.body.club;
    const adminEmail = req.email;

    const post = await service.savePost(message, clubId, adminEmail);

    // Send response to user
    res.status(200).send(post.toJSON());

    // Get admin details
    const user = await userModel.findOne({ email: adminEmail }).exec();
    const userJson = user.toJSON();

    // Get club details
    const club = await clubModel.findOne({ _id: clubId }).exec();
    const clubJson = club.toJSON();

    // Notify subscribers for new post
    await notify(req.body.club, {
      ...post.toJSON(),
      adminName: userJson.name,
      adminAvatar: userJson.avatar,
      clubName: clubJson.name,
      updated: "0",
    });
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const deletePost = async (req, res) => {
  try {
    await service.deletePost(req.params.post);

    res.status(200).send({});
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const updatePost = async (req, res) => {
  try {
    const post = await service.updatePost(req.params.post, req.body.message);
    const postJson = post.toJSON();

    // Send response to user
    res.status(200).send({});

    // Get admin details
    const user = await userModel.findOne({ email: req.email }).exec();
    const userJson = user.toJSON();

    // Get club details
    const club = await clubModel.findOne({ _id: postJson.club }).exec();
    const clubJson = club.toJSON();

    // Notify subscribers for new post
    await notify(postJson.club, {
      ...post.toJSON(),
      adminName: userJson.name,
      adminAvatar: userJson.avatar,
      clubName: clubJson.name,
      updated: "1",
    });
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

module.exports = {
  getPosts,
  savePost,
  deletePost,
  updatePost,
};
