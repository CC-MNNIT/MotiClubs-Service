import { Club } from "../models/Club";
import { AdminRepository } from "../repository/AdminRepository";
import { ClubRepository } from "../repository/ClubRepository";
import { UserRepository } from "../repository/UserRepository";
import validate from "../utility/validate";

const getClubs = async () => {
    const mappedClubs = (await ClubRepository.getAllClubs()).map(async (club) => {
        const admins = await AdminRepository.getAdminsFromClubId(club.cid);
        return { ...club, admins: admins };
    });
    return (await Promise.all(mappedClubs));
};

const saveClub = async (club: Club) => {
    validate([club.name, club.description, club.avatar, club.summary]);

    const clubId = await ClubRepository.saveClub(club);
    return clubId;
};

const deleteClub = async (clubId: number) => {
    validate([clubId]);

    await ClubRepository.deleteClubByCid(clubId);
};

const addAdmin = async (email: string, clubId: number) => {
    validate([email, clubId]);

    // Get user by email
    const user = await UserRepository.getUserByEmail(email.toLowerCase());

    // Check if club exists
    const club = await ClubRepository.clubExists(clubId);
    if (!club) throw new Error("Club does not exist");

    // Update admin table
    await AdminRepository.addAdmin({ cid: clubId, uid: user.uid });
};

const removeAdmin = async (userId: number, clubId: number) => {
    validate([userId, clubId]);

    // Check if user exists
    const user = await UserRepository.userExists(userId);
    if (!user) throw new Error("User does not exist");

    // Check if club exists
    const club = await ClubRepository.clubExists(clubId);
    if (!club) throw new Error("Club does not exist");

    // Update admin table
    await AdminRepository.removeAdmin({ cid: clubId, uid: userId });
};

export const SuperAdminService = {
    getClubs,
    saveClub,
    deleteClub,
    addAdmin,
    removeAdmin,
};
