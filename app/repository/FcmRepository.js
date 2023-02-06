const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});

const setTokenByUid = async (userId, token) => {
    const response = await con.execute(
        "INSERT INTO fcm (uid, token) VALUES (?,?)",
        [userId, token]
    );
    return response;
};

const updateTokenByUid = async (userId, token) => {
    const response = await con.execute("UPDATE fcm SET token=? WHERE uid=?", [
        token,
        userId,
    ]);
    return response;
};

module.exports = { setTokenByUid, updateTokenByUid };
