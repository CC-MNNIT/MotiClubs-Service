const mongoose = require("mongoose");

const SubscriptionSchema = new mongoose.Schema(
  {
    club: {
      type: String,
      required: true,
      trim: true,
    },
    subscribers: {
      type: Array,
      default: [],
    },
  },
  { versionKey: false }
);

const Subscription = mongoose.model(
  "Subscription",
  SubscriptionSchema,
  "Subscriptions"
);

module.exports = Subscription;
