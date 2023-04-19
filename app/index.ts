import * as express from 'express';
import * as cors from 'cors';

import { ChannelRouter } from "./routes/ChannelRoute";
import { ClubRouter } from "./routes/ClubRoute";
import { PostRouter } from "./routes/PostRoute";
import { SuperAdminRouter } from "./routes/SuperAdminRoute";
import { UrlRouter } from "./routes/UrlRoute";
import { UserRouter } from "./routes/UserRoute";
import { ViewRouter } from "./routes/ViewRoute";

const app: express.Application = express();

app.use(cors());
app.use(express.json());

app.use("/user", UserRouter);
app.use("/clubs", ClubRouter);
app.use("/posts", PostRouter);
app.use("/admin", SuperAdminRouter);
app.use("/channel", ChannelRouter);
app.use("/url", UrlRouter);
app.use("/views", ViewRouter);

export default app;
