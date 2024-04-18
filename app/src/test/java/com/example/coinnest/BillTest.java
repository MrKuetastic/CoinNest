package com.example.coinnest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BillTest {

    private Bill bill;
    private final String testBillName = "DTE";
    private final double testAmount = 300.0;
    private final String testDueDate = "2024-04-17";
    private final String testCategory = "Utilities";

    @Before
    public void setUp() {
        bill = new Bill(testBillName, testAmount, testDueDate, testCategory);
    }

    @Test
    public void setBillName_AssignsCorrectValue() {
        bill.setBillName("New Name");
        assertEquals("New Name", bill.getBillName());
    }

    @Test
    public void getBillName_ReturnsAssignedValue() {
        assertEquals(testBillName, bill.getBillName());
    }

    @Test
    public void setAmount_AssignsCorrectValue() {
        double newAmount = 500.0;
        bill.setAmount(newAmount);
        assertEquals(Double.valueOf(newAmount), bill.getAmount(), 0.0);
    }

    @Test
    public void getAmount_ReturnsAssignedValue() {
        assertEquals(Double.valueOf(testAmount), bill.getAmount(), 0.0);
    }

    @Test
    public void setDueDate_AssignsCorrectValue() {
        String newDueDate = "2024-05-17";
        bill.setDueDate(newDueDate);
        assertEquals(newDueDate, bill.getDueDate());
    }

    @Test
    public void getDueDate_ReturnsAssignedValue() {
        assertEquals(testDueDate, bill.getDueDate());
    }

    @Test
    public void setCategory_AssignsCorrectValue() {
        String newCategory = "Healthcare";
        bill.setCategory(newCategory);
        assertEquals(newCategory, bill.getCategory());
    }

    @Test
    public void getCategory_ReturnsAssignedValue() {
        assertEquals(testCategory, bill.getCategory());
    }
}
