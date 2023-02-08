const service = require("../services/ChannelService");

const getChannels = async (req, res) => {
    try {
        let channels;
        if (req.query.clubId)
            channels = await service.getChannelsByClubId(req.query.clubId);
        else channels = await service.getAllChannels();
        res.status(200).send(channels);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getChannelByChannelId = async (req, res) => {
    try {
        const channel = await service.getChannelByChannelId(
            req.params.channelId
        );
        res.status(200).send(channel);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveChannel = async (req, res) => {
    try {
        await service.saveChannel(req.body.clubId, req.body.channelName);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteChannel = async (req, res) => {
    try {
        await service.deleteChannel(req.query.channelId);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateChannelName = async (req, res) => {
    try {
        await service.updateChannelName(req.params.channelId, req.body.name);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getChannels,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
