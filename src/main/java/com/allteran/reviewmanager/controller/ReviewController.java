package com.allteran.reviewmanager.controller;

import com.allteran.reviewmanager.domain.Review;
import com.allteran.reviewmanager.domain.User;
import com.allteran.reviewmanager.service.ReviewService;
import com.allteran.reviewmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping("start/{id}")
    public Review startReview(@PathVariable("id") Review review, HttpServletResponse response, @CookieValue(name = "user_id") String anonUserId) {
        if(anonUserId.isEmpty()) {
            anonUserId = String.valueOf(ThreadLocalRandom.current().nextLong());
            Cookie cookie = new Cookie("user_id", anonUserId);
            response.addCookie(cookie);
        }
        User user = userService.findById(Long.parseLong(anonUserId));
        if(user == null) {
            user = new User();
            user.setId(Long.parseLong(anonUserId));
            userService.createUser(user);
        }
        review = reviewService.startReview(review, user);
        return review;
    }

    @PostMapping("end/{id}")
    public Review endReview(@PathVariable("id") Review review, HttpServletResponse response, @CookieValue(name = "user_id") String anonUserId) {
        if(anonUserId.isEmpty()) {
            anonUserId = String.valueOf(ThreadLocalRandom.current().nextLong());
            Cookie cookie = new Cookie("user_id", anonUserId);
            response.addCookie(cookie);
        }
        User user = userService.findById(Long.parseLong(anonUserId));
        if(user == null) {
            user = new User();
            user.setId(Long.parseLong(anonUserId));
            userService.createUser(user);
        }
        review = reviewService.endReview(review, user);
        return review;
    }

    @GetMapping("active")
    public List<Review> getActiveReviews(HttpServletResponse response, @CookieValue(name = "user_id") String anonUserId) {
        if (anonUserId.isEmpty()) {
            anonUserId = String.valueOf(ThreadLocalRandom.current().nextLong());
            Cookie cookie = new Cookie("user_id", anonUserId);
            response.addCookie(cookie);
        }
        User user = userService.findById(Long.parseLong(anonUserId));
        if (user == null) {
            user = new User();
            user.setId(Long.parseLong(anonUserId));
            userService.createUser(user);
        }
        List<Review> activeReviews = reviewService.findAll();
        if(user.getPassedReviews() != null) {
            activeReviews.removeAll(user.getPassedReviews());
        }
        return activeReviews;
    }

    @GetMapping("passed")
    public List<Review> getPassedReviews(@CookieValue(name = "user_id") String anonUserId) {
        if(!anonUserId.isEmpty()) {
            User user = userService.findById(Long.parseLong(anonUserId));
            if(user == null) {
                user = new User();
                user.setId(Long.parseLong(anonUserId));
                userService.createUser(user);
            }
            List<Review> passedReviews;
            if(user.getPassedReviews() == null) {
                passedReviews = new ArrayList<>();
            } else {
                passedReviews = user.getPassedReviews();
            }
            return passedReviews;
        }
        return new ArrayList<>();
    }
}
