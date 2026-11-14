package controller;

import entity.Comment;
import entity.Post;
import entity.Profile;
import enums.Role;
import service.TwitterService;
import utils.Util;

import java.util.List;

import static utils.Util.*;

public class TwitterController {

    private final TwitterService twitterService = new TwitterService();


    public void start() {

        while (true) {
            System.out.println("""
                    1. Login
                    2. Register
                    0. Exite""");

            int menu = inputInt("Choose one");

            switch (menu) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void login() {

        String username = inputStr("Enter your username");
        String password = inputStr("Enter your password");

        Role role = twitterService.login(username, password);

        if (role == null) System.out.println("Username or password is wrong try again");
        else if (role.equals(Role.ADMIN)) adminMenu();
        else if (role.equals(Role.USER)) userMenu();


    }


    private void register() {

        String fullName = inputStr("Enter your full name");
        String username = inputStr("Enter your username");
        String password = inputStr("Enter your password");

        boolean res = twitterService.register(fullName, username, password);
        if (res) System.out.println("Successfully register");
        else System.out.println("Failed to register");


    }


    // -------------------------Admin Menu------------------

    private void adminMenu() {

        while (true) {
            System.out.println("""
                    1. Show All users
                    2. Block User
                    3. Active User
                    4. Delete User
                    5. Change Password
                    0. Log out""");

            int menu = inputInt("Choose one");
            switch (menu) {
                case 1 -> showAllUsers();
                case 2 -> blockUser();
                case 3 -> activeUser();
                case 4 -> deleteUser();
                case 5 -> changePassword();
                case 0 -> {
                    return;
                }

            }
        }

    }

    private void showAllUsers() {
        List<Profile> profiles = twitterService.getAllUsers();

        for (Profile profile : profiles) {
            System.out.println(profile);
        }
    }


    private void blockUser() {
        showAllUsers();

        int id = inputInt("Choose id");
        twitterService.blockUserById(id);
        System.out.println("User blocked");

    }


    private void activeUser() {

        showAllUsers();

        int id = inputInt("Choose id");
        twitterService.activeUserById(id);
        System.out.println("User activated");

    }

    private void deleteUser() {
        showAllUsers();

        int id = inputInt("Enter delete user id");
        twitterService.deleteUserById(id);
        System.out.println("Successfully deleted");
    }


    private void changePassword() {

        String oldPassword = inputStr("Enter old password");
        String newPassword = inputStr("Enter new password");

        boolean res = twitterService.changePassword(oldPassword, newPassword);

        if (res) System.out.println("Successfully");
        else System.out.println("Error");

    }


    // ------------------------User Menu--------------------

    private void userMenu() {

        while (true) {
            System.out.println("""
                    1. Add Post
                    2. Show Posts
                    3. Delete Post
                    4. Show Global Posts
                    5. Change Password
                    0. Log out""");

            int menu = inputInt("Choose one");
            switch (menu) {
                case 1 -> addPost();
                case 2 -> showPost();
                case 3 -> deletePost();
                case 4 -> showGlobalPosts();
                case 5 -> changeUserPassword();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void addPost() {

        String title = inputStr("Enter post title");
        String content = inputStr("Enter post content");

        boolean res = twitterService.createPosts(title, content);

        if (res) System.out.println("Successfully post");
        else System.out.println("Failed to post");

    }


    private void showPost() {
        List<Post> posts = twitterService.getPostsByUserId(currentUserId);

        for (Post post : posts) {
            System.out.println(post);
        }
    }


    private void deletePost() {
        showPost();

        int id = inputInt("Delete post id");
        twitterService.deletePostById(id);
        System.out.println("Successfully post deleted");

    }


    private void showGlobalPosts() {

        List<Post> posts = twitterService.getAllPosts();
        postInfo(posts);
    }

    private void postInfo(List<Post> posts) {
        for (Post post : posts) {
            System.out.println(post);
        }

        int id = inputInt("Enter id");
        Post post = twitterService.getpostById(id);
        if (post == null) {
            System.out.println("Post not fount");
            return;
        }

        Profile profile = twitterService.getUserById(post.getUserId());

        int viewCount = twitterService.getViewCountById(post.getId());
        int likeCount = twitterService.getLikeCountById(post.getId());
        int commentCount = twitterService.getCommentCountById(post.getId());

        System.out.println("-----------" + profile.getUserName() + "-------------");
        System.out.println("Title: " + post.getTitle());
        System.out.println("Content: " + post.getContent());
        System.out.printf("View %d  Like %d  Comment %d \n", viewCount, likeCount, commentCount);

        System.out.println("""
                1. Like
                2. Comment
                """);
        int postMenu = inputInt("Choose one");

        if (postMenu == 1) {
            twitterService.createLike(currentUserId, post.getId());
        } else if (postMenu == 2) {
            writeComment(currentUserId, post.getId());
        }

        twitterService.createView(currentUserId, post.getId());


    }

    private void writeComment(int userId, int postId) {

        List<Comment> list = twitterService.getCommentByPostId(postId);

        Profile profile = twitterService.getUserById(userId);


        System.out.println("""
                1. Add Comment
                2. Comment Like
                """);

        int menu = inputInt("Choose one");

        if (menu == 1) {
            String comment = inputStr("Enter comment");
            twitterService.createComment(userId, postId, comment);
        } else if (menu == 2) {
            if (list.isEmpty()){
                System.out.println("no comments to like");
                return;
            }

            System.out.println("------" + profile.getUserName() + "-------");
            for (Comment comment : list) {
                System.out.println(comment.getId() + " " + comment);

                int likeComment = inputInt("Enter comment id");;
                twitterService.createCommentLike(userId, likeComment);

                int commentLikeCount = twitterService.getCountCommentLikeById(likeComment);
                System.out.println("Like: " + commentLikeCount);
            }





        }

    }

    private void changeUserPassword() {

        String oldPassword = inputStr("Enter old password");
        String newPassword = inputStr("Enter new password");

        boolean res = twitterService.changePassword(oldPassword, newPassword);

        if (res) System.out.println("Successfully");
        else System.out.println("Error");
    }
}
