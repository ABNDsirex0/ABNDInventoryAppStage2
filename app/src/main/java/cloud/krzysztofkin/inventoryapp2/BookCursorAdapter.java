package cloud.krzysztofkin.inventoryapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cloud.krzysztofkin.inventoryapp2.data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {
    /**
     * @param context
     * @param c
     * @deprecated
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.item_name);
        TextView priceTextView = view.findViewById(R.id.item_price);
        final TextView quantityTextView = view.findViewById(R.id.item_quantity);
        Button sellButton = view.findViewById(R.id.item_sell);

        int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);

        final int bookID = cursor.getInt(idColumnIndex);
        String bookName = cursor.getString(nameColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);
        int bookPrice = cursor.getInt(priceColumnIndex);

        nameTextView.setText(bookName);
        priceTextView.setText(Integer.toString(bookPrice));
        quantityTextView.setText(Integer.toString(bookQuantity));

        sellButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(bookQuantity>0){
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY,bookQuantity-1);

                    Uri currentPetUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, bookID);
                    context.getContentResolver().update(currentPetUri, values, null, null);
                }
            }
        });
    }
}
