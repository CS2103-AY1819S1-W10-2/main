package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_RESTAURANT;
import static seedu.address.testutil.TypicalRestaurants.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserData;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectRestaurantCommand}.
 */
public class SelectRestaurantCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserData());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserData());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastRestaurantIndex = Index.fromOneBased(model.getFilteredRestaurantList().size());

        assertExecutionSuccess(INDEX_FIRST_RESTAURANT);
        assertExecutionSuccess(INDEX_THIRD_RESTAURANT);
        assertExecutionSuccess(lastRestaurantIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT);

        assertExecutionSuccess(INDEX_FIRST_RESTAURANT);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT);

        Index outOfBoundsIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getRestaurantList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectRestaurantCommand selectFirstCommand = new SelectRestaurantCommand(INDEX_FIRST_RESTAURANT);
        SelectRestaurantCommand selectSecondCommand = new SelectRestaurantCommand(INDEX_SECOND_RESTAURANT);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectRestaurantCommand selectFirstCommandCopy = new SelectRestaurantCommand(INDEX_FIRST_RESTAURANT);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectRestaurantCommand} with the given {@code index},
     * and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectRestaurantCommand selectCommand = new SelectRestaurantCommand(index);
        String expectedMessage =
                String.format(SelectRestaurantCommand.MESSAGE_SELECT_RESTAURANT_SUCCESS, index.getOneBased());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code SelectRestaurantCommand} with the given {@code index},
     * and checks that a {@code CommandException} is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectRestaurantCommand selectCommand = new SelectRestaurantCommand(index);
        assertCommandFailure(selectCommand, model, commandHistory, expectedMessage);
        assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
    }
}
