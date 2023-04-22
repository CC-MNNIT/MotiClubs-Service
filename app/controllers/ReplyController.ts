import { Request, Response } from "express";
import { ReplyService } from "../services/ReplyService";

const getReplies = async (req: Request, res: Response) => {
    try {
        const replies = await ReplyService.getReplies(Number(req.query.postId));
        res.status(200).send(replies);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const saveReply = async (req: Request, res: Response) => {
    try {
        const { pid, uid, to_uid, message, time } = req.body;
        await ReplyService.saveReply({
            pid: Number(pid),
            uid: Number(uid),
            to_uid: Number(to_uid),
            message: String(message),
            time: Number(time),
        });

        // Send response to user
        res.status(200).send({});

        // await notifyUsers(Number(pid), 2);
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

const deleteReply = async (req: Request, res: Response) => {
    try {
        await ReplyService.deleteReply(Number(req.query.replyId));

        res.status(200).send({});
    } catch (error) {
        console.log(error);
        res.status(400).send({ message: error.message });
    }
};

export const ReplyController = {
    getReplies,
    saveReply,
    deleteReply,
};
