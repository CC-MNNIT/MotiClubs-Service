const service = require("../services/UrlService");

const getUrls = async (req, res) => {
    try {
        const urls = await service.getUrls(req.query.clubId);
        res.status(200).send(urls);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveUrl = async (req, res) => {
    try {
        await service.saveUrl(req.query.clubId, req.body);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateUrl = async (req, res) => {
    try {
        await service.updateUrl(req.body);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteUrl = async (req, res) => {
    try {
        await service.deleteUrl(req.body.urlIds);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getUrls,
    saveUrl,
    updateUrl,
    deleteUrl,
};
