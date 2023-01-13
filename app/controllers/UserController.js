const service = require("../services/UserService");

const getUser = async (req, res) => {
  try {
    // Get email from auth.loggedIn middleware
    const user = await service.getUser(req.email);

    res.status(200).send(user);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const getUserByEmail = async (req, res) => {
  try {
    const user = await service.getUserByEmail(req.params.email);

    res.status(200).send(user);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const saveUser = async (req, res) => {
  try {
    await service.saveUser(req.body);
    res.status(200).send({ ...req.body, admin: [] });
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const updateAvatar = async (req, res) => {
  try {
    // Get email from auth.loggedIn middleware
    await service.updateAvatar(req.email, req.body.avatar);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const updateFcmToken = async (req, res) => {
  try {
    await service.updateFcmToken(req.email, req.body.token);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const subscribe = async (req, res) => {
  try {
    // Get email from auth.loggedIn middleware
    await service.subscribe(req.email, req.body.club);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const unsubscribe = async (req, res) => {
  try {
    // Get email from auth.loggedIn middleware
    await service.unsubscribe(req.email, req.body.club);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

module.exports = {
  getUser,
  getUserByEmail,
  saveUser,
  updateAvatar,
  updateFcmToken,
  subscribe,
  unsubscribe,
};
