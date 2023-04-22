import { Reply } from "../models/Reply";
import { ReplyRepository } from "../repository/ReplyRepository";
import validate from "../utility/validate";

const getReplies = async (postId: number) => {
    validate([postId]);

    return await ReplyRepository.getRepliesByPid(postId);
};

const saveReply = async (reply: Reply) => {
    validate([reply.pid, reply.uid, reply.to_uid, reply.message, reply.time]);

    await ReplyRepository.saveReply(reply);
};

const deleteReply = async (replyID: number) => {
    validate([replyID]);

    await ReplyRepository.deleteReply(replyID);
};

export const ReplyService = {
    getReplies,
    saveReply,
    deleteReply,
};
