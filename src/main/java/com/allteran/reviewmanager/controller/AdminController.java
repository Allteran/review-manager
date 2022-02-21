package com.allteran.reviewmanager.controller;

import com.allteran.reviewmanager.domain.Review;
import com.allteran.reviewmanager.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/adm/review")
public class AdminController {
    private final ReviewService reviewService;

    @Autowired
    public AdminController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("list")
    public List<Review> getAll() {
        return reviewService.findAll();
    }

    @GetMapping("{id}")
    public Review getOne(@PathVariable("id") Review review) {
        return review;
    }

    @PostMapping("new")
    public Review create(@RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping("{id}")
    public Review update(@PathVariable("id") Review reviewFromDb, @RequestBody Review review) {
        return reviewService.updateReview(reviewFromDb, review);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Review review) {
        reviewService.delete(review);
    }
}
