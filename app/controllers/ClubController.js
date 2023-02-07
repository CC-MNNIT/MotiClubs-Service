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

const subscriberCount = async (req, res) => {
    try {
        const count = await service.subscriberCount(req.params.clubId);
        res.status(200).send({ count });
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getClubs,
    updateClub,
    subscriberCount,
};
