const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getPosts = async (clubId, channelId) => {
    validate([clubId, channelId]);

    const posts = await postRepository.getPostsByClubAndChannel(
        clubId,
        channelId
    );
    return posts;
};

const savePost = async (userId, clubId, channelId, message, general) => {
    validate([userId, clubId, channelId, message, general]);

    const postId = postRepository.savePost(
        userId,
        clubId,
        channelId,
        message,
        general
    );
    return postId;
};

const deletePost = async (postId) => {
    validate([postId]);

    await postRepository.detelePostByPostId(postId);
};

const updatePost = async (postId, message) => {
    validate([postId]);

    await postRepository.updatePostByPostId(postId, message);
};

module.exports = {
    getPosts,
    savePost,
    deletePost,
    updatePost,
};
