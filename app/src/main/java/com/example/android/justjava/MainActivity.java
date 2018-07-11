package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhipppedCram = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText nameField = (EditText) findViewById(R.id.name_field);
        String getName = nameField.getText().toString();

        if (getName.isEmpty()) {
            String text = getString(R.string.enterName);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return;
        }
        int price = calculatePrice(hasWhipppedCram, hasChocolate);

        //displayMessage(createOrderSummary(price, hasWhipppedCram, hasChocolate, getName));

        String subject = "Just Java order for " + getName;
        String mail = createOrderSummary(price, hasWhipppedCram, hasChocolate, getName);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, mail);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity is the number of cups of coffee ordered
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int price = 5;
        if (addWhippedCream)
            price++;
        if (addChocolate)
            price += 2;
        return price * quantity;
    }

//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//    }

    public void increment(View view) {
        quantity++;
        if (quantity > 100) {
            quantity = 100;
            String text = getString(R.string.maxCoffee);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);

    }

    public void decrement(View view) {
        quantity--;
        if (quantity < 1) {
            quantity = 1;
            String text = getString(R.string.minCoffee);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }

    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_wc, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_c, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_q, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_t, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}