import { Request, Response } from "express";
import { Channel } from "../models/Channel";
import { ChannelService } from "../services/ChannelService";

const getChannels = async (req: Request, res: Response) => {
    try {
        const clubId = Number(req.query.clubId);
        const channels = isNaN(clubId) ? await ChannelService.getAllChannels() : await ChannelService.getChannelsByClubId(clubId);
        res.status(200).send(channels);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const getChannelByChannelId = async (req: Request, res: Response) => {
    try {
        const channel = await ChannelService.getChannelByChannelId(Number(req.params.channelId));
        res.status(200).send(channel);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveChannel = async (req: Request, res: Response) => {
    try {
        const { clubId, channelId, name } = req.body;
        const channel: Channel = {
            cid: Number(clubId),
            chid: Number(channelId),
            name: String(name),
        }

        await ChannelService.saveChannel(channel);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteChannel = async (req: Request, res: Response) => {
    try {
        await ChannelService.deleteChannel(Number(req.params.channelId));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updateChannelName = async (req: Request, res: Response) => {
    try {
        const { name } = req.body;
        await ChannelService.updateChannelName(Number(req.params.channelId), String(name));
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const ChannelController = {
    getChannels,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
