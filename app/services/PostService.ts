import { Post } from "../models/Post";
import { PostRepository } from "../repository/PostRepository";
import validate from "../utility/validate";

const getPosts = async (channelId: number, page: number, items: number) => {
    validate([channelId, page, items]);

    return await PostRepository.getPostsByClubAndChannel(channelId, page, items);
};

const savePost = async (post: Post) => {
    validate([post.pid, post.chid, post.message, post.time, post.uid, post.general]);

    await PostRepository.savePost(post);
};

const deletePost = async (postId: number) => {
    validate([postId]);

    await PostRepository.deletePostByPostId(postId);
};

const updatePost = async (postId: number, message: string) => {
    validate([postId]);

    await PostRepository.updatePostByPostId(postId, message);
};

export const PostService = {
    getPosts,
    savePost,
    deletePost,
    updatePost,
};
