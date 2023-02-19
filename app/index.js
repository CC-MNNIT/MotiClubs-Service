const express = require("express");
var cors = require("cors");
const app = express();

app.use(cors());
app.use(express.json());

app.use("/user", require("./routes/UserRoute"));
app.use("/clubs", require("./routes/ClubRoute"));
app.use("/posts", require("./routes/PostRoute"));
app.use("/admin", require("./routes/SuperAdminRoute"));
app.use("/channel", require("./routes/ChannelRoute"));
app.use("/url", require("./routes/UrlRoute"));
app.use("/views", require("./routes/ViewRoute"));

module.exports = app;
