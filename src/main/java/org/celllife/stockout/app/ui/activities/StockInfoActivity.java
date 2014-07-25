package org.celllife.stockout.app.ui.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.R;
import org.celllife.stockout.app.domain.Drug;
import org.celllife.stockout.app.domain.StockHistory;
import org.celllife.stockout.app.domain.StockReceived;
import org.celllife.stockout.app.domain.StockTake;
import org.celllife.stockout.app.manager.CalculationManager;
import org.celllife.stockout.app.manager.DrugManager;
import org.celllife.stockout.app.manager.ManagerFactory;
import org.celllife.stockout.app.manager.StockTakeManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** 
 * This activity displays information about a specific drug (dates and quantities of last stock take/stock received)
 */
public class StockInfoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stock_info);
     
        TextView drugNameTextView = (TextView) findViewById(R.id.drugstock_name);
        drugNameTextView.setText(getIntent().getExtras().getString("Drug_name"));

        TextView drugStockTakeDateTextView = (TextView) findViewById(R.id.laststock_date);
        drugStockTakeDateTextView.setText(lastStockTakeDate());

        TextView drugStockReceivedDateTextView = (TextView) findViewById(R.id.stockreceived_date);
        drugStockReceivedDateTextView.setText(lastStockReceivedDate());

        TextView drugStockTakeQuantityTextView = (TextView) findViewById(R.id.laststocklvl_text);
        drugStockTakeQuantityTextView.setText(lastStockTakeQuantity());

        TextView drugStockADCTextView = (TextView) findViewById(R.id.avgdaily_text);
        drugStockADCTextView.setText(drugAverageDailyConsumption());

        TextView drugEstimatedStockTextView = (TextView) findViewById(R.id.eststock_text);
        if (drugEstimatedStock() != Integer.MAX_VALUE) {
            drugEstimatedStockTextView.setText(Integer.toString(drugEstimatedStock()));
        } else {
            drugEstimatedStockTextView.setText(noStockString());
        }

        TextView drugScannedStockTextView = (TextView) findViewById(R.id.stocklvlscan_text);
        drugScannedStockTextView.setText(stockReceivedQuantity());

        Button backButton = (Button) findViewById(R.id.cancel_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockInfoActivity.this.finish();

            }
        });
    }

    //Get the drug that match the barcode sent from the DrugStockList in StockFragment
    public Drug getDrug(String barcode) {
        DrugManager drugManager = ManagerFactory.getDrugManager();
        List<Drug> drugs = drugManager.getDrugs();
        Drug drug = new Drug();
        String drugBarcode = "";
        for (int i = 0; i < drugs.size(); i++) {
            drugBarcode = drugs.get(i).getBarcode();
            if (drugBarcode.equals(barcode)) {
                drug = drugs.get(i);
            }
        }
        return drug;
    }
    
    //Get the barcode of the drug  sent from the DrugStockList in StockFragment
    private String getDrugBarcode() {
        String drugBarcode = getIntent().getExtras().getString("Drug_barcode");
        return drugBarcode;
    }
    
    //Get the date of the last stocktake of the drug
    private String lastStockTakeDate() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
        StockTake stockTake = stockTakeManager.getDrugLastStockTake(d);
        if (stockTake != null) {
            Date drugStockTakeDate = stockTake.getDate();
            return formatDate(drugStockTakeDate);
        } else {
            return noDateString();
        }
    }
    
  //Get the date of the last time stock for the drug was received
    private String lastStockReceivedDate() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
        StockReceived stocRecieved = stockTakeManager.getDrugLastStockReceived(d);
        if (stocRecieved != null) {
            Date drugStockReceivedDate = stocRecieved.getDate();
            return formatDate(drugStockReceivedDate);
        } else {
            return noDateString();
        }
    }
    
    //Get the quantity that was captured at the last stocktake of the drug 
    private String lastStockTakeQuantity() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
        StockTake stockTake = stockTakeManager.getDrugLastStockTake(d);
        if (stockTake != null) {
            String drugStockQuantity = stockTake.getQuantity().toString();
            return drugStockQuantity;
        } else {
            return noStockString();
        }
    }
    
    //Get the average daily consumption of the drug
    private String drugAverageDailyConsumption() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
        StockHistory stockHistory = stockTakeManager.getDrugStockHistory(d);
        if (stockHistory != null) {
            String averageDailyConsumption = stockHistory.getAverageDailyConsumption().toString();
            return averageDailyConsumption;
        } else {
            return noStockString();
        }
    }
    
    //Get the estimated stock of the drug
    private int drugEstimatedStock() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        CalculationManager calcManager = ManagerFactory.getCalculationManager();
        int estimatedStock = calcManager.getEstimatedStock(d);
        return estimatedStock;
    }
    
    //Get the quantity that was captured the last time stock was received for the drug
    private String stockReceivedQuantity() {
        String barcode = getDrugBarcode();
        Drug d = getDrug(barcode);
        StockTakeManager stockTakeManager = ManagerFactory.getStockTakeManager();
        StockReceived stocRecieved = stockTakeManager.getDrugLastStockReceived(d);
        if (stocRecieved != null) {
            String drugStockReceived = stocRecieved.getQuantity().toString();
            return drugStockReceived;
        } else {
            return noStockString();
        }
    }
    
    //Format the date(eg 25 May 2014)
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String formatedDate = sdf.format(date);
        return formatedDate;
    }
    
    //String to display for units(quantity) when no stocktake/stockreceive was done
    private String noStockString() {
        String noStock = getApplicationContext().getString(R.string.no_stock);
        return noStock;
    }
    
    //String to display for date when no stocktake/stockreceive was done
    private String noDateString() {
        String noDate = getApplicationContext().getString(R.string.no_date);
        return noDate;
    }

}
