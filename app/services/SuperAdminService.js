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
    for (let i = 0; i < clubs.length; ++i) {
        const admins = await adminRepository.getAdminsFromClubId(clubs[i].cid);
        clubs[i].admins = admins;
    }
    return clubs;
};

const saveClub = async (clubDetails) => {
    validate([
        clubDetails.name,
        clubDetails.description,
        clubDetails.avatar,
        clubDetails.summary,
    ]);

    const clubId = await clubRepository.saveClub(
        clubDetails.name,
        clubDetails.description,
        clubDetails.avatar,
        clubDetails.summary
    );
    return clubId;
};

const deleteClub = async (clubId) => {
    validate([clubId]);

    await clubRepository.deleteClubByCid(clubId);
};

const addAdmin = async (email, clubId) => {
    validate([email, clubId]);

    // Get user by email
    // Check if user exists
    const user = await userRepository.getUserByEmail(email.toLowerCase());
    if (user.size() < 1) throw new Error("User does not exist");

    // Check if club exists
    const club = await clubRepository.clubExists(clubId);
    if (!club) throw new Error("Club does not exist");

    // Update admin table
    await adminRepository.addAdmin(clubId, user[0].uid);
};

const removeAdmin = async (userId, clubId) => {
    validate([userId, clubId]);

    // Check if user exists
    const user = await userRepository.userExists(userId);
    if (!user) throw new Error("User does not exist");

    // Check if club exists
    const club = await clubRepository.clubExists(clubId);
    if (!club) throw new Error("Club does not exist");

    // Update admin table
    await adminRepository.removeAdmin(clubId, userId);
};

module.exports = {
    getClubs,
    saveClub,
    deleteClub,
    addAdmin,
    removeAdmin,
};
