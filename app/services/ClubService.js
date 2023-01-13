const model = require("../models/ClubModel");

const getClubs = async () => {
  const clubs = await model.find({});
  return clubs;
};

module.exports = {
  getClubs,
};
