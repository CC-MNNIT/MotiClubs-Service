import { Router } from "express";
import { auth } from "../middlewares/auth";
import { ReplyController } from "../controllers/ReplyController";

const router = Router();

// Get replies of post by postId in the query
router.get("/", auth.userAuthorization, ReplyController.getReplies);

// Add reply to post and notify users
router.post("/", auth.userAuthorization, ReplyController.saveReply);

// Delete a reply through replyId and userId authorization
router.delete("/", auth.replyAuthorization, ReplyController.deleteReply);

export const ReplyRouter = router;
