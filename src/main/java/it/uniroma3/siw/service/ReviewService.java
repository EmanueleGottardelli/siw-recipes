package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public Review getReviewById(Long id) {
		return this.reviewRepository.findById(id).get();
	}
	
	public List<Review> getAllReview(){
		List<Review> result = new ArrayList<>();
		
		for(Review r : this.reviewRepository.findAll()) {
			result.add(r);
		}
		
		return result;
	}
	
	public List<Review> getReviewByEvaluation(int evaluation){
		return this.reviewRepository.findByEvaluation(evaluation);
	}
	
	public Review saveReview(Review review) {
		return this.reviewRepository.save(review);
	}
	
	public void deleteReview(Review review) {
		this.reviewRepository.delete(review);
	}

}
