const clubModel = require("../models/ClubModel");
const SubscriptionModel = require("../models/SubscriptionModel");

const getClubs = async () => {
  const clubs = await clubModel.find({});
  return clubs;
};

const updateClub = async (clubId, data) => {
  await clubModel.updateOne({ _id: clubId }, data);
};

const subscriberCount = async (clubId) => {
  const subscriptions = await SubscriptionModel.findOne({ club: clubId });
  subscriptionsObj = subscriptions.toJSON();
  return subscriptionsObj.subscribers.length;
};

const updateSocialLink = async (clubId, data) => {
  if (data.remove)
    await clubModel.updateOne(
      { _id: clubId },
      { $pull: { socialUrls: data.link } }
    );
  else
    await clubModel.updateOne(
      { _id: clubId },
      { $addToSet: { socialUrls: data.link } }
    );
};

module.exports = {
  getClubs,
  updateClub,
  subscriberCount,
  updateSocialLink,
};
