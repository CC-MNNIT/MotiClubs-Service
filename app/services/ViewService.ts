import { ViewRepository } from "../repository/ViewRepository";
import validate from "../utility/validate";

const getViews = async (postId: number) => {
    validate([postId]);

    return (await ViewRepository.getViews(postId));
};

const addView = async (postId: number, userId: number) => {
    validate([postId, userId]);

    await ViewRepository.addView({ pid: postId, uid: userId });
};

export const ViewService = {
    getViews,
    addView,
};
