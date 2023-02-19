const service = require("../services/SuperAdminService");

const login = async (req, res) => {
    res.status(200).send({});
};

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
        const clubId = await service.saveClub(req.body);
        console.log(clubId);
        res.status(200).send({ cid: clubId });
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteClub = async (req, res) => {
    try {
        await service.deleteClub(req.query.clubId);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const assignAdmin = async (req, res) => {
    try {
        // Extract email and club id from body
        const clubId = req.body.clubId;
        const email = req.body.email;

        await service.addAdmin(email, clubId);

        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(500).send({ message: error.message });
    }
};

const unassignAdmin = async (req, res) => {
    try {
        // Extract email and club id from body
        const userId = req.body.userId;
        const clubId = req.body.clubId;

        await service.removeAdmin(userId, clubId);

        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    login,
    getClubs,
    saveClub,
    deleteClub,
    assignAdmin,
    unassignAdmin,
};
