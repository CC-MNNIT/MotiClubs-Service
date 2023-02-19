const viewRepository = require("../repository/ViewRepository");

const getViews = async (postId) => {
    const count = await viewRepository.getViews(postId);
    return count;
};

const addView = async (postId, userId) => {
    await viewRepository.addView(postId, userId);
};

module.exports = {
    getViews,
    addView,
};
