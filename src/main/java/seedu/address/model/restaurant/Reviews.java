package seedu.address.model.restaurant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Restaurant's reviews in the address book.
 */
public class Reviews {

    public static final String MESSAGE_OVERALL_RATING_CONSTRAINTS =
            "Rating must be a positive integer from 1 to 5 where 1 is the lowest rating and 5, the highest rating.";

    private static DecimalFormat df = new DecimalFormat("#.00");
    private List<UserReview> userReviewList;
    private double restaurantRating;
    private String restaurantRatingValue;

    /**
     * Constructs {@code Reviews}.
     */
    public Reviews() {
        userReviewList = new ArrayList<>();
        restaurantRating = 0.00;
        this.restaurantRatingValue = "0.00";
    }

    /**
     * Constructs {@code Reviews}.
     *
     * @param restaurantRatingValue The overall rating of a restaurant.
     * @param userReviewList A list of UserReview.
     */
    public Reviews(String restaurantRatingValue, List<UserReview> userReviewList) {
        requireNonNull(restaurantRatingValue);
        requireNonNull(userReviewList);
        checkArgument(isValidReviewsRating(restaurantRatingValue), MESSAGE_OVERALL_RATING_CONSTRAINTS);
        this.userReviewList = userReviewList;
        if (restaurantRatingValue.equals("0.00")) {
            this.restaurantRating = 0.00;
            this.restaurantRatingValue = "0.00";
        } else {
            this.restaurantRating = Double.parseDouble(restaurantRatingValue);
            this.restaurantRatingValue = df.format(restaurantRating);
        }
    }

    public static boolean isValidReviewsRating(String test) {
        try {
            double testRating = Double.parseDouble(test);
            if (testRating < 1.0 || testRating > 5.0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getRestaurantRatingValue() {
        return restaurantRatingValue;
    }

    public List<UserReview> getUserReviewList() {
        return userReviewList;
    }

    /**
     * Adds a User Review to the Reviews Object of a restaurant.
     *
     * The restaurantRatingValue of a restaurant is the average rating of a restaurant and
     * is updated each time a {@code userReview} is added.
     */
    public Reviews addUserReview(UserReview userReview) {
        userReviewList.add(userReview);
        if (userReviewList.size() == 1) {
            restaurantRating = (double) userReview.getRating();
            this.restaurantRatingValue = df.format(restaurantRating);
            return this;
        }
        restaurantRating = restaurantRating + (double) userReview.getRating();
        Double tempCalculation = restaurantRating / userReviewList.size();
        this.restaurantRatingValue = df.format(tempCalculation);
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Reviews)) {
            return false;
        }

        return restaurantRatingValue.equals(((Reviews) other).restaurantRatingValue)
                && userReviewList.equals(((Reviews) other).userReviewList);
    }
}
