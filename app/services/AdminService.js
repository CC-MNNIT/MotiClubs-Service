const userRepository = require("../repository/UserRepository");
const fcmRepository = require("../repository/FcmRepository");
const subscribersRepository = require("../repository/SubscribersRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
const clubRepository = require("../repository/ClubRepository");
const postRepository = require("../repository/PostRepository");
const urlRepository = require("../repository/UrlRepository");
const validate = require("../utility/validate");

const getClubs = async () => {
    const clubs = await clubModel.find({});
    return clubs;
};

const saveClub = async (clubDetails) => {
    // Create club object from club details (name, description)
    const club = new clubModel(clubDetails);

    // Save club
    await club.save();

    // Create subscription doc for new Club
    const subscription = new subscriptionModel({
        club: club._id,
        subscribers: [],
    });

    // Save subscription doc
    await subscription.save();

    return club;
};

const deleteClub = async (id) => {
    validate([id]);

    // Find club
    const club = await clubModel.findById(id).exec();

    // Club not found
    if (!club) throw new Error("Club does not exist");

    // Delete club
    await club.remove();
};

const updateAdmin = async (email, club, add) => {
    validate([email, club]);

    // Check if user exists
    const user = await userModel.findOne({ email: email }).exec();
    if (!user) throw new Error("User does not exist");

    // Check if club exists
    const clubObject = await clubModel.findOne({ _id: club }).exec();
    if (!clubObject) throw new Error("Club does not exist");

    // Update admins array in club with _id=club
    if (add)
        await clubModel.updateOne(
            { _id: club },
            { $addToSet: { admins: email } }
        );
    else await clubModel.updateOne({ _id: club }, { $pull: { admins: email } });
};

module.exports = {
    getClubs,
    saveClub,
    deleteClub,
    updateAdmin,
};
