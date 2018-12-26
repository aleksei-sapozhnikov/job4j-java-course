package ru.job4j.theater.storage.repository.seat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Seat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple seat repository based on Set.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SeatRepositoryHashSet implements SeatRepository {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(SeatRepositoryHashSet.class);
    /**
     * Singleton instance.
     */
    private static final SeatRepository INSTANCE = new SeatRepositoryHashSet();
    private final Set<Seat> store = new HashSet<>();

    /**
     * Constructor.
     */
    private SeatRepositoryHashSet() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static SeatRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Seat seat) {
        this.store.add(seat);
    }

    @Override
    public List<Seat> getAll() {
        return new ArrayList<>(this.store);
    }

    /**
     * Updates seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @param newObj Seat with new values.
     */
    @Override
    public void updateByPlace(int row, int column, Seat newObj) {
        Seat seat = this.getByPlace(row, column);
        this.store.remove(seat);
        seat = new Seat
                .Builder(newObj.getRow(), newObj.getColumn(), newObj.getPrice())
                .owner(newObj.getOwner())
                .build();
        this.store.add(seat);
    }

    @Override
    public Seat getByPlace(int row, int column) {
        return this.store.stream()
                .filter(seat -> seat.getRow() == row && seat.getColumn() == column)
                .findFirst()
                .orElse(Seat.getEmptySeat());
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() {
        this.store.clear();
    }
}
