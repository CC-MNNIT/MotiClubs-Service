const service = require("../services/AdminService");

const getClubs = async (req, res) => {
  try {
    const clubs = await service.getClubs();

    res.status(200).send(clubs);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const saveClub = async (req, res) => {
  try {
    const club = await service.saveClub(req.body);

    res.status(200).send(club.toJSON());
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const deleteClub = async (req, res) => {
  try {
    await service.deleteClub(req.query.club);

    res.status(200).send({});
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

const assignAdmin = async (req, res) => {
  try {
    // Extract email and club id from body
    const email = req.body.email;
    const club = req.body.club;

    await service.updateAdmin(email, club, true);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(500).send({ message: error.message });
  }
};

const unassignAdmin = async (req, res) => {
  try {
    // Extract email and club id from body
    const email = req.body.email;
    const club = req.body.club;

    await service.updateAdmin(email, club, false);

    res.status(200).send(req.body);
  } catch (error) {
    console.log(error);
    res.status(400).send({ message: error.message });
  }
};

module.exports = {
  getClubs,
  saveClub,
  deleteClub,
  assignAdmin,
  unassignAdmin,
};
