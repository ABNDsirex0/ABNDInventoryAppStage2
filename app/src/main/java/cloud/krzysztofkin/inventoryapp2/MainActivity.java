package cloud.krzysztofkin.inventoryapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cloud.krzysztofkin.inventoryapp2.data.BookContract.BookEntry;
import cloud.krzysztofkin.inventoryapp2.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    BookDbHelper mDbHelper;
    TextView tableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

    }
}






