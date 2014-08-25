package com.webservice.android.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webservice.android.listeners.LoginListener;
import com.webservice.android.listeners.StockCodeItemSelectedListener;
import com.webservice.android.listeners.StockLocationItemSelectedListener;
import com.webservice.android.listeners.StockTradeItemSelectedListener;
import com.webservice.android.util.IssueStockSpinnerOnItemSelected;
import com.webservice.android.util.IssueStockTextWatcher;
import com.webservice.android.util.StringComparator;
import com.webservice.android.xml.StockItemListTranslator;
import com.webservice.android.xml.StockLevelTranslator;
import com.webservice.android.xml.StockTradeListTranslator;
import com.webservice.android.xml.StoremanTranslator;
import com.webservice.domain.Location;
import com.webservice.domain.LocationList;
import com.webservice.domain.StockItem;
import com.webservice.domain.StockItemList;
import com.webservice.domain.StockLevel;
import com.webservice.domain.StockLevelList;
import com.webservice.domain.StockLocation;
import com.webservice.domain.StockToIssue;
import com.webservice.domain.StockTrade;
import com.webservice.domain.StockTradeList;
import com.webservice.domain.StoremanList;
import com.webservice.domain.Storemen;

public class IssueStockActivity extends DialogActivity {
	private static int QUANTITY_LIMIT=20;
	ArrayAdapter<String> stockTradeAdapter;
	ArrayAdapter<String> stockLocationAdapter;
	ArrayAdapter<String> stockLevelsAdapter;
	ArrayAdapter<String> stockCodeAdapter;
	ArrayAdapter<String> stockItemAdapter;
	ArrayAdapter<String> quantityAdapter;
	ArrayAdapter<String> storemenAdapter;
	IssueStockSpinnerOnItemSelected issueStockSpinner1,issueStockSpinner2,issueStockSpinner3,issueStockSpinner4,issueStockSpinner5;
	IssueStockTextWatcher watcher1, watcher2, watcher3;
	List<String> stockItemNames;
    List<String> stockLocationNames;
    List<String> stockTradeNames;
    List<String> quantityList;
    List<String> storemenNames;
    List<String> stockLevelNames;
	EditText username, password, levels, description, refreshed, stockIssue;
	TextView error;
	List<StockItem> stockItems;
	List<StockLocation> stockLocationList;
	LocationList locationList;
	StockLevelList stockLevelList;
	StockTradeList stockTradeList;
	StockItemList stockItemList;
	StockTradeItemSelectedListener stockTradeListener;
	StockCodeItemSelectedListener stockCodeListener;
	StockLocationItemSelectedListener stockLocationListener;
	String response;
	String data;
	Spinner stockTradeSpinner;
	Spinner stockCodeSpinner;
	Spinner stockLocationSpinner;
	Spinner quantitySpinner;
	Spinner storemenSpinner;
    StockTradeList stockList;
    StockToIssue stockToIssue;
    StoremanList storemenList;
	Button previous;
	LoginListener loginListener;
	int count;
	int peopleListSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue_stock);
		Bundle bundle = getIntent().getExtras();
		locationList = bundle.getParcelable("com.webservice.domain.LocationList");
		stockToIssue = bundle.getParcelable("com.webservice.domain.StockToIssue");
		if ( data==null) {
			data = this.getContext().getSchedule();
		}
		if ( storemenList==null) {
			storemenList = populateStoremanList(data);
		}
		if ( stockLevelList==null) {
			stockLevelList = populateStockLevelList(data);
		}
		if ( stockItemList==null) {
			stockItemList = populateStockItems(data);
		}
		if ( stockTradeList==null) {
			stockTradeList = populateStockTrade(data);
		}
		stockTradeListener = new StockTradeItemSelectedListener(this,stockToIssue);
		stockCodeListener = new StockCodeItemSelectedListener(this,stockToIssue);
		stockToIssue.setIsNew("0");
		stockLocationListener = new StockLocationItemSelectedListener(this,stockToIssue);
		storemenNames = getStoremenNames(storemenList);
		stockLocationNames = getStockLocationNames(locationList);
		description=(EditText) findViewById(R.id.is60);
		description.setTextSize(12);
		description.setEnabled(false);
		levels=(EditText) findViewById(R.id.is100);
		levels.setEnabled(false);
		stockIssue=(EditText) findViewById(R.id.is150);
		stockIssue.setText("Issue Stock");
		stockIssue.setTypeface(Typeface.DEFAULT_BOLD);
		stockIssue.setEnabled(false);
		refreshed=(EditText) findViewById(R.id.is120);
		refreshed.setEnabled(false);
		stockTradeSpinner=(Spinner) findViewById(R.id.is20);
		storemenSpinner=(Spinner) findViewById(R.id.is170);
		stockCodeSpinner=(Spinner) findViewById(R.id.is40);
		stockLocationSpinner=(Spinner) findViewById(R.id.is80);
		quantitySpinner=(Spinner) findViewById(R.id.is140);
		issueStockSpinner1 = new IssueStockSpinnerOnItemSelected(stockToIssue,"stock");
		issueStockSpinner2 = new IssueStockSpinnerOnItemSelected(stockToIssue,"storemen");
		issueStockSpinner3 = new IssueStockSpinnerOnItemSelected(stockToIssue,"stockitem");
		issueStockSpinner5 = new IssueStockSpinnerOnItemSelected(stockToIssue,"quantity");
		watcher1 = new IssueStockTextWatcher(stockToIssue,"level");
		watcher2 = new IssueStockTextWatcher(stockToIssue,"refresh");
		refreshed.addTextChangedListener(watcher2);
		watcher3 = new IssueStockTextWatcher(stockToIssue,"description");
		description.addTextChangedListener(watcher3);
		quantityList = getQuantityList();
		quantityAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, quantityList);
		quantitySpinner.setAdapter(quantityAdapter);
		quantitySpinner.setOnItemSelectedListener(issueStockSpinner5);
		stockTradeNames = getStockTradeNames(stockTradeList);
		stockTradeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, stockTradeNames);
		stockTradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stockTradeAdapter.sort(new StringComparator());
		stockTradeSpinner.setAdapter(stockTradeAdapter);
		stockTradeSpinner.setOnItemSelectedListener(stockTradeListener);
		storemenAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, storemenNames);
		storemenSpinner.setAdapter(storemenAdapter);
		storemenSpinner.setOnItemSelectedListener(issueStockSpinner2);
		stockCodeSpinner.setAdapter(stockCodeAdapter);
		stockCodeSpinner.setOnItemSelectedListener(stockCodeListener);
		stockLocationNames = getStockLocationNames(locationList);
		stockLocationAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, stockLocationNames);
		stockLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stockLocationAdapter.sort(new StringComparator());
		stockLocationSpinner.setAdapter(stockLocationAdapter);
		stockLocationSpinner.setOnItemSelectedListener(stockLocationListener);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	public Spinner getStockCodeSpinner() {
		return stockCodeSpinner;
	}
	
	public List<String> getStockCodeList(String value) {
		List<String> codeList = new ArrayList<String>();
		String id = getTradeId(value);
		for (StockItem item:stockItemList.getAllStockItems()) {
			if (item.getType().equalsIgnoreCase(id)) {
				codeList.add(item.getCode());
			}
		}
	   return codeList;	
	}
	
	public String getStockCodeDescription(String value) {
		String desc="";
		boolean found=false;
        StockItem item;
		Iterator<StockItem> list = stockItemList.getAllStockItems().iterator();
		while ((list.hasNext() && (!found))) {
			item = list.next();
			if (item.getCode().equalsIgnoreCase(value)) {
				desc= item.getName();
			}
		}
		return desc;
	}
	
	public EditText getStockCodeField() {
		  return description;
	  }
	
	public void setStockLevelAndRefreshed(String value) {
		Iterator<StockLevel> iter = stockLevelList.getAllStock().iterator();
		StockLevel stockLevel=null;
		boolean found=false;
		String refresh = "";
		String level="";
		String partNum=value;
		while ((iter.hasNext()) && (!found)) {
			stockLevel = iter.next();
			if (stockLevel.getPartNumber().equalsIgnoreCase(partNum)) {
				found=true;
				refresh = stockLevel.getRefreshed();
				level = "Part Number: "+stockLevel.getPartNumber()+" & Quantity:"+stockLevel.getQuantity();
			}
		}
		levels.setText(level);
		refreshed.setText(refresh);
		setStockQuantities(stockLevel.getQuantity());
		stockToIssue.setUpdated("1");
	}
	
	public void setStockLevelFields(String value) {
		setStockLevelAndRefreshed(value);
		setStockQuantities(value);
		
	}
	
	public void setStockQuantities(String value) {
		int upperAmount = Integer.valueOf(value);
		for (int i=0;i<upperAmount;i++) {
			quantityList.add(String.valueOf(i+1));
		}
		quantityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, quantityList);
 	    quantitySpinner.setAdapter(quantityAdapter);
	    quantitySpinner.refreshDrawableState();
	}
	
	private String getTradeId(String value) {
		String id="";
		String name="";
		StockTrade trade;
		boolean found = false;
		Iterator<StockTrade> trades = stockTradeList.getStockTradeList().iterator();
		while ((trades.hasNext()) && (!found)) {
			trade = trades.next();
			name = trade.getDesc();
			if (name.equalsIgnoreCase(value)) {
				id = trade.getId();
				found=true;
			}
		}
	   return id;
	}
	
	private StockLevelList populateStockLevelList(String data) {
		StockLevelTranslator stockLevelTranslator = new StockLevelTranslator();
		StockLevelList levelList = stockLevelTranslator.translate(data);
		return levelList;
	}
	
	public StockTradeList populateStockTrade(String data) {
		StockTradeListTranslator stockTranslator = new StockTradeListTranslator();
		return stockTranslator.translate(data);
	}
	
	public StockItemList populateStockItems(String data) {
		StockItemListTranslator stockItemTranslator = new StockItemListTranslator();
		return stockItemTranslator.translate(data);
	}

	private List<String> getStockLocationNames(LocationList locationList) {
		List<String> stockLocationNames = new ArrayList<String>();
		for (Location location:locationList.getAllLocation()) {
			stockLocationNames.add(location.getReference());
		}
		return stockLocationNames;
	}
	

	private StoremanList populateStoremanList(String data) {
		StoremanTranslator storemanTranslator = new StoremanTranslator();
		StoremanList storemanList = storemanTranslator.translate(data);
		return storemanList;
	}
	
	private List<String> getStockItemNames(List<StockItem> stockItems) {
		List<String> stockItemNames = new ArrayList<String>();
		for (StockItem stockItem:stockItems) {
			stockItemNames.add(stockItem.getCode()+" : "+stockItem.getName());
		}
		return stockItemNames;
	}
	
	private List<String> getQuantityList() {
		List<String> quantityList = new ArrayList<String>();
		for (int i=0; i<QUANTITY_LIMIT; i++) {
			quantityList.add(Integer.valueOf(i+1).toString());
		}
		return quantityList;
	}

	private List<String> getStockTradeNames(StockTradeList stockTradeList) {
		List<String> nameList = new ArrayList<String>();
		for (StockTrade trade:stockTradeList.getStockTradeList()) {
			nameList.add(trade.getDesc());
		}
		return nameList;
	}
	
	private List<String> getStoremenNames(StoremanList storemenList) {
		List<String> names = new ArrayList<String>();
		if (storemenList != null) {
		    for (Storemen storemen:storemenList.getAllStoremen()) {
		    	names.add(storemen.getStoremanName()); 
		    }
		}
		return names;
	}
}