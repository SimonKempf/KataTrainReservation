package coltrain.test.acceptance;

import coltrain.*;
import coltrain.api.models.Seat;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColtrainTest {

    public static final String BOOKING_REFERENCE = "75bcd15";
    private static final String EMPTY_BOOKING = "";
    public static final String TRAIN_ID = "express_2000";

    @Test
    public void should_reserve_seats_when_train_is_empty() {
        final TrainDataService trainDataService = new FakeTrainDataService(TrainTopology.EMPTY_TRAIN);
        final FakeBookingReferenceService bookingReferenceService = new FakeBookingReferenceService(BOOKING_REFERENCE);
        final WebTicketManager sut = new WebTicketManager(trainDataService, bookingReferenceService);

        final String reservation = sut.reserve(TRAIN_ID, 3);

        assertEquals("{\"trainId\": \"" + TRAIN_ID + "\",\"bookingReference\": \"" + BOOKING_REFERENCE + "\",\"seats\":[\"1A\", \"2A\", \"3A\"]}", reservation);
    }

    @Test
    public void should_not_reserve_seats_when_train_is_70_percent_booked() {
        final TrainDataService trainDataService = new FakeTrainDataService(TrainTopology.WITH_10_SEATS_AND_6_ALREADY_BOOKED);
        final FakeBookingReferenceService bookingReferenceService = new FakeBookingReferenceService(BOOKING_REFERENCE);
        final WebTicketManager sut = new WebTicketManager(trainDataService, bookingReferenceService);

        final String reservation = sut.reserve(TRAIN_ID, 3);

        assertEquals("{\"trainId\": \"" + TRAIN_ID + "\",\"bookingReference\": \"" + EMPTY_BOOKING + "\",\"seats\":[]}", reservation);
    }


    @Test
    @Ignore("should_reserve_all_seats_in_the_same_coach - This domain rule does not seems to be implemented !!")
    public void should_reserve_all_seats_in_the_same_coach() {
        final TrainDataService trainDataService = new FakeTrainDataService(TrainTopology.WITH_COACH_A_HAVING_1_FREE_SEAT_AND_COACH_B_EMPTY);
        final FakeBookingReferenceService bookingReferenceService = new FakeBookingReferenceService(BOOKING_REFERENCE);
        final WebTicketManager sut = new WebTicketManager(trainDataService, bookingReferenceService);

        final String reservation = sut.reserve(TRAIN_ID, 3);

        assertEquals("{\"trainId\": \"" + TRAIN_ID + "\",\"bookingReference\": \"" + BOOKING_REFERENCE + "\",\"seats\":[\"1B\", \"2B\", \"3B\"]}", reservation);
    }

    private class FakeTrainDataService implements TrainDataService {
        private String topology;

        private FakeTrainDataService(final String topology) {
            this.topology = topology;
        }

        @Override
        public Train getTrain(final String trainId) {
            return new Train(TrainDataServiceImpl.adaptTrainTopology(topology));
        }

        @Override
        public void bookSeats(final String trainId, final List<Seat> availableSeats, final String bookingRef) {

        }
    }

    private class FakeBookingReferenceService implements BookingReferenceService {
        private final String bookingReference;

        public FakeBookingReferenceService(final String bookingReference) {
            this.bookingReference = bookingReference;
        }

        @Override
        public String getBookingReference() {
            return bookingReference;
        }
    }
}