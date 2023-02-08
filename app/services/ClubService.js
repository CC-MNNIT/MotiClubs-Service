const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getClubs = async () => {
    const clubs = await clubRepository.getAllClubs();
    return clubs;
};

const updateClub = async (clubId, data) => {
    validate([clubId, data.description, data.avatar, data.summary]);

    // Name and admin list update not allowed
    if (data.name) throw new Error("Cannot change name of club");

    await clubRepository.updateClubByCid(
        clubId,
        data.description,
        data.avatar,
        data.summary
    );
};

const subscriberCount = async (clubId) => {
    validate([clubId]);

    const count = await subscribersRepository.getSubscribersCountByCid(clubId);
    return count;
};

module.exports = {
    getClubs,
    updateClub,
    subscriberCount,
};
