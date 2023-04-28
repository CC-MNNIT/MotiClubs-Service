import { Router } from "express";
import { ChannelController as controller } from "../controllers/ChannelController";
import { auth } from "../middlewares/auth";

const router = Router();

// Get all channels or channels of club with clubId passed in query parameter
router.get("/", auth.userAuthorization, controller.getChannels);

// Get channel from channel ID
router.get(
    "/:channelId",
    auth.userAuthorization,
    controller.getChannelByChannelId
);

// Add new channel, body requires chid, cid and name
router.post("/", auth.channelAuthorization, controller.saveChannel);

// Delete a channel with channelId passed in query parameter
router.delete(
    "/:channelId",
    auth.channelAuthorization,
    controller.deleteChannel
);

// Update channel name, body requires channelName
router.put(
    "/:channelId",
    auth.channelAuthorization,
    controller.updateChannelName
);

export const ChannelRouter = router;
