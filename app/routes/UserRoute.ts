import { Router } from "express";
import { UserController as controller } from "../controllers/UserController";
import { auth } from "../middlewares/auth";

const router = Router();

// Save user details
router.post("/", auth.signUpAuthorization, controller.saveUser);

// Get own details
router.get("/", auth.userAuthorization, controller.getUser);

// Get all admins
router.get("/admins", auth.userAuthorization, controller.getAdmins);

// Get all users
router.get("/all", auth.userAuthorization, controller.getAllUsers);

// Get user details
router.get("/:userId", auth.userAuthorization, controller.getUserByUid);

// Update avatar
router.post("/avatar", auth.userAuthorization, controller.updateAvatar);

// Update FCM Token
router.post("/fcmtoken", auth.userAuthorization, controller.updateFcmToken);

// Subscribe a club
router.post("/subscribe", auth.userAuthorization, controller.subscribe);

// Unsubscribe a club
router.post("/unsubscribe", auth.userAuthorization, controller.unsubscribe);

export const UserRouter = router;
