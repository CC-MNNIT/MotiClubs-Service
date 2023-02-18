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

const updateClub = async (req, res) => {
    try {
        await service.updateClub(req.params.clubId, req.body);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const subscribers = async (req, res) => {
    try {
        const subscribers = await service.subscribers(req.params.clubId);
        res.status(200).send(subscribers);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getClubs,
    updateClub,
    subscribers,
};
