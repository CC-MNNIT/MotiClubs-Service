import { Request, Response } from "express";
import { Payload, Type } from "../models/Payload";
import { ChannelRepository } from "../repository/ChannelRepository";
import { ClubRepository } from "../repository/ClubRepository";
import { PostRepository } from "../repository/PostRepository";
import { ReplyRepository } from "../repository/ReplyRepository";
import { UserRepository } from "../repository/UserRepository";
import { ReplyService } from "../services/ReplyService";
import { Notification } from "../utility/notification";

const getReplies = async (req: Request, res: Response) => {
    try {
        const replies = await ReplyService.getReplies(Number(req.query.postId));
        res.status(200).send(replies);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveReply = async (req: Request, res: Response) => {
    try {
        const { pid, uid, to_uid, message, time } = req.body;
        await ReplyService.saveReply({
            pid: Number(pid),
            uid: Number(uid),
            to_uid: Number(to_uid),
            message: String(message),
            time: Number(time),
        });

        // Send response to user
        res.status(200).send({});

        await notifyUsers(Number(pid), Number(time));
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteReply = async (req: Request, res: Response) => {
    try {
        const reply = await ReplyRepository.getReplyByTime(Number(req.query.replyId));
        const post = await PostRepository.getPostByPostId(reply.pid);

        await ReplyService.deleteReply(reply.time);

        res.status(200).send({});

        await sendDeleteReplyPushNotification(reply.pid, post.chid, reply.time);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const sendDeleteReplyPushNotification = async (postId: number, channelId: number, replyId: number) => {
    await Notification.notifyAll({
        type: Type.DELETE_REPLY,
        pid: postId.toString(),
        chid: channelId.toString(),
        time: replyId.toString(),
    });
};

// Notify users about new reply
const notifyUsers = async (postId: number, replyId: number) => {

    const reply = await ReplyRepository.getReplyByTime(replyId);

    // Get admin details
    const user = await UserRepository.getUserByUid(reply.uid);

    // Get inserted post details
    const post = await PostRepository.getPostByPostId(postId);

    // Get channel details
    const channel = await ChannelRepository.getChannelByChannelId(post.chid);

    // Get club details
    const club = await ClubRepository.getClubByClubId(channel.cid);

    const payload: Payload = {
        type: Type.REPLY,
        pid: reply.pid.toString(),
        uid: reply.uid.toString(),
        to_uid: reply.to_uid.toString(),
        message: reply.message,
        time: reply.time.toString(),
        userName: user.name,
        userAvatar: user.avatar,
        clubName: club.name,
        channelName: channel.name,
        cid: club.cid.toString(),
        chid: channel.chid.toString()
    }

    await Notification.notifyPostParticipants(postId, payload);
};

export const ReplyController = {
    getReplies,
    saveReply,
    deleteReply,
};
