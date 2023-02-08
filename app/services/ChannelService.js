const channelRepository = require("../repository/ChannelRepository");

const getAllChannels = async () => {
    const channels = await channelRepository.getAllChannels();
    return channels;
};

const getChannelsByClubId = async (clubId) => {
    const channels = await channelRepository.getChannelsByClubId(clubId);
    return channels;
};

const getChannelByChannelId = async (channelId) => {
    const channel = await channelRepository.getChannelByChannelId(channelId);
    return channel;
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
    getChannelsByClubId,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
