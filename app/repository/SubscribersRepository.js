const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});

const subscribe = async (userId, clubId) => {
    const response = await con.execute(
        "INSERT INTO subscribers (uid, cid) VALUES (?,?)",
        [userId, clubId]
    );
    return response;
};

const unsubscribe = async (userId, clubId) => {
    const response = await con.execute(
        "DELETE FROM subscribers where uid=? AND cid=?",
        [userId, clubId]
    );
    return response;
};

const getSubscribersCountByCid = async (clubId) => {
    const response = await con.execute(
        "SELECT COUNT(uid) as subscribersCount FROM club WHERE cid=?",
        [clubId]
    );
    return response;
};

module.exports = {
    subscribe,
    unsubscribe,
    getSubscribersCountByCid,
};
