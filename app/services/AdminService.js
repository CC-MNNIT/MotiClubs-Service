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
    const clubs = await clubRepository.getAllClubs();
    return clubs;
};

const saveClub = async (clubDetails) => {
    await clubRepository.saveClub(
        clubDetails.name,
        clubDetails.description,
        clubDetails.avatar,
        clubDetails.summary
    );
};

const deleteClub = async (clubId) => {
    validate([id]);

    await clubRepository.deleteClubByCid(clubId);
};

const updateAdmin = async (userId, clubId, add) => {
    validate([email, club]);

    // Check if user exists
    const user = await userRepository.userExists(userId);
    if (!user) throw new Error("User does not exist");

    // Check if club exists
    const club = await clubRepository.clubExists(clubId);
    if (!club) throw new Error("Club does not exist");

    // Update admin table
    if (add) await adminRepository.addAdmin(clubId, userId);
    else await adminRepository.removeAdmin(clubId, userId);
};

module.exports = {
    getClubs,
    saveClub,
    deleteClub,
    updateAdmin,
};
