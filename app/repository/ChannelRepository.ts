import { OkPacket, RowDataPacket } from "mysql2";
import { db } from "../db/db";
import { Channel } from "../models/Channel";

const getAllChannels = async (): Promise<Channel[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM channel",
        [],
        (error, result) => {
            const channels: Channel[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => channels.push({ chid: row.chid, cid: row.cid, name: row.name }));
            resolve(channels);
        }
    );
});

const getChannelsByClubId = async (clubId: number): Promise<Channel[]> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM channel WHERE cid=?",
        [clubId],
        (error, result) => {
            const channels: Channel[] = [];
            if (error) {
                reject(error);
                return;
            }

            (<RowDataPacket[]>result).forEach(row => channels.push({ chid: row.chid, cid: row.cid, name: row.name }));
            resolve(channels);
        }
    );
});

const getChannelByChannelId = async (channelId: number): Promise<Channel> => new Promise((resolve, reject) => {
    db.query(
        "SELECT * FROM channel WHERE chid=?",
        [channelId],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }

            const row = (<RowDataPacket[]>result)[0];
            resolve({ chid: row.chid, cid: row.cid, name: row.name });
        }
    );
});

const saveChannel = async (channel: Channel): Promise<number> => new Promise((resolve, reject) => {
    db.query(
        "INSERT INTO channel (chid, cid, name) VALUES(?,?,?)",
        [channel.chid, channel.cid, channel.name],
        (error, result) => {
            if (error) {
                reject(error);
                return;
            }
            resolve((<OkPacket>result).insertId);
        }
    );
});

const deleteChannel = async (channelId: number): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "DELETE FROM channel WHERE chid=?",
        [channelId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

const updateChannelName = async (channelId: number, channelName: string): Promise<void> => new Promise((resolve, reject) => {
    db.query(
        "UPDATE channel SET name=? WHERE chid=?",
        [channelName, channelId],
        (error, _) => {
            if (error) {
                reject(error);
                return;
            }
            resolve();
        }
    );
});

export const ChannelRepository = {
    getAllChannels,
    getChannelsByClubId,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
