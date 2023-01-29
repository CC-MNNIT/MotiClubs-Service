const clubModel = require("../models/ClubModel");
const SubscriptionModel = require("../models/SubscriptionModel");

const getClubs = async () => {
  const clubs = await clubModel.find({});
  return clubs;
};

const updateAvatar = async (clubId, avatar) => {
  await clubModel.updateOne({ _id: clubId }, { avatar: avatar });
};

const subscriberCount = async (clubId) => {
  const subscriptions = await SubscriptionModel.findOne({ club: clubId });
  subscriptionsObj = subscriptions.toJSON();
  return subscriptionsObj.subscribers.length;
};

module.exports = {
  getClubs,
  updateAvatar,
  subscriberCount,
};
