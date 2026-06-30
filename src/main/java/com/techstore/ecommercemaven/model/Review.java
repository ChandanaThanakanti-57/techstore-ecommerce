package com.techstore.ecommercemaven.model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private int rating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime reviewDate;

    private boolean approved = false;

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(
            LocalDateTime reviewDate) {

        this.reviewDate = reviewDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
