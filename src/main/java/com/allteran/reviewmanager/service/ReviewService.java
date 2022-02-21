package com.allteran.reviewmanager.service;

import com.allteran.reviewmanager.domain.Review;
import com.allteran.reviewmanager.domain.User;
import com.allteran.reviewmanager.repo.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepo, UserService userService) {
        this.reviewRepo = reviewRepo;
        this.userService = userService;
    }

    public List<Review> findAll() {
        return reviewRepo.findAll();
    }

    public Review create(Review review) {
        return reviewRepo.save(review);
    }

    public Review updateReview(Review reviewFromDb, Review review) {
        if(review.getStartDate() != null) {
            return reviewFromDb;
        }
        BeanUtils.copyProperties(review, reviewFromDb, "id");
        reviewRepo.save(reviewFromDb);
        return reviewFromDb;
    }

    public void delete(Review review) {
        reviewRepo.delete(review);
    }

    public Review startReview(Review review, User user) {
        review.setStartDate(LocalDateTime.now());
        List<Review> passedReviews = user.getPassedReviews();
        if(passedReviews == null || passedReviews.isEmpty()) {
            passedReviews = new ArrayList<>();
        }
        passedReviews.add(review);
        user.setPassedReviews(passedReviews);

        userService.updateUser(user, user);
        return review;
    }

    public Review endReview(Review review, User user) {
        review.setEndDate(LocalDateTime.now());
        List<Review> passedReviews = user.getPassedReviews();
        if(passedReviews == null || passedReviews.isEmpty()) {
            passedReviews = new ArrayList<>();
        }
        passedReviews.removeIf(r -> Objects.equals(r.getId(), review.getId()));
        passedReviews.add(review);

        user.setPassedReviews(passedReviews);
        userService.updateUser(user, user);
        return review;
    }


//    @PostConstruct
//    public void initReviews() {
//        List<Review> reviews = new ArrayList<>();
//        for (int i = 0; i<15; i++) {
//            Review r = new Review();
//            r.setDescription("Description for review No." + i);
//            r.setName("Name for review No." + i);
//
//            Set<Question> questions = new HashSet<>();
//            Question q1 = new Question();
//            q1.setText("Question 1" + " for review no." + i);
//            q1.setType(QuestionType.SINGLE);
//
//            Set<Answer> answersQ1 = new HashSet<>();
//            Answer a1q1 = new Answer();
//            a1q1.setText("Answer for question 1 for review no." + i);
//            a1q1.setChecked(false);
//            answersQ1.add(a1q1);
//
//            q1.setAnswers(answersQ1);
//            questions.add(q1);
//
//            Question q2 = new Question();
//            q2.setText("Question 2" + " for review no." + i);
//            q2.setType(QuestionType.MULTIPLE);
//
//            Set<Answer> answersQ2 = new HashSet<>();
//            Answer a1q2 = new Answer();
//            a1q2.setText("Answer 1 for question 2 for review no." + i);
//            a1q2.setChecked(false);
//            answersQ2.add(a1q2);
//
//            Answer a2q2 = new Answer();
//            a2q2.setText("Answer 2 for question 2 for review no." + i);
//            a2q2.setChecked(false);
//            answersQ2.add(a2q2);
//
//            Answer a3q2 = new Answer();
//            a3q2.setText("Answer 3 for question 2 for review no." + i);
//            a3q2.setChecked(false);
//            answersQ2.add(a3q2);
//
//            q2.setAnswers(answersQ2);
//
//            questions.add(q2);
//
//            Question q3 = new Question();
//            q3.setText("Question 3" + " for review no." + i);
//            q3.setType(QuestionType.TEXT);
//
//            Set<Answer> answersQ3 = new HashSet<>();
//            Answer aq3 = new Answer();
//            aq3.setChecked(false);
//
//            answersQ3.add(aq3);
//
//            questions.add(q3);
//            r.setQuestions(questions);
//
//            reviews.add(r);
//        }
//
//        reviewRepo.saveAll(reviews);
//    }
}
