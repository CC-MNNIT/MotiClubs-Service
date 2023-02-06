const postModel = require("../models/PostModel");
const validate = require("../utility/validate");

const getPosts = async (clubId) => {
	// Create filter object
	// If id is null, send all posts, else post of the required club
	const clubFilter = clubId ? { club: clubId } : {};

	// Fetch posts from most to least recent with filter
	const posts = await postModel.find(clubFilter).sort({ time: -1 });

	return posts;
};

const savePost = async (message, club, adminEmail) => {
	validate([message, club]);

	// Create Post object
	const post = new postModel({
		message: message,
		time: Date.now(),
		club: club,
		adminEmail: adminEmail,
	});

	await post.save();

	return post;
};

const deletePost = async (post) => {
	validate([post]);

	await postModel.deleteOne({ _id: post }).exec();
};

const updatePost = async (post, message) => {
	validate([post]);

	await postModel
		.updateOne({ _id: post }, { message: message, time: Date.now() })
		.exec();

	// Get updated post
	const updatedPost = await postModel.findOne({ _id: post });
	return updatedPost;
};

module.exports = {
	getPosts,
	savePost,
	deletePost,
	updatePost,
};
