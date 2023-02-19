const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getClubs = async () => {
    const clubs = await clubRepository.getAllClubs();
    for (let i = 0; i < clubs.length; ++i) {
        const admins = await adminRepository.getAdminsFromClubId(clubs[i].cid);
        clubs[i].admins = admins;

        const channels = await channelRepository.getChannelsByClubId(clubs[i].cid);
        clubs[i].channels = channels;
    }
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

const subscribers = async (clubId) => {
    validate([clubId]);

    const subscribers = await subscribersRepository.getSubscribersByCid(clubId);
    return subscribers;
};

module.exports = {
    getClubs,
    updateClub,
    subscribers,
};
