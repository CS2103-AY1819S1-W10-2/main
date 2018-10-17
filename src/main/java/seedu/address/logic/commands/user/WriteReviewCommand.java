package seedu.address.logic.commands.user;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVIEW;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.restaurant.Rating;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.UserReview;
import seedu.address.model.restaurant.WrittenReview;
import seedu.address.model.user.Username;

import java.util.List;

/**
 * Adds a review to the user.
 */
public class WriteReviewCommand extends Command {
    public static final String COMMAND_WORD = "WriteReview";

    // TODO
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Gives a review of the restaurant identified "
            + "by the index number used in the displayed restaurant list. A review consists of a rating that must "
            + "be a positive integer from 1 to 5 where 1 is the lowest rating and 5, the highest rating, and a written "
            + "review of experience at the restaurant."
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_RATING + "RATING "
            + PREFIX_REVIEW + "REVIEW\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_RATING + "3 "
            + PREFIX_REVIEW + "I tried the cold noodles and pork bulgogi for dinner. They were delicious. "
            + "The place packed a crowd during dinner time which indicates that the food is good and the queue "
            + "clears fast. Situated at at a nice location, I would certainly go back for more.";

    public static final String MESSAGE_SUCCESS = "Successfully Wrote Review";
    public static final String MESSAGE_ALREADY_LOGGEDIN = "User is already logged in";

    private final Index index;
    private final Rating rating;
    private final WrittenReview writtenReview;

    /**
     * Creates a WriteReview to add the specified {@code Integer} review, that ranges from 1 - 5.
     */
    public WriteReviewCommand(Index index, Rating rating, WrittenReview writtenReview) {
        requireNonNull(index);
        requireNonNull(rating);
        requireNonNull(writtenReview);

        this.index = index;
        this.rating = rating;
        this.writtenReview = writtenReview;
        
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        // TODO
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (model.isCurrentlyLoggedIn()) {
            throw new CommandException(MESSAGE_ALREADY_LOGGEDIN);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }
        
        model.addReview(rating, writtenReview);

        return null;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WriteReviewCommand // instanceof handles nulls
                && rating.equals(((WriteReviewCommand) other).rating)
                && writtenReview.equals(((WriteReviewCommand) other).writtenReview));                ;
    }
}