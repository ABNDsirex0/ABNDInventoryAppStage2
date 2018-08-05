package cloud.krzysztofkin.inventoryapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

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
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
