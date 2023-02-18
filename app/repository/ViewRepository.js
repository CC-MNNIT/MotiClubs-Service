const pool = require("../db/db");

const getViews = async (postId) => {
    const [response] = await pool.execute(
        "SELECT * FROM view WHERE pid=?",
        [postId]
    );
    return response;
};

const addView = async (postId, userId) => {
    await pool.execute("INSERT INTO view (pid, uid) VALUES (?,?)", [
        postId,
        userId,
    ]);
};

module.exports = {
    getViews,
    addView,
};
