const service = require("../services/ViewService");

const getViewCount = async (req, res) => {
    try {
        const count = await service.getViewCount(
            req.query ? req.query.postId : -1
        );
        res.status(200).send({ count: count });
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const addView = async (req, res) => {
    try {
        await service.addView(req.body.postId, req.userId);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

module.exports = {
    getViewCount,
    addView,
};
