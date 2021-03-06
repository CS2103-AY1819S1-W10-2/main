package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalRestaurants.RESTAURANT_A;
import static seedu.address.testutil.TypicalRestaurants.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.exceptions.DuplicateRestaurantException;
import seedu.address.testutil.RestaurantBuilder;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getRestaurantList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateRestaurants_throwsDuplicateRestaurantException() {
        // Two restaurants with the same identity fields
        Restaurant editedAlice = new RestaurantBuilder(RESTAURANT_A).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        List<Restaurant> newRestaurants = Arrays.asList(RESTAURANT_A, editedAlice);
        AddressBookStub newData = new AddressBookStub(newRestaurants);

        thrown.expect(DuplicateRestaurantException.class);
        addressBook.resetData(newData);
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.hasRestaurant(null);
    }

    @Test
    public void hasRestaurant_restaurantNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasRestaurant(RESTAURANT_A));
    }

    @Test
    public void hasRestaurant_restaurantInAddressBook_returnsTrue() {
        addressBook.addRestaurant(RESTAURANT_A);
        assertTrue(addressBook.hasRestaurant(RESTAURANT_A));
    }

    @Test
    public void hasRestaurant_restaurantWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addRestaurant(RESTAURANT_A);
        Restaurant editedAlice = new RestaurantBuilder(RESTAURANT_A).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasRestaurant(editedAlice));
    }

    @Test
    public void getRestaurantList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getRestaurantList().remove(0);
    }

    /**
     * A stub ReadOnlyAddressBook whose restaurants list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

        AddressBookStub(Collection<Restaurant> restaurants) {
            this.restaurants.setAll(restaurants);
        }

        @Override
        public ObservableList<Restaurant> getRestaurantList() {
            return restaurants;
        }
    }

}
