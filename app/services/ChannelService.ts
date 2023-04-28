import { Channel } from "../models/Channel";
import { ChannelRepository } from "../repository/ChannelRepository";
import { PostRepository } from "../repository/PostRepository";
import validate from "../utility/validate";

const getAllChannels = async () => await ChannelRepository.getAllChannels();

const getChannelsByClubId = async (clubId: number) => {
    validate([clubId]);

    await ChannelRepository.getChannelsByClubId(clubId);
};

const getChannelByChannelId = async (channelId: number) => {
    validate([channelId]);

    await ChannelRepository.getChannelByChannelId(channelId);
};

const saveChannel = async (channel: Channel) => {
    validate([channel.chid, channel.cid, channel.name]);

    await ChannelRepository.saveChannel(channel);
};

const deleteChannel = async (channelId: number) => {
    validate([channelId]);

    await ChannelRepository.deleteChannel(channelId);
    await PostRepository.deletePostsByChannelId(channelId);
};

const updateChannelName = async (channelId: number, channelName: string) => {
    validate([channelId, channelName]);

    await ChannelRepository.updateChannelName(channelId, channelName);
};

export const ChannelService = {
    getAllChannels,
    getChannelsByClubId,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
