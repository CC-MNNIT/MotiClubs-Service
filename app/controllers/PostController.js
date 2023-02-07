const { notify, notifyAll } = require("../utility/notification");
const service = require("../services/PostService");
const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");

const getPosts = async (req, res) => {
    try {
        const posts = await service.getPosts(
            req.query.clubId,
            req.query.channelId
        );
        res.status(200).send(posts);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const savePost = async (req, res) => {
    try {
        const message = req.body.message;
        const userId = req.userId;
        const clubId = req.body.clubId;
        const channelId = req.body.channelId;
        const general = req.body.general;

        const postId = await service.savePost(
            userId,
            clubId,
            channelId,
            message,
            general
        );

        // Send response to user
        res.status(200).send({});

        notifyUsers(postId, 0);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deletePost = async (req, res) => {
    try {
        await service.deletePost(req.params.postId);
        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const updatePost = async (req, res) => {
    try {
        await service.updatePost(req.params.postId, req.body.message);

        // Send response to user
        res.status(200).send({});

        notifyUsers(req.params.postId, 1);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

// Notify users about new post / post update
const notifyUsers = async (postId, updated) => {
    // Get inserted post details
    const post = await postRepository.getPostByPostId(postId);

    // Get admin details
    const user = await userRepository.getUserByUid(post[0].uid);

    // Get club details
    const club = await clubRepository.getClubByClubId(post[0].cid);

    // Get channel details
    const channel = await channelRepository.getChannelByChannelId(post[0].chid);

    // Notify subscribers for new post
    if (post[0].general) {
        await notifyAll({
            ...post[0],
            adminName: user[0].name,
            adminAvatar: user[0].avatar,
            clubName: club[0].name,
            channelName: channel[0].name,
            updated: updated.toString(),
        });
    } else {
        await notify(club[0].cid, {
            ...post[0],
            adminName: user[0].name,
            adminAvatar: user[0].avatar,
            clubName: club[0].name,
            channelName: channel[0].name,
            updated: updated.toString(),
        });
    }
};

module.exports = {
    getPosts,
    savePost,
    deletePost,
    updatePost,
};
