import admin from "../config/firebase";
import { User } from "../models/User";
import { AdminRepository } from "../repository/AdminRepository";
import { FcmRepository } from "../repository/FcmRepository";
import { SubscribersRepository } from "../repository/SubscribersRepository";
import { UserRepository } from "../repository/UserRepository";
import validate from "../utility/validate";

const getUser = async (userId: number) => {
    validate([userId]);

    const user = await UserRepository.getUserByUid(userId);
    if (!user) throw new Error("User does not exist");

    // Get list of clubs user is admin of
    const adminArray = await AdminRepository.getClubsWithUidAsAdmin(userId);

    // Get list of clubs user subscribed to
    const subscribedArray = await SubscribersRepository.getSubscribedClubsByUid(userId);

    // Append admin array to user model
    const userWithAdminList = {
        ...user,
        admin: adminArray,
        subscribed: subscribedArray,
    };

    return userWithAdminList;
};

const getAdmins = async () => await UserRepository.getAdmins();

const getUserByUid = async (userId: number) => {
    validate([userId]);

    // Get user from userId
    const user = await UserRepository.getUserByUid(userId);
    if (!user) throw new Error("User does not exist");

    // Return restricted content
    return user;
};

const saveUser = async (userDetails: User) => {
    validate([
        userDetails.regno,
        userDetails.name,
        userDetails.email,
        userDetails.course,
        userDetails.phone,
    ]);

    // userDetails = {name, email, phone, course, avatar, regno}
    const userId = await UserRepository.saveUser(userDetails);

    // Default set up
    await FcmRepository.setTokenByUid({ uid: userId, token: "" });

    // Add userId to custom claims [Firebase]
    await saveUserIdInCustomUserClaims(userId);
};

const updateAvatar = async (userId: number, avatar: string) => {
    validate([userId, avatar]);

    await UserRepository.updateAvatarByUid(userId, avatar);
};

const updateFcmToken = async (userId: number, token: string) => {
    validate([userId, token]);

    await FcmRepository.updateTokenByUid({ uid: userId, token });
};

const subscribe = async (userId: number, clubId: number) => {
    validate([userId, clubId]);

    await SubscribersRepository.subscribe({ uid: userId, cid: clubId });
};

const unsubscribe = async (userId: number, clubId: number) => {
    validate([userId, clubId]);

    await SubscribersRepository.unsubscribe({ uid: userId, cid: clubId });
};

// Add userId to custom claims [Firebase]
const saveUserIdInCustomUserClaims = async (userId: number) => {
    const user = await UserRepository.getUserByUid(userId);
    const firebaseUser = await admin.auth().getUserByEmail(user.email);
    const claims = firebaseUser?.customClaims ? firebaseUser.customClaims : {};
    await admin
        .auth()
        .setCustomUserClaims(firebaseUser.uid, { ...claims, userId: userId });
};

export const UserService = {
    getUser,
    getUserByUid,
    getAdmins,
    saveUser,
    updateAvatar,
    updateFcmToken,
    subscribe,
    unsubscribe,
};
