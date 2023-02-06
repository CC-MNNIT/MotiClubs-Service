const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});

const saveUser = async (user) => {
    const response = await con.execute(
        "INSERT INTO user(name, email, course, phone, avatar) VALUES (?,?,?,?,?)",
        [user.name, user.email, user.course, user.phone, user.avatar]
    );
    return response;
};

const getUserByUid = async (userId) => {
    const response = await con.execute("SELECT * FROM user WHERE uid=?", [
        userId,
    ]);
    return response;
};

const updateAvatarByUid = async (userId, avatar) => {
    const response = await con.execute("UPDATE user SET avatar=? WHERE uid=?", [
        avatar,
        userId,
    ]);
    return response;
};

module.exports = {
    saveUser,
    getUserByUid,
    updateAvatarByUid,
};
