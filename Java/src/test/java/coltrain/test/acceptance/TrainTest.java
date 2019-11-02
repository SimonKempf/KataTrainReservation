package coltrain.test.acceptance;

import coltrain.Coach;
import coltrain.Train;
import coltrain.TrainDataServiceImpl;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TrainTest {
    @Test
    public void should_expose_its_coaches() {
        final Train sut = new Train(TrainDataServiceImpl.adaptTrainTopology(TrainTopology.WITH_10_SEATS_AND_6_ALREADY_BOOKED));
        final Map<String, Coach> coaches = sut.getCoaches();
        assertEquals(coaches.size(), 2);
        assertEquals(coaches.get("A").getSeats().size(), 5);
        assertEquals(coaches.get("B").getSeats().size(), 5);
    }
}