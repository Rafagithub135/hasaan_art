package com.example.hasaan_art.controllers;

import com.example.hasaan_art.models.Art;
import com.example.hasaan_art.repositories.ArtRepository;
import com.example.hasaan_art.repositories.CommentsRepository;
import com.example.hasaan_art.repositories.CustomerRepository;
import com.example.hasaan_art.repositories.PostsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PostsController {
    private final ArtRepository teamDao;
    private final CustomerRepository userDao;
    private final PostsRepository postDao;
    private final CommentsRepository commentDao;

    public PostsController(ArtRepository artDao, CustomerRepository customerDao, PostsRepository postDao, CommentsRepository commentDao) {
        this.teamDao = artDao;
        this.userDao = customerDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
    }
    @GetMapping("/art/{id}/posts")
    public String showTeamPosts(@PathVariable long id, Model model) {
        Customer loggedCustomer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Art art = artDao.findArtById(id);
        List<Customer> customers = customerDao.findAllByArt(art);
        List<Post> posts = postDao.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
        List<Post> filteredPosts = new ArrayList<>();
        if (!art.getCustomers().contains(loggedCustomer)) {
            return "redirect:/art/" + id;
        }
        for (Post post : posts) {
            for (Customer customer : customers) {
                if (post.getCustomer().getId() == customer.getId()) {
                    filteredPosts.add(post);
                    break;
                }
            }
        }
        model.addAttribute("customer", customers);
        model.addAttribute("art", art);
        model.addAttribute("posts", filteredPosts);
        return "posts/art-posts";
    }

    @PostMapping("/art/{id}/posts/create")
    public String createPost(@PathVariable long id, Model model, @RequestParam String title, @RequestParam String content) {
        User user = userDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setTimestamp(new Date());
        post.setCustomer(customer);
        postDao.save(post);
        return "redirect:/art/" + id + "/posts";
    }

    @PostMapping("/art/{id}/posts/{postId}/delete")
    public String deletePost(@PathVariable long id, @PathVariable long postId) {
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postDao.findPostById(postId);
        if (customer.getId() == post.getCustomer().getId()) {
            postDao.deleteById(postId);
        }
        return "redirect:/art/" + id + "/posts";
    }

    @PostMapping("/art/{id}/posts/{postId}/edit")
    public String editPost(@PathVariable long id, @PathVariable long postId, @RequestParam(name = "post-title") String title, @RequestParam(name = "post-content") String content) {
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postDao.findPostById(postId);
        if (customer.getId() == post.getCustomer().getId()) {
            post.setTitle(title);
            post.setContent(content);
            post.setEditing(false);
            postDao.save(post);
        }
        return "redirect:/art/" + id + "/posts";
    }

    @PostMapping("/art/{id}/posts/{postId}/comments/create")
    public String createComment(@PathVariable long id, @PathVariable long postId, @RequestParam(name = "comment-content") String content) {
        Art art = artDao.findArtById(id);
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postDao.findPostById(postId);
        if (!art.getCustomers().contains(customer)) {
            return "redirect:/art/" + id + "/posts";
        }
        Comment comment = new Comment(content, new Date(), customer, post);
        commentDao.save(comment);
        return "redirect:/art/" + id + "/posts";
    }

    @PostMapping("/art/{id}/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable long id, @PathVariable long postId, @PathVariable long commentId) {
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postDao.findPostById(postId);
        Comment comment = commentDao.findCommentById(commentId);
        if (customer.getId() == comment.getCustomer().getId()) {
            commentDao.deleteById(commentId);
        }
        return "redirect:/art/" + id + "/posts";
    }
}