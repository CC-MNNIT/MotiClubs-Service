const channelRepository = require("../repository/ChannelRepository");

const getAllChannels = async () => {
    const channels = await channelRepository.getAllChannels();
    return channels;
};

const getChannelByClubId = async (clubId) => {
    const channels = await channelRepository.getChannelByClubId(clubId);
    return channels;
};

const getChannelByChannelId = async (channelId) => {
    const channels = await channelRepository.getChannelByChannelId(channelId);
    return channels;
};

const saveChannel = async (clubId, channelName) => {
    const channelId = await channelRepository.saveChannel(clubId, channelName);
    return channelId;
};

const deleteChannel = async (channelId) => {
    await channelRepository.deleteChannel(channelId);
};

const updateChannelName = async (channelId, channelName) => {
    await channelRepository.updateChannelName(channelId, channelName);
};

module.exports = {
    getAllChannels,
    getChannelByClubId,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
