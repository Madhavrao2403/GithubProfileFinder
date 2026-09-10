package com.mini.GithubProfileFinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

@SpringBootApplication
public class GithubProfileFinderApplication {

	public static void main(String[] args) {

        SpringApplication.run(GithubProfileFinderApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your UserName of Github Profile: ");
        String userName = scanner.nextLine();
        try {
            String json = fetchGithub(userName);
            GithubUser user = parseResponse(json);
            displayUserDetails(user);
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
        }
	}

    public static String fetchGithub(String userName) throws Exception {
        String url = "https://api.github.com/users/" + userName;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Github Profile Found");
        }
        else if(response.statusCode() == 404) {
            throw new RuntimeException("User Not Found");
        }
        else{
            throw new RuntimeException("Status code " + response.statusCode());
        }
        return response.body();
    }

    public static GithubUser parseResponse(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GithubUser user = mapper.readValue(json, GithubUser.class);
        return user;
    }
    public static void displayUserDetails(GithubUser user) {
        System.out.println("========================================");
        System.out.println("             GITHUB PROFILE             ");
        System.out.println("========================================"+"\n");
        System.out.println("User Name: " + user.getLogin());
        System.out.println("Name: " + user.getName()+"\n");
        System.out.println("Bio: " + user.getBio()+"\n");
        System.out.println("company: " + user.getCompany());
        System.out.println("location: " + user.getLocation()+"\n");

        System.out.println("----------------------------------------");
        System.out.println("              GITHUB STATS              ");
        System.out.println("----------------------------------------"+"\n");
        System.out.println("Public Repos: "+user.getPublicRepos());
        System.out.println("Public Gists: "+user.getPublicGists());
        System.out.println("followers: "+user.getFollowers());
        System.out.println("following: "+user.getFollowing()+"\n");

        System.out.println("----------------------------------------");
        System.out.println("                 ACCOUNT                ");
        System.out.println("----------------------------------------"+"\n");
        System.out.println("Created At: "+user.getCreatedAt());
        System.out.println("Updated At: "+user.getUpdatedAt()+"\n");
        System.out.println("Avatar: "+user.getAvatarUrl()+"\n");
        System.out.println("========================================");
    }
}
