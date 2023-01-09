const admin = require("./config");
const subscriptionModel = require("../models/SubscriptionModel");
const fcmTokenModel = require("../models/FcmTokenModel");
require("dotenv").config();

const options = {
  priority: "high",
  timeToLive: 60 * 60 * 24,
};

// Utility function to notify subscribers for new post
async function notify(club, post) {
  try {
    const subscription = await subscriptionModel.findOne({ club: club });
    const subscribers = subscription.toJSON().subscribers;
    for (const subscriber of subscribers) {
      await sendNotification(subscriber, post);
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
      },
    };
    await admin
      .messaging()
      .sendToDevice(token.toJSON().token, payload, options);
  } catch (error) {
    console.log(error);
  }
}

module.exports = notify;
