const admin = require("../config/firebase");
const postRepository = require("../repository/PostRepository");
const adminRepository = require("../repository/AdminRepository");
const channelRepository = require("../repository/ChannelRepository");
require("dotenv").config();

// Verify JWT
async function signUpAuthorization(req, res, next) {
    const token = req.header("Authorization");
    try {
        await admin.auth().verifyIdToken(token);
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Please log in first" });
        return;
    }
    next();
}

// Check if user is logged in
async function userAuthorization(req, res, next) {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
    } catch (error) {
        console.log(error);
        res.status(401).send({ message: "Please log in first" });
        return;
    }
    next();
}

// Check if user is super admin
async function superAdmin(req, res, next) {
    const token = req.header("Authorization");
    if (token !== process.env.SUPER_ADMIN_PASSWORD) {
        res.status(401).send({ message: "Unauthorized" });
        return;
    }
    next();
}

// Check if user is authorized to add / update post
async function postAuthorization(req, res, next) {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
        if (req.method !== "POST") {
            const post = await postRepository.getPostByPostId(
                req.params.postId
            );
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
}

// Check if user is authorized to access club modification apis
const clubAuthorization = async (req, res, next) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
        const isAdmin = await clubAdminCheck(req.userId, req.params.clubId);
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

// Check if user is authorized to access club url modification apis
const urlAuthorization = async (req, res, next) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
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

// Check if user is authorized to access channel modification apis
const channelAuthorization = async (req, res, next) => {
    try {
        const token = req.header("Authorization");
        req.userId = await getUserId(token);
        let clubId = "";
        if (req.method === "POST") {
            clubId = req.body.clubId;
        } else {
            const channelId = req.params.channelId;
            const channel = await channelRepository.getChannelByChannelId(
                channelId
            );
            clubId = channel.cid;
        }
        const isAdmin = await clubAdminCheck(req.userId, clubId);
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

// Check is the user with email (email) is admin of club with id clubId
const clubAdminCheck = async (userId, clubId) => {
    console.log(userId, clubId);
    try {
        const admins = await adminRepository.getAdminsFromClubId(clubId);
        for (let i = 0; i < admins.length; ++i)
            if (admins[i].userId === userId) return true;
        return false;
    } catch (error) {
        console.log(error);
        return false;
    }
};

const getUserId = async (token) => {
    const decodedToken = await admin.auth().verifyIdToken(token);
    const firebaseUser = await admin.auth().getUserByEmail(decodedToken.email);
    return firebaseUser.customClaims.userId;
};

module.exports = {
    signUpAuthorization,
    userAuthorization,
    superAdmin,
    postAuthorization,
    clubAuthorization,
    urlAuthorization,
    channelAuthorization,
};
