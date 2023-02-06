const pool = require("../db/db");

const setTokenByUid = async (userId, token) => {
    const response = await pool.execute(
        "INSERT INTO fcm (uid, token) VALUES (?,?)",
        [userId, token]
    );
    return response;
};

const updateTokenByUid = async (userId, token) => {
    const response = await pool.execute("UPDATE fcm SET token=? WHERE uid=?", [
        token,
        userId,
    ]);
    return response;
};

module.exports = { setTokenByUid, updateTokenByUid };
