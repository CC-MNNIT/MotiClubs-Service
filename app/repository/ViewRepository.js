const pool = require("../db/db");

const getViewCount = async (postId) => {
    const [response] = await pool.execute(
        "SELECT COUNT(uid) as count FROM view WHERE pid=?",
        [postId]
    );
    return response[0].count;
};

const addView = async (postId, userId) => {
    await pool.execute("INSERT INTO view (pid, uid) VALUES (?,?)", [
        postId,
        userId,
    ]);
};

module.exports = {
    getViewCount,
    addView,
};
