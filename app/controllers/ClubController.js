const service = require("../services/ClubService");

const getClubs = async (req, res) => {
  try {
    const clubs = await service.getClubs();
    res.status(200).send(clubs);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const updateAvatar = async (req, res) => {
  try {
    await service.updateAvatar(req.body.club, req.body.avatar);
    res.status(200).send({});
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const subscriberCount = async (req, res) => {
  try {
    const count = await service.subscriberCount(req.params.club);
    res.status(200).send({ count });
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

module.exports = {
  getClubs,
  updateAvatar,
  subscriberCount,
};
