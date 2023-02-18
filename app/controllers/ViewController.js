const service = require("../services/ViewService");

const getViews = async (req, res) => {
    try {
        const views = await service.getViews(
            req.query ? req.query.postId : -1
        );
        res.status(200).send(views);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const addView = async (req, res) => {
    try {
        await service.addView(req.body.pid, req.userId);
    } catch (_) {
    } finally {
        res.status(200).send({});
    }
};

module.exports = {
    getViews,
    addView,
};
