const channelRepository = require("../repository/ChannelRepository");
const postRepository = require("../repository/PostRepository");

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

const saveChannel = async (channel) => {
    const channelId = await channelRepository.saveChannel(channel);
    return channelId;
};

const deleteChannel = async (channelId) => {
    await channelRepository.deleteChannel(channelId);
    await postRepository.deletePostsByChannelId(channelId);
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
