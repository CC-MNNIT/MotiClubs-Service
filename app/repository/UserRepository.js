const pool = require("../db/db");

const saveUser = async (user) => {
    const [response] = await pool.execute(
        "INSERT INTO user(regno, name, email, course, phone, avatar) VALUES (?,?,?,?,?,?)",
        [
            user.regno,
            user.name,
            user.email,
            user.course,
            user.phone,
            user.avatar,
        ]
    );
    return response.insertId;
};

const getUserByUid = async (userId) => {
    const [response] = await pool.execute("SELECT * FROM user WHERE uid=?", [
        userId,
    ]);
    return response[0];
};

const updateAvatarByUid = async (userId, avatar) => {
    await pool.execute("UPDATE user SET avatar=? WHERE uid=?", [
        avatar,
        userId,
    ]);
};

const userExists = async (userId) => {
    const user = await getUserByUid(userId);
    return user !== null && user !== undefined;
};

module.exports = {
    saveUser,
    getUserByUid,
    updateAvatarByUid,
    userExists,
};
