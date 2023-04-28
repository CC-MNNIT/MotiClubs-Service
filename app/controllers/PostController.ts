import { Request, Response } from "express";
import { Post } from "../models/Post";
import { ChannelRepository } from "../repository/ChannelRepository";
import { ClubRepository } from "../repository/ClubRepository";
import { PostRepository } from "../repository/PostRepository";
import { UserRepository } from "../repository/UserRepository";
import { PostService } from "../services/PostService";
import { Notification } from "../utility/notification";

const getPosts = async (req: Request, res: Response) => {
    try {
        const posts = await PostService.getPosts(
            Number(req.query.channelId),
            Number(req.query.page ? req.query.page : '1'),
            Number(req.query.items ? req.query.items : '10')
        );
        res.status(200).send(posts);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const savePost = async (req: Request, res: Response) => {
    try {
        const { pid, chid, message, time, uid, general } = req.body;
        const post: Post = {
            pid: Number(pid),
            uid: Number(uid),
            chid: Number(chid),
            message: String(message),
            time: Number(time),
            general: Number(general),
        }

        const postId = post.pid;
        await PostService.savePost(post);

        // Send response to user
        res.status(200).send({});

        await notifyUsers(postId, 0);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deletePost = async (req: Request, res: Response) => {
    try {
        await PostService.deletePost(Number(req.params.postId));

        res.status(200).send({});

        await sendDeletePushNotification(
            Number(req.params.postId),
            Number(req.query.channelId)
        );
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const sendDeletePushNotification = async (postId: number, channelId: number) => {
    await Notification.notifyAll({
        deleted: "1",
        pid: postId.toString(),
        chid: channelId.toString(),
    });
};

const updatePost = async (req: Request, res: Response) => {
    try {
        const postId = Number(req.params.postId);
        await PostService.updatePost(postId, String(req.body.message));

        // Send response to user
        res.status(200).send({});

        await notifyUsers(postId, 1);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

// Notify users about new post / post update
const notifyUsers = async (postId: number, updated: number) => {
    // Get inserted post details
    const post = await PostRepository.getPostByPostId(postId);

    // Get admin details
    const user = await UserRepository.getUserByUid(post.uid);

    // Get channel details
    const channel = await ChannelRepository.getChannelByChannelId(post.chid);

    // Get club details
    const club = await ClubRepository.getClubByClubId(channel.cid);

    // Notify subscribers for new post
    if (post.general) {
        await Notification.notifyAll({
            ...post,
            time: post.time.toString(),
            uid: post.uid.toString(),
            pid: post.pid.toString(),
            cid: channel.cid.toString(),
            chid: post.chid.toString(),
            general: post.general.toString(),
            adminName: user.name,
            adminAvatar: user.avatar,
            clubName: club.name,
            channelName: channel.name,
            updated: updated.toString(),
        });
    } else {
        await Notification.notifyUsers(club.cid, {
            ...post,
            time: post.time.toString(),
            uid: post.uid.toString(),
            pid: post.pid.toString(),
            cid: channel.cid.toString(),
            chid: post.chid.toString(),
            general: post.general.toString(),
            adminName: user.name,
            adminAvatar: user.avatar,
            clubName: club.name,
            channelName: channel.name,
            updated: updated.toString(),
        });
    }
};

export const PostController = {
    getPosts,
    savePost,
    deletePost,
    updatePost,
};
