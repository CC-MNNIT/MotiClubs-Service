import * as cors from 'cors';
import * as dotenv from 'dotenv';
import * as express from 'express';

dotenv.config();

import { ChannelRouter } from "./routes/ChannelRoute";
import { ClubRouter } from "./routes/ClubRoute";
import { PostRouter } from "./routes/PostRoute";
import { ReplyRouter } from './routes/ReplyRoute';
import { SuperAdminRouter } from "./routes/SuperAdminRoute";
import { UrlRouter } from "./routes/UrlRoute";
import { UserRouter } from "./routes/UserRoute";
import { ViewRouter } from "./routes/ViewRoute";

const app: express.Application = express();

app.use(cors());
app.use(express.json());

const rootUrl: string = process.env.ROOT_URL || "";

app.use(`${rootUrl}/user`, UserRouter);
app.use(`${rootUrl}/clubs`, ClubRouter);
app.use(`${rootUrl}/posts`, PostRouter);
app.use(`${rootUrl}/admin`, SuperAdminRouter);
app.use(`${rootUrl}/channel`, ChannelRouter);
app.use(`${rootUrl}/url`, UrlRouter);
app.use(`${rootUrl}/views`, ViewRouter);
app.use(`${rootUrl}/reply`, ReplyRouter);
app.use(`${rootUrl}/`, (_, res) => res.status(200).send({ message: "API Running user" }));

export default app;
