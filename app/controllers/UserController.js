const service = require("../services/UserService");

const getUser = async (req, res) => {
    try {
        const user = await service.getUser(req.userId);
        res.status(200).send(user);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getUserByUid = async (req, res) => {
    try {
        const user = await service.getUserByUid(req.params.userId);
        res.status(200).send(user);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getAdmins = async (req, res) => {
    try {
        const admins = await service.getAdmins();
        res.status(200).send(admins);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveUser = async (req, res) => {
    try {
        await service.saveUser(req.body);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateAvatar = async (req, res) => {
    try {
        await service.updateAvatar(req.userId, req.body.avatar);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateFcmToken = async (req, res) => {
    try {
        await service.updateFcmToken(req.userId, req.body.token);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const subscribe = async (req, res) => {
    try {
        await service.subscribe(req.userId, req.body.clubId);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const unsubscribe = async (req, res) => {
    try {
        await service.unsubscribe(req.userId, req.body.clubId);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getUser,
    getUserByUid,
    getAdmins,
    saveUser,
    updateAvatar,
    updateFcmToken,
    subscribe,
    unsubscribe,
};
