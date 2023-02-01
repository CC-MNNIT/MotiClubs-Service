const clubModel = require("../models/ClubModel");
const SubscriptionModel = require("../models/SubscriptionModel");

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
