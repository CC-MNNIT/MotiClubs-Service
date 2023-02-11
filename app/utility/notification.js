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
async function notify(clubId, post) {
    try {
        const subscribers = await fcmRepository.getTokensOfSubscribers(clubId);
        for (const subscriber of subscribers) {
            try {
                if (subscriber.token)
                    await sendNotification(subscriber.token, post);
            } catch (error) {
                console.log(error);
            }
        }
    } catch (error) {
        console.log(error);
    }
}

async function notifyAll(post) {
    const users = await fcmRepository.getAllTokens();
    for (let i = 0; i < users.length; ++i) {
        await sendNotification(users[i].token, post);
    }
}

async function sendNotification(token, post) {
    try {
        await admin.messaging().send({
            data: {
                ...post,
                time: post.time.toString(),
                uid: post.uid.toString(),
                pid: post.pid.toString(),
                cid: post.cid.toString(),
                chid: post.chid.toString(),
                general: post.general.toString(),
            },
            token,
        });
    } catch (error) {
        console.log(error);
    }
}

module.exports = { notify, notifyAll };
