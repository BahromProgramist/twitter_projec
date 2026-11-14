package dao;

import entity.*;
import enums.Role;
import enums.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TwitterDao {

    private final Path pathPost = Path.of("posts_file/post.txt");

    private final Path pathProfile = Path.of("profile_file/profile.txt");

    private List<Profile> profileList = new ArrayList<>();

    private List<Post> postList = new LinkedList<>();

    private List<View> viewList = new ArrayList<>();

    private List<Like> likeList = new LinkedList<>();

    private List<Comment> commentList = new ArrayList<>();

    private List<CommentLike> commentLikeList = new ArrayList<>();

    {
        profileList.add(new Profile(1, "Ali Aliyev", "ali", "123", Role.ADMIN, Status.ACTIVE));
    }



    public void saveProfileByFile(Profile profile){
        try {
            Files.writeString(pathProfile, profile.toString() + "\n",StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Profile> getProfile() {
        List<Profile> profiles = new ArrayList<>();

        try {

            List<String> list = Files.readAllLines(pathProfile);
            for (String s : list) {
                String[] array = s.split("#");

                Profile profile = new Profile(Integer.parseInt(array[0]), array[1], array[2], array[3], Role.valueOf(array[4]), Status.valueOf(array[5]));
                profiles.add(profile);
            }

            return profiles;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


    public void savePosts(Post post) {
        try {
            Files.writeString(pathPost, post.toString() + "\n", StandardOpenOption.CREATE,  StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Post> getPosts() {

        List<Post> posts = new ArrayList<>();

        try {

            List<String> list = Files.readAllLines(pathPost);
            for (String s : list) {
                String[] array = s.split("#");

                Post post = new Post(Integer.parseInt(array[0]), array[1], array[2], Integer.parseInt(array[3]));
                posts.add(post);
            }

            return posts;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }






    public TwitterDao(){

        try {
            Files.createDirectories(pathProfile.getParent());
            Files.createDirectories(pathPost.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileList.addAll(getProfile());
        postList.addAll(getPosts());
    }



    public void saveProfile(Profile profile) {
        profileList.add(profile);
        saveProfileByFile(profile);
    }

    public void savePost(Post post) {
        postList.add(post);
        savePosts(post);
    }

    public void saveView(View view) {
        viewList.add(view);
    }

    public void saveLike(Like like) {
        likeList.add(like);
    }

    public void saveComment(Comment commentObj) {
        commentList.add(commentObj);
    }

    public void saveCommentLike(CommentLike commentLike) {
        commentLikeList.add(commentLike);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public List<View> getViewList() {
        return viewList;
    }

    public List<Profile> getAllUsers() {
        return profileList;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public List<CommentLike> getCommentLikeList() {
        return commentLikeList;
    }

    public Profile getProfileByUserName(String username) {
        for (Profile p : profileList) {
            if (p.getUserName().equals(username)) {
                return p;
            }
        }

        return null;
    }

    public Profile getProfileById(int id) {
        for (Profile p : profileList) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }


    public List<Post> getAllPosts() {
        return postList;
    }


    public List<Profile> deleteUserById(int id) {

        for (int i = 0; i < profileList.size(); i++) {
            if (profileList.get(i).getId() == id) {
                profileList.remove(i);
                break;
            }
        }
        return profileList;
    }


    public List<Post> deletePostById(int id) {

        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getId() == id) {
                postList.remove(i);
                break;
            }
        }

        return postList;
    }





}
