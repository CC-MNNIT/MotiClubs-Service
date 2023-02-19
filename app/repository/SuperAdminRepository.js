const pool = require("../db/db");

const isSuperAdmin = async (userId) => {
    const [response] = await pool.execute(
        "SELECT uid as userId FROM super_admin WHERE uid=?",
        [userId]
    );
    if (response.length > 0) return true;
    return false;
};

module.exports = { isSuperAdmin };
