const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");

const getClubs = async () => {
    const clubs = await clubModel.find({});
    return clubs;
};

const updateClub = async (clubId, data) => {
    // Name and admin list update not allowed
    if (data.name) throw new Error("Unauthorized request");
    if (data.admins) throw new Error("Unauthorized request");

    await clubModel.updateOne({ _id: clubId }, data);
};

const subscriberCount = async (clubId) => {
    const subscriptions = await SubscriptionModel.findOne({ club: clubId });
    subscriptionsObj = subscriptions.toJSON();
    return subscriptionsObj.subscribers.length;
};

module.exports = {
    getClubs,
    updateClub,
    subscriberCount,
};
