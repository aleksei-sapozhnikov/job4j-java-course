package ru.job4j.theater.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.complex.ComplexOperations;
import ru.job4j.theater.storage.complex.ComplexOperationsDatabase;
import ru.job4j.theater.storage.complex.ComplexOperationsHashSet;
import ru.job4j.theater.storage.repository.account.AccountRepository;
import ru.job4j.theater.storage.repository.account.AccountRepositoryHashSet;
import ru.job4j.theater.storage.repository.payment.PaymentRepository;
import ru.job4j.theater.storage.repository.payment.PaymentRepositoryHashSet;
import ru.job4j.theater.storage.repository.seat.SeatRepository;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryDatabase;
import ru.job4j.theater.storage.repository.seat.SeatRepositoryHashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({SeatRepositoryDatabase.class, ComplexOperationsDatabase.class})
public class SeatLogicTest {

    private final Account buyer = new Account.Builder("acc_1", "123-456").build();

    private final Seat seatFree = new Seat.Builder(1, 1, 50).build();
    private final Seat seatTwo = new Seat.Builder(1, 2, 43).build();
    private final Seat seatOccupied = new Seat.Builder(5, 8, 512).owner(this.buyer).build();

    private final ObjectMapper mapper = new ObjectMapper();

    private final ComplexOperations complexOperationsStub = ComplexOperationsHashSet.getInstance();
    private final SeatRepository seatRepositoryStub = SeatRepositoryHashSet.getInstance();
    private final PaymentRepository paymentRepositoryStub = PaymentRepositoryHashSet.getInstance();
    private final AccountRepository accountRepositoryStub = AccountRepositoryHashSet.getInstance();

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;

    @Before
    public void initStubStorage() throws SQLException {
        this.seatRepositoryStub.clear();
        this.paymentRepositoryStub.clear();
        this.accountRepositoryStub.clear();
        this.seatRepositoryStub.add(this.seatFree);
        this.seatRepositoryStub.add(this.seatTwo);
        this.seatRepositoryStub.add(this.seatOccupied);
    }


    @Test
    public void whenDoGetThenSortedListOfAllSeatsFromStorageGroupedByRows() throws SQLException, IOException {
        PowerMockito.mockStatic(SeatRepositoryDatabase.class);
        when(SeatRepositoryDatabase.getInstance()).thenReturn(this.seatRepositoryStub);
        this.req = mock(HttpServletRequest.class);
        this.resp = mock(HttpServletResponse.class);
        OutputStream response = new ByteArrayOutputStream();
        when(resp.getWriter()).thenReturn(new PrintWriter(response));
        new SeatLogic().doGet(req, resp);
        String result = String.valueOf(response);
        //
        List<List<Seat>> byRows = new ArrayList<>(
                this.seatRepositoryStub.getAll().stream()
                        .sorted()
                        .collect(Collectors.groupingBy(Seat::getRow))
                        .values());
        String expected = this.mapper.writeValueAsString(byRows);
        //
        assertThat(result, is(expected));
    }

    @Test
    public void whenDoPostAndSeatIsFreeThenBuySeat() throws IOException, SQLException {
        Map<String, String> requestObject = new HashMap<>();
        requestObject.put("seat_row", Integer.toString(this.seatFree.getRow()));
        requestObject.put("seat_column", Integer.toString(this.seatFree.getColumn()));
        requestObject.put("account_name", "vasya");
        requestObject.put("account_phone", "111-567");
        String requestString = this.mapper.writeValueAsString(requestObject);
        //
        PowerMockito.mockStatic(SeatRepositoryDatabase.class);
        when(SeatRepositoryDatabase.getInstance()).thenReturn(this.seatRepositoryStub);
        PowerMockito.mockStatic(ComplexOperationsDatabase.class);
        when(ComplexOperationsDatabase.getInstance()).thenReturn(this.complexOperationsStub);
        //
        this.req = mock(HttpServletRequest.class);
        when(req.getReader()).thenReturn(
                new BufferedReader(new StringReader(requestString)));
        this.resp = mock(HttpServletResponse.class);
        OutputStream response = new ByteArrayOutputStream();
        when(resp.getWriter()).thenReturn(new PrintWriter(response));
        new SeatLogic().doPost(req, resp);
        String resultResponse = String.valueOf(response);
        //
        String expectedResponse = String.format("{\"%s\":\"%s\"}", "success", "");
        Account expectedBuyer = new Account.Builder("vasya", "111-567").build();
        Payment expectedPayment = new Payment
                .Builder(this.seatFree.getPrice(), expectedBuyer)
                .comment(String.format("Payed for seat (%s, %s)", seatFree.getRow(), seatFree.getColumn()))
                .build();
        assertThat(resultResponse, is(expectedResponse));
        assertThat(this.accountRepositoryStub.getAll().get(0), is(expectedBuyer));
        assertThat(this.paymentRepositoryStub.getAll().get(0), is(expectedPayment));
    }

    @Test
    public void whenDoPostAndSeatIsOccupiedThenErrorAndNotBuySeatButAddBuyer() throws IOException, SQLException {
        Map<String, String> requestObject = new HashMap<>();
        requestObject.put("seat_row", Integer.toString(this.seatOccupied.getRow()));
        requestObject.put("seat_column", Integer.toString(this.seatOccupied.getColumn()));
        requestObject.put("account_name", "vasya");
        requestObject.put("account_phone", "111-567");
        String requestString = this.mapper.writeValueAsString(requestObject);
        //
        PowerMockito.mockStatic(SeatRepositoryDatabase.class);
        when(SeatRepositoryDatabase.getInstance()).thenReturn(this.seatRepositoryStub);
        PowerMockito.mockStatic(ComplexOperationsDatabase.class);
        when(ComplexOperationsDatabase.getInstance()).thenReturn(this.complexOperationsStub);
        //
        this.req = mock(HttpServletRequest.class);
        when(req.getReader()).thenReturn(
                new BufferedReader(new StringReader(requestString)));
        this.resp = mock(HttpServletResponse.class);
        OutputStream response = new ByteArrayOutputStream();
        when(resp.getWriter()).thenReturn(new PrintWriter(response));
        new SeatLogic().doPost(req, resp);
        String resultResponse = String.valueOf(response);
        //
        String expectedResponse = String.format(
                "{\"%s\":\"%s\"}", "error", "Не удалось купить место. Возможно, оно уже занято.");
        Account expectedBuyer = new Account.Builder("vasya", "111-567").build();
        boolean noPayment = false;
        try {
            //noinspection ResultOfMethodCallIgnored
            this.paymentRepositoryStub.getAll().get(0);
        } catch (IndexOutOfBoundsException e) {
            noPayment = true;
        }
        assertThat(resultResponse, is(expectedResponse));
        assertThat(this.accountRepositoryStub.getAll().get(0), is(expectedBuyer));
        assertThat(noPayment, is(true));
    }
}