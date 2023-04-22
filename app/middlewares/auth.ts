import * as dotenv from "dotenv";
import { NextFunction, Request, Response } from "express";
import admin from "../config/firebase";
import { AdminRepository } from "../repository/AdminRepository";
import { ChannelRepository } from "../repository/ChannelRepository";
import { PostRepository } from "../repository/PostRepository";
import { SuperAdminRepository } from "../repository/SuperAdminRepository";
import { ReplyRepository } from "../repository/ReplyRepository";

dotenv.config();

declare module "express-serve-static-core" {
    interface Request {
        userId?: number;
    }
};

// Verify JWT
const signUpAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    const token = req.header("Authorization");
    try {
        await admin.auth().verifyIdToken(token);
    } catch (error) {
        res.status(401).send({ message: "Please log in first" });
    }
    next();
};

// Check if user is logged in
const userAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Please log in first" });
        return;
    }
    next();
};

// Check if user is super admin
const superAdmin = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
        const isSuperAdmin = await SuperAdminRepository.isSuperAdmin(req.userId);
        if (!isSuperAdmin) {
            throw new Error("Unauthorized");
        }
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
        return;
    }
    next();
};

// Check if user is authorized to add / update post
const postAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
        if (req.method !== "POST") {
            const postID = Number(req.params.postId);
            if (isNaN(postID)) {
                res.status(401).send({ message: "Unauthorized: Invalid post ID" });
                return;
            }

            const post = await PostRepository.getPostByPostId(Number(req.params.postId));
            if (post["uid"] === req.userId) {
                next();
                return;
            }
            res.status(401).send({ message: "Unauthorized" });
            return;
        }
        const isAdmin = await clubAdminCheck(req.userId, req.body.clubId);
        if (isAdmin) {
            next();
            return;
        }
        res.status(401).send({ message: "Unauthorized" });
        return;
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
        return;
    }
};

// Check if user is authorized to access club modification apis
const clubAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);

        const clubID = Number(req.params.clubId);
        if (isNaN(clubID)) {
            res.status(401).send({ message: "Unauthorized: Invalid club ID" });
            return;
        }

        const isAdmin = await clubAdminCheck(req.userId, clubID);
        if (isAdmin) {
            next();
            return;
        }
        res.status(401).send({ message: "Unauthorized" });
        return;
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
    }
};

// Check if user is authorized to access club url modification apis
const urlAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);

        const clubID = Number(req.query.clubId);
        if (isNaN(clubID)) {
            res.status(401).send({ message: "Unauthorized: Invalid club ID" });
            return;
        }

        const isAdmin = await clubAdminCheck(Number(req.userId), clubID);
        if (isAdmin) {
            next();
            return;
        }
        res.status(401).send({ message: "Unauthorized" });
        return;
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
    }
};

// Check if user is authorized to access channel modification apis
const channelAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);

        let clubID: number = -1;
        if (req.method === "POST") {
            const cid = Number(req.body.cid);
            if (isNaN(cid)) {
                res.status(401).send({ message: "Unauthorized: Invalid club ID" });
                return;
            }
            clubID = cid;
        } else {
            const channelID = Number(req.params.channelId);
            if (isNaN(channelID)) {
                res.status(401).send({ message: "Unauthorized: Invalid channel ID" });
                return;
            }
            const channel = await ChannelRepository.getChannelByChannelId(channelID);
            clubID = channel.cid;
        }

        const isAdmin = await clubAdminCheck(req.userId, clubID);
        if (isAdmin) {
            next();
            return;
        }
        res.status(401).send({ message: "Unauthorized" });
        return;
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
    }
};

const replyAuthorization = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);

        const replyTime = Number(req.query.replyId);
        if (isNaN(replyTime)) {
            res.status(401).send({ message: "Unauthorized: Invalid reply ID" });
            return;
        }

        const reply = await ReplyRepository.getReplyByTime(replyTime);
        if (reply.uid === req.userId) {
            next();
            return;
        }
        res.status(401).send({ message: "Unauthorized" });
        return;
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Unauthorized" });
    }
};

// Check is the user with email (email) is admin of club with id clubId
const clubAdminCheck = async (userId: number, clubId: number): Promise<boolean> => new Promise(async (resolve, reject) => {
    try {
        const admins = await AdminRepository.getAdminsFromClubId(clubId);
        const hasAdmin = admins.find((admin) => admin.id === userId);
        resolve(hasAdmin !== undefined);
    } catch (error) {
        console.log(error);
        reject(error);
    }
});

const getUserId = async (token: string): Promise<number> => new Promise(async (resolve, reject) => {
    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        const user = await admin.auth().getUserByEmail(decodedToken.email);
        resolve(user.customClaims.userId);
    } catch (error) {
        reject(error);
    }
});

export const auth = {
    signUpAuthorization,
    userAuthorization,
    superAdmin,
    postAuthorization,
    clubAuthorization,
    urlAuthorization,
    channelAuthorization,
    replyAuthorization,
};
