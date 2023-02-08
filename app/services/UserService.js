const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");
const admin = require("../config/firebase");

const getUser = async (userId) => {
    validate([userId]);

    const user = await userRepository.getUserByUid(userId);

    if (!user) throw new Error("User does not exist");

    // Get list of clubs user is admin of
    const adminArray = await adminRepository.getClubsWithUidAsAdmin(userId);

    // Append admin array to user model
    const userWithAdminList = { ...user, admin: adminArray };

    return userWithAdminList;
};

const getUserByUid = async (userId) => {
    validate([userId]);

    // Get user from userId
    const user = await userRepository.getUserByUid(userId);

    if (!user) throw new Error("User does not exist");

    // Return restricted content
    return {
        name: user.name,
        email: user.email,
        phone: user.phone,
        avatar: user.avatar,
    };
};

const saveUser = async (userDetails) => {
    // userDetails = {name, email, phone, course, avatar}
    const userId = await userRepository.saveUser(userDetails);

    // Default set up
    await fcmRepository.setTokenByUid(userId, "");

    // Add userId to custom claims [Firebase]
    await saveUserIdInCustomUserClaims(userId);
};

const updateAvatar = async (userId, avatar) => {
    validate([userId, avatar]);

    await userRepository.updateAvatarByUid(userId, avatar);
};

const updateFcmToken = async (userId, token) => {
    validate([userId, token]);

    await fcmRepository.updateTokenByUid(userId, token);
};

const subscribe = async (userId, clubId) => {
    validate([userId, clubId]);

    await subscribersRepository.subscribe(userId, clubId);
};

const unsubscribe = async (userId, clubId) => {
    validate([userId, clubId]);

    await subscribersRepository.unsubscribe(userId, clubId);
};

// Add userId to custom claims [Firebase]
const saveUserIdInCustomUserClaims = async (userId) => {
    const user = await userRepository.getUserByUid(userId);
    const firebaseUser = await admin.auth().getUserByEmail(user.email);
    await admin
        .auth()
        .setCustomUserClaims(firebaseUser.uid, { userId: userId });
};

module.exports = {
    getUser,
    getUserByUid,
    saveUser,
    updateAvatar,
    updateFcmToken,
    subscribe,
    unsubscribe,
};
