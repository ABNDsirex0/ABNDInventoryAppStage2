package cloud.krzysztofkin.inventoryapp2;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cloud.krzysztofkin.inventoryapp2.data.BookContract.BookEntry;
import cloud.krzysztofkin.inventoryapp2.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO obsługa listview z adapterem

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO czyszczenie bazy
        switch (item.getItemId()){
            case R.id.action_insert_sample_data:
                insertSampleBook();
                return true;
            case R.id.action_deleta_all_data:
                deleteAllData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Deleta all data from database
     */
    private void deleteAllData() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
    }

    /**
     * Insert sample data to database
     */
    private void insertSampleBook() {
        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME,"A book");
        values.put(BookEntry.COLUMN_BOOK_PRICE,7);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY,2);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER,"A supplier");
        values.put(BookEntry.COLUMN_BOOK_PHONE,"77777");
        //use content resolver to add data
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }
}






