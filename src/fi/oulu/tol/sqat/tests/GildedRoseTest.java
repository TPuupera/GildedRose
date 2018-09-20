package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80));
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
//   Item("Conjured Mana Cake", 3, 6));
	GildedRose store = new GildedRose();
	int days=0;
	@Test
	public void testUpdateEndOfDay_Normal_Item_Decrease() {
        // 10 days left
		store.addItem(new Item("Normal Item", 10, 10));

		do {
			// Change value with days
			days++;
			store.updateEndOfDay();
			assertEquals(10-days, store.getItems().get(0).getQuality());
			assertEquals(10-days, store.getItems().get(0).getSellIn());
		} while (days<10);
	}
	
	@Test
	public void testUpdateEndOfDay_Normal_Item_Decrease_Quality_Twice_As_Fast__On_Sell_By_Date() {
		
		//Sell by date
		store.addItem(new Item("Normal Item", 0, 10));
		// Change value with 2 days
		store.updateEndOfDay();
		store.updateEndOfDay();
	 
		assertEquals(6, store.getItems().get(0).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_Sulfuras_Quality_Doesnt_Alter() {
		
		//10 days left
		store.addItem(new Item("Sulfuras", 10, 80));
		// change value with 1 day
		store.updateEndOfDay();
		// Quality does not decrease
		assertEquals(80, store.getItems().get(0).getQuality());
		
		// Sell by date
		store.addItem(new Item("Sulfuras", 0, 80));
		// Change value with 1 day
		store.updateEndOfDay();
		// Quality does not decrease
		assertEquals(80, store.getItems().get(1).getQuality());
	}
		
	@Test
	public void testUpdateEndOfDay_Quality_Is_Never_Negative() {
		
		// 10 days left
		store.addItem(new Item("Basic item", 10, 0));
		// Change value with 1 day
		store.updateEndOfDay();
		 
		assertEquals(0, store.getItems().get(0).getQuality());
		
		// Sell by date
		store.addItem(new Item("Basic item", 0, 0));
		// Change value with 1 day
		store.updateEndOfDay();
		 
		assertEquals(0, store.getItems().get(1).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_AgedBrie_Increases_Quality() {
		// Add aged brie
		store.addItem(new Item("Aged Brie", 50, 10));
		// Change value with 1 day
		store.updateEndOfDay();
		//Aged brie increases quality the older it gets
		assertEquals(11, store.getItems().get(0).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_Quality_Is_Never_More_Than_50() {
		// Add aged brie
		store.addItem(new Item("Aged Brie", 10, 50));
		// Change value with 1 day
		store.updateEndOfDay();
		// Item cannot have quality increase above 50
		assertEquals(50, store.getItems().get(0).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_Backstage_Pass_Increases_Quality_By_2_When_10_Or_Less_Days_Left() {
		
		// 10 days to concert
		store.addItem(new Item("Backstage pass", 10, 10));
		// Change value with 1 day
		store.updateEndOfDay();
		 
		assertEquals(12, store.getItems().get(0).getQuality());
		
		// 9 Days to concert
		store.addItem(new Item("Backstage pass", 9, 10));
		// Change value with 1 day
		store.updateEndOfDay();
		// Quality value increased by two
		assertEquals(12, store.getItems().get(1).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_Backstage_Pass_Increases_Quality_By_3_When_5_Or_Less_Days_Left() {
		
		// 5 Days to concert
		store.addItem(new Item("Backstage pass", 5, 10));
		// Change value with 1 day
		store.updateEndOfDay();
		// Quality value increased by three
		assertEquals(13, store.getItems().get(0).getQuality());
		// 4 Days to concert
		store.addItem(new Item("Backstage pass", 4, 8));
		// Change value with 1 day
		store.updateEndOfDay();
		// Quality value increased by three
		assertEquals(11, store.getItems().get(1).getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_Backstage_Pass_Quality_To_0_After_Concert() {
		
		// 1 Day after concert
		store.addItem(new Item("Backstage pass", -1, 10));
		// Change value with 1 day
		store.updateEndOfDay();
		// Quality value is zero
		assertEquals(0, store.getItems().get(0).getQuality());
	}
}