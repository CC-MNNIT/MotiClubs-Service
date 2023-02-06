const userModel = require("../models/UserModel");
const clubModel = require("../models/ClubModel");
const subscriptionModel = require("../models/SubscriptionModel");
const fcmTokenModel = require("../models/FcmTokenModel");
const validate = require("../utility/validate");

const getUser = async (email) => {
    validate([email]);

    const user = await userModel.findOne({ email: email }).exec();

    if (!user) throw new Error("User does not exist");

    // Get list of clubs user is admin of
    const adminArray = await getAdminList(email);

    // Append admin array to user model
    const userWithAdminList = { ...user.toJSON(), admin: adminArray };

    // Remove unwanted property
    delete userWithAdminList._id;

    return userWithAdminList;
};

const getUserByEmail = async (email) => {
    validate([email]);

    // Get user from email
    const user = await userModel.findOne({ email: email }).exec();

    if (!user) throw new Error("User does not exist");

    const userJson = user.toJSON();

    // Return restricted content
    return {
        name: userJson.name,
        personalEmail: userJson.personalEmail,
        phoneNumber: userJson.phoneNumber,
        avatar: userJson.avatar,
    };
};

const saveUser = async (userDetails) => {
    // Get user object from user details
    // {name, email, personalEmail, registrationNumber, phoneNumber, graduationYear, course, avatar}
    const user = new userModel(userDetails);

    await user.save();
};

const updateAvatar = async (email, avatar) => {
    validate([email, avatar]);

    // Get user from email
    const user = await userModel.findOne({ email: email }).exec();

    if (!user) throw new Error("User does not exist");

    // Update {avatar: "avatar_url"}
    await user.updateOne({ avatar: avatar });
};

const updateFcmToken = async (email, token) => {
    validate([email, token]);

    // Find fcmToken document
    const fcmToken = await fcmTokenModel.findOne({ user: email }).exec();

    // Add new doc if not already added else update existing
    if (fcmToken === null) {
        const newToken = new fcmTokenModel({ token: token, user: email });
        await newToken.save();
    } else await fcmToken.updateOne({ token: token });
};

const subscribe = async (email, club) => {
    validate([email, club]);

    // Find user
    const user = await userModel.findOne({ email: email }).exec();

    // Find club
    const clubObj = await clubModel.findOne({ _id: club }).exec();

    if (!user) throw new Error("User does not exist");
    if (!clubObj) throw new Error("Club does not exist");

    // Add club id to subscribed array
    await user.updateOne({ $addToSet: { subscribed: club } });

    // Get subscription doc for req.body.club
    const subscription = await subscriptionModel.findOne({
        club: club,
    });

    // Add subscription doc if does not exist
    if (!subscription) {
        const newSubscriptionDoc = new subscriptionModel({
            club: club,
            subscribers: [email],
        });
        await newSubscriptionDoc.save();
        return;
    }

    // Add user email to subscription list
    await subscription.updateOne({ $addToSet: { subscribers: email } });
};

const unsubscribe = async (email, club) => {
    validate([email, club]);

    // Find user
    const user = await userModel.findOne({ email: email }).exec();

    // Find club
    const clubObj = await clubModel.findOne({ _id: club }).exec();

    if (!user) throw new Error("User does not exist");
    if (!clubObj) throw new Error("Club does not exist");

    // Add club id to subscribed array
    await user.updateOne({ $pull: { subscribed: club } });

    // Get subscription doc for req.body.club
    const subscription = await subscriptionModel.findOne({
        club: club,
    });

    // Add subscription doc if does not exist
    if (!subscription) {
        const newSubscriptionDoc = new subscriptionModel({
            club: club,
            subscribers: [],
        });
        await newSubscriptionDoc.save();
        return;
    }

    // Add user email to subscription list
    await subscription.updateOne({ $pull: { subscribers: email } });
};

// Utility function to get list of clubs a user is admin of
async function getAdminList(email) {
    const arr = [];
    const clubs = await clubModel.find({}).exec();
    for (let i = 0; i < clubs.length; ++i) {
        const admins = clubs[i].toJSON()["admins"];
        for (let j = 0; j < admins.length; ++j) {
            if (admins[j] === email) {
                arr.push(clubs[i].toJSON()["_id"]);
                break;
            }
        }
    }
    return arr;
}

module.exports = {
    getUser,
    getUserByEmail,
    saveUser,
    updateAvatar,
    updateFcmToken,
    subscribe,
    unsubscribe,
};
