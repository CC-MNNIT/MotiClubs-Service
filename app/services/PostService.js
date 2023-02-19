const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getPosts = async (channelId) => {
    validate([channelId]);

    const posts = await postRepository.getPostsByClubAndChannel(channelId);
    return posts;
};

const savePost = async (post) => {
    validate([post.pid, post.chid, post.message, post.time, post.uid, post.general]);

    await postRepository.savePost(post);
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
