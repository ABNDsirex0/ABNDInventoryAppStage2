package cloud.krzysztofkin.inventoryapp2;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cloud.krzysztofkin.inventoryapp2.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri currentBookUri;

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;

    TextView nameTextView;
    TextView priceTextView;
    TextView quantityTextView;
    TextView supplierTextView;
    TextView phoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        nameTextView = findViewById(R.id.name_EditText);
        priceTextView = findViewById(R.id.price_EditText);
        quantityTextView = findViewById(R.id.quantity_EditText);
        supplierTextView = findViewById(R.id.supplier_name_EditText);
        phoneTextView = findViewById(R.id.phone_EditText);


        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        currentBookUri = intent.getData();
        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (currentBookUri == null) {
            // This is a new pet, so change the app bar to say "Add a Book"
            setTitle(getString(R.string.title_new_book));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing pet, so change app bar to say "Edit Book"
            setTitle(getString(R.string.title_edit_book));

            // Initialize a loader to read the pet data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteBook();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteBook() {
        int rowsDeleted = 0;
        if (currentBookUri != null) {
            rowsDeleted = getContentResolver().delete(currentBookUri, null, null);
        }
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.delete_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.delete_book_successful),
                    Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void phoneIntent(View view) {
        String phoneNumber = "tel:"+phoneTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the book table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER,
                BookEntry.COLUMN_BOOK_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentBookUri,         // Query the content URI for the current book
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);


            // Update the views on the screen with the values from the database

            nameTextView.setText(name);
            priceTextView.setText(Integer.toString(price));
            quantityTextView.setText(Integer.toString(quantity));
            supplierTextView.setText(supplier);
            phoneTextView.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameTextView.setText("");
        priceTextView.setText("");
        quantityTextView.setText("");
        supplierTextView.setText("");
        phoneTextView.setText("");
    }

    public void quantityLessMoreClick(View view) {
        String quantityText = quantityTextView.getText().toString();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException nfe ){
            quantity = 0;
        }

        switch (view.getId()) {
            case R.id.less_button:
                if (quantity > 0) {
                    quantityTextView.setText(Integer.toString(quantity - 1));
                }
                break;
            case R.id.more_button:
                quantityTextView.setText(Integer.toString(quantity + 1));
                break;
        }
    }
}
