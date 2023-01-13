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

module.exports = {
  getClubs,
};
