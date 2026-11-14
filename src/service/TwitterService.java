package service;

import dao.TwitterDao;
import entity.*;
import enums.Role;
import enums.Status;
import utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TwitterService {

    private final TwitterDao twitterDao = new TwitterDao();


    public boolean register(String fullName, String username, String password) {

        Profile test = twitterDao.getProfileByUserName(username);
        if (test != null) return false;


        Profile profile = new Profile(Util.userId++, fullName, username, password, Role.USER, Status.ACTIVE);
        twitterDao.saveProfile(profile);
        return true;
    }


    public Role login(String username, String password) {

        Profile profile = twitterDao.getProfileByUserName(username);

        if (profile == null) return null;

        if (!profile.getPassword().equals(password)) return null;

        Util.currentUserId = profile.getId();

        return profile.getRole();

    }

    public boolean createPosts(String title, String content) {

        if (title == null) return false;

        if (title.isBlank()) return false;

        Profile profile = twitterDao.getProfileById(Util.currentUserId);
        if (profile.getStatus().equals(Status.BLOCK)) return false;

        Post post = new Post(Util.postId++, title, content, Util.currentUserId);
        twitterDao.savePost(post);
        return true;
    }

    public List<Post> getPostsByUserId(int userId) {
        List<Post> list = twitterDao.getAllPosts();

        List<Post> resList = new ArrayList<>();

        for (Post post : list) {
            if (post.getUserId() == userId) resList.add(post);
        }
        return resList;
    }


    public List<Post> getAllPosts() {
        return twitterDao.getAllPosts();
    }

    public List<Profile> getAllUsers() {
        return twitterDao.getAllUsers();
    }


    public void blockUserById(int id) {
        List<Profile> profiles = twitterDao.getAllUsers();
        for (Profile profile : profiles) {
            if (profile.getId() == id) {
                profile.setStatus(Status.BLOCK);
                break;
            }
        }
    }

    public void activeUserById(int id) {
        List<Profile> profiles = twitterDao.getAllUsers();
        for (Profile profile : profiles) {
            if (profile.getId() == id) {
                profile.setStatus(Status.ACTIVE);
                break;
            }
        }
    }


    public void deleteUserById(int id) {
        twitterDao.deleteUserById(id);
    }

    public boolean changePassword(String oldPassword, String newPassword) {

        Profile profile = twitterDao.getProfileById(Util.currentUserId);

        if (profile == null) return false;

        if (!profile.getPassword().equals(oldPassword)) return false;

        profile.setPassword(newPassword);

        return true;

    }


    public void deletePostById(int id) {
        twitterDao.deletePostById(id);
    }

    public Post getpostById(int id) {
        List<Post> posts = twitterDao.getAllPosts();

        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    public Profile getUserById(int userId) {
        List<Profile> profiles = twitterDao.getAllUsers();

        for (Profile profile : profiles) {
            if (profile.getId() == userId) {
                return profile;
            }
        }
        return null;
    }

    public void createView(int userId, int postId) {
        View view = new View(userId, postId);
        twitterDao.saveView(view);
    }

    public int getViewCountById(int postId) {
        List<View> viewList = twitterDao.getViewList();

        int count = 0;

        for (View view : viewList) {
            if (view.getPostId() == postId) {
                count++;
            }
        }
        return count;
    }

    public void createLike(int userId, int postId) {

        List<Like> likeList = twitterDao.getLikeList();
        Iterator<Like> iterator = likeList.iterator();

        while (iterator.hasNext()) {
            Like like = iterator.next();
            if (like.getUserId() == userId && like.getPostId() == postId) {
                iterator.remove();
                return;
            }
        }


        Like like = new Like(userId, postId);
        twitterDao.saveLike(like);
    }

    public int getLikeCountById(int postId) {
        List<Like> likeList = twitterDao.getLikeList();

        int count = 0;

        for (Like like : likeList) {
            if (like.getPostId() == postId) {
                count++;
            }
        }
        return count;
    }

    public void createComment(int userId, int postId, String comment) {
        Comment commentObj = new Comment(userId, postId, comment);
        twitterDao.saveComment(commentObj);

    }

    public List<Comment> getCommentByPostId(int postId) {
        List<Comment> commentList = twitterDao.getCommentList();

        List<Comment> restlist = new ArrayList<>();

        for (Comment comment : commentList) {
            if (comment.getPostId() == postId) {
                restlist.add(comment);
            }
        }
        return restlist;
    }

    public int getCommentCountById(int postId) {

        List<Comment> postsCommentList = getCommentByPostId(postId);
        return postsCommentList.size();
    }

    public void createCommentLike(int userId, int commentId) {
        List<CommentLike> commentLikeList = twitterDao.getCommentLikeList();

        Iterator<CommentLike> iterator = commentLikeList.iterator();

        while (iterator.hasNext()) {
            CommentLike like = iterator.next();

            if (like.getUserId() == userId && like.getCommentId() == commentId){
                iterator.remove();
                return;
            }
        }

        CommentLike commentLike = new CommentLike(userId, commentId);
        twitterDao.saveCommentLike(commentLike);
    }


    public int getCountCommentLikeById(int commentId){
        List<CommentLike> commentLikeList = twitterDao.getCommentLikeList();

        int count =0;

        for (CommentLike commentLike : commentLikeList) {
            if (commentLike.getCommentId()==commentId){
                count++;
            }
        }
        return count;
    }


    public List<Post> getPosts(){
        return twitterDao.getPosts();
    }

}
