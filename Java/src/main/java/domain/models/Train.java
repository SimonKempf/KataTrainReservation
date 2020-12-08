package domain.models;

import domain.models.Seat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Train {
    private final List<Seat> seats;
    private int reservedSeats;
    private int maxSeat = 0;



    public Train(final String trainTopol) {
        seats = fromJson(trainTopol);
    }

    private List<Seat> fromJson(String trainTopol) {
        final List<Seat> seats = new ArrayList<>();
        Seat e;
        //  sample
        //  {"seats": {"1A": {"booking_reference": "", "seat_number": "1", "coach": "A"},
        //  "2A": {"booking_reference": "", "seat_number": "2", "coach": "A"}}}

        JsonObject parsed = Json.createReader(new StringReader(trainTopol)).readObject();

        final Set<Map.Entry<String, JsonValue>> allStuffs = parsed.getJsonObject("seats").entrySet();


        this.reservedSeats = 0;
        for (Map.Entry<String, JsonValue> stuff : allStuffs) {
            final JsonObject seat = stuff.getValue().asJsonObject();
             e= new Seat(seat.getString("coach"), Integer.parseInt(seat.getString("seat_number")));
            seats.add(e);
            if(!seat.getString("booking_reference").isEmpty()){
                this.reservedSeats++;
            }
            this.maxSeat++;

            if(!seat.getString("booking_reference").isEmpty()) {
                e.setBookingRef(seat.getString("booking_reference"));
            }
        }
        return seats;
    }

    public List<Seat> getSeats() {
        return this.seats;
    }

    public int getReservedSeats() {
        return this.reservedSeats;
    }

    public int getMaxSeat() {
        return this.maxSeat;
    }

    public List<Seat> getSeats(int seats) {
        // find seats to reserve
        List<Seat> availableSeats = new ArrayList<>();
        for (int index = 0, i = 0; index < getSeats().size(); index++) {
            Seat each = (Seat) getSeats().toArray()[index];
            if ("".equals(each.getBookingRef())) {
                i++;
                if (i <= seats) {
                    availableSeats.add(each);
                }
            }
        }
        return availableSeats;
    }

    public boolean canBook(int seats) {
        return (getReservedSeats() + seats) <= Math.floor(ThreasholdManager.getMaxRes() * getMaxSeat());
    }
}
