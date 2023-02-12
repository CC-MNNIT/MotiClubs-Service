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
        await service.saveUrl(req.query.clubId, req.body.urls);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getUrls,
    saveUrl,
};
