const admin = require("../config/firebase");
const postRepository = require("../repository/PostRepository");
const adminRepository = require("../repository/AdminRepository");
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
    const token = req.header("Authorization");
    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        req.userId = decodedToken.userId;
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
    if (!token === process.env.SUPER_ADMIN_PASSWORD) {
        res.status(401).send({ message: "Unauthorized" });
        return;
    }
    next();
}

// Check if user is authorized to add / update post
async function postAuthorization(req, res, next) {
    const token = req.header("Authorization");
    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        req.userId = decodedToken.userId;
        if (req.method !== "POST") {
            const post = await postRepository.getPostByPostId(
                req.params.postId
            );
            if (post[0]["uid"] === req.userId) {
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

// Check if user is authorized to work update club
const clubAuthorization = async (req, res, next) => {
    const token = req.header("Authorization");
    try {
        const decodedToken = await admin.auth().verifyIdToken(token);
        req.userId = decodedToken.userId;
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

// Check is the user with email (email) is admin of club with id clubId
const clubAdminCheck = async (userId, clubId) => {
    try {
        const admins = adminRepository.getAdminsFromClubId(clubId);
        for (let i = 0; i < admins.length; ++i)
            if (admins[i] === userId) return true;
        return false;
    } catch (error) {
        console.log(error);
        return false;
    }
};

module.exports = {
    signUpAuthorization,
    userAuthorization,
    superAdmin,
    postAuthorization,
    clubAuthorization,
};
