package cloud.krzysztofkin.inventoryapp2;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import cloud.krzysztofkin.inventoryapp2.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Adapter for listView
     **/
    BookCursorAdapter cursorAdapter;

    /**
     * Identifier for the book data loader
     */
    private static final int BOOK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO FAB floating action button
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //TODO on item list click
        //TODO sell button click
        ListView bookListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_list_view);
        bookListView.setEmptyView(emptyView);
        cursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(cursorAdapter);
        // Kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        values.put(BookEntry.COLUMN_BOOK_NAME, "A book");
        values.put(BookEntry.COLUMN_BOOK_PRICE, 7);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 2);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER, "A supplier");
        values.put(BookEntry.COLUMN_BOOK_PHONE, "77777");
        //use content resolver to add data
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}






