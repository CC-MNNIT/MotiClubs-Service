import { Club } from "../models/Club";
import { AdminRepository } from "../repository/AdminRepository";
import { ChannelRepository } from "../repository/ChannelRepository";
import { ClubRepository } from "../repository/ClubRepository";
import { SubscribersRepository } from "../repository/SubscribersRepository";
import validate from "../utility/validate";

const getClubs = async () => {
    const mappedClubs = (await ClubRepository.getAllClubs()).map(async (club) => {
        const admins = await AdminRepository.getAdminsFromClubId(club.cid);
        const channels = await ChannelRepository.getChannelsByClubId(club.cid);
        return { ...club, admins: admins, channels: channels };
    });
    return (await Promise.all(mappedClubs));
};

const updateClub = async (clubId: number, description: string, avatar: string, summary: string) => {
    validate([clubId, description, avatar, summary]);

    await ClubRepository.updateClubByCid(clubId, description, avatar, summary);
};

const subscribers = async (clubId: number) => {
    validate([clubId]);
    return (await SubscribersRepository.getSubscribersByCid(clubId));
};

export const ClubService = {
    getClubs,
    updateClub,
    subscribers,
};
