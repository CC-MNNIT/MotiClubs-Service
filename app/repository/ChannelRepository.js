const pool = require("../db/db");

const getAllChannels = async () => {
    const [response] = await pool.execute("SELECT * FROM channel", []);
    return response;
};

const getChannelByClubId = async (clubId) => {
    const [response] = await pool.execute("SELECT * FROM channel WHERE cid=?", [
        clubId,
    ]);
    return response;
};

const getChannelByChannelId = async (channelId) => {
    const [response] = await pool.execute(
        "SELECT * FROM channel WHERE chid=?",
        [channelId]
    );
    return response;
};

const saveChannel = async (clubId, channelName) => {
    const [response] = await pool.execute(
        "INSERT INTO channel (cid, name) VALUES(?,?)",
        [clubId, channelName]
    );
    return response.insertId;
};

const deleteChannel = async (channelId) => {
    await pool.execute("DELETE FROM channel WHERE chid=?", [channelId]);
};

const updateChannelName = async (channelId, channelName) => {
    await pool.execute("UPDATE channel SET name=? WHERE chid=?", [
        channelName,
        channelId,
    ]);
};

module.exports = {
    getAllChannels,
    getChannelByClubId,
    getChannelByChannelId,
    saveChannel,
    deleteChannel,
    updateChannelName,
};
