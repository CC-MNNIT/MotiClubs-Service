const pool = require("../db/db");
const subscribersRepository = require("./SubscribersRepository");

const setTokenByUid = async (userId, token) => {
    await pool.execute("INSERT INTO fcm (uid, token) VALUES (?,?)", [
        userId,
        token,
    ]);
};

const updateTokenByUid = async (userId, token) => {
    await pool.execute("UPDATE fcm SET token=? WHERE uid=?", [token, userId]);
};

const getAllTokens = async () => {
    const [response] = await pool.execute("SELECT * FROM fcm");
    return response;
};

const getTokensOfSubscribers = async (clubId) => {
    const [response] = pool.execute(
        "SELECT A.uid, token FROM fcm INNER JOIN (SELECT * FROM subscribers WHERE cid=?) A ON A.uid = fcm.uid",
        [clubId]
    );
    return response;
};

module.exports = {
    setTokenByUid,
    updateTokenByUid,
    getAllTokens,
    getTokensOfSubscribers,
};
