import admin from "../config/firebase";
import { FcmRepository } from "../repository/FcmRepository";

interface Payload {
    [key: string]: string;
}

// Utility function to notify subscribers for new post
const notifyUsers = async (clubId: number, payload: Payload) => {
    try {
        const subscribers = await FcmRepository.getTokensOfSubscribers(clubId);
        for (const subscriber of subscribers) {
            try {
                if (subscriber.token) await sendNotification(subscriber.token, payload);
            } catch (error) {
                console.log(error);
            }
        }
    } catch (error) {
        // console.log(error);
    }
};

const notifyAll = async (payload: Payload) => {
    const users = await FcmRepository.getAllTokens();
    for (let i = 0; i < users.length; ++i) {
        await sendNotification(users[i].token, payload);
    }
};

const sendNotification = async (token: string, payload: Payload) => {
    try {
        await admin.messaging().send({
            data: { ...payload },
            token: token,
        });
    } catch (error) {
        // console.log(error);
    }
};

export const Notification = { notifyUsers, notifyAll };
