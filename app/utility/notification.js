const admin = require("../config/firebase");
const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");

// Utility function to notify subscribers for new post
async function notify(club, post) {
    try {
        const subscription = await subscriptionModel.findOne({ club: club });
        const subscribers = subscription.toJSON().subscribers;
        for (const subscriber of subscribers) {
            try {
                await sendNotification(subscriber, post);
            } catch (error) {
                console.log(error);
            }
        }
    } catch (error) {
        console.log(error);
    }
}

async function sendNotification(subscriber, post) {
    try {
        const token = await fcmTokenModel.findOne({ user: subscriber });
        const payload = {
            data: {
                ...post,
                _id: post._id.toString(),
                time: post.time.toString(),
            },
        };
        await admin.messaging().send({
            data: {
                ...post,
                _id: post._id.toString(),
                time: post.time.toString(),
            },
            token: token.toJSON().token,
        });
    } catch (error) {
        console.log(error);
    }
}

module.exports = notify;
