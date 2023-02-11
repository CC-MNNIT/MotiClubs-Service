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

const getAdmins = async () => {
    const [response] = await pool.execute(
        "SELECT user.uid as userId, name, email, phone, avatar FROM user INNER JOIN admin ON user.uid = admin.uid"
    );
    return response;
}

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
    getAdmins,
    updateAvatarByUid,
    userExists,
};
