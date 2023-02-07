const pool = require("../db/db");

const subscribe = async (userId, clubId) => {
    const response = await pool.execute(
        "INSERT INTO subscribers (uid, cid) VALUES (?,?)",
        [userId, clubId]
    );
    return response;
};

const unsubscribe = async (userId, clubId) => {
    const response = await pool.execute(
        "DELETE FROM subscribers where uid=? AND cid=?",
        [userId, clubId]
    );
    return response;
};

const getSubscribersCountByCid = async (clubId) => {
    const [response] = await pool.execute(
        "SELECT COUNT(uid) as subscribersCount FROM subscribers WHERE cid=?",
        [clubId]
    );
    return response[0].subscribersCount;
};

module.exports = {
    subscribe,
    unsubscribe,
    getSubscribersCountByCid,
};
