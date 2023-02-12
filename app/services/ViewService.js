const viewRepository = require("../repository/ViewRepository");

const getViewCount = async (postId) => {
    const count = await viewRepository.getViewCount(postId);
    return count;
};

const addView = async (postId, userId) => {
    await viewRepository.addView(postId, userId);
};

module.exports = {
    getViewCount,
    addView,
};
