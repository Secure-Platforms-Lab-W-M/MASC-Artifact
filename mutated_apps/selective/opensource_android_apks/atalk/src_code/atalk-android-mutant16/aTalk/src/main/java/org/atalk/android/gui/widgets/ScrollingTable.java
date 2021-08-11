/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

import org.atalk.android.R;

/**
 * Custom layout that handles fixes table header, by measuring maximum column widths in both header and table body. Then
 * synchronizes those maximum values in header and body columns widths.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class ScrollingTable extends LinearLayout
{
    /**
     * Create a new instance of <tt>ScrollingTable</tt>
     *
     * @param context the context
     */
    public ScrollingTable(Context context)
    {
        super(context);
    }

    /**
     * Creates a new instance of <tt>ScrollingTable</tt>.
     *
     * @param context the context
     * @param attrs the attribute set
     */
    public ScrollingTable(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        TableLayout header = findViewById(R.id.table_header);
        TableLayout body = findViewById(R.id.table_body);

        // Find max column widths
        int[] headerWidths = findMaxWidths(header);
        int[] bodyWidths = findMaxWidths(body);

        if (bodyWidths == null) {
            // Table is empty
            return;
        }

        int[] maxWidths = new int[bodyWidths.length];

        for (int i = 0; i < headerWidths.length; i++) {
            maxWidths[i] = Math.max(headerWidths[i], bodyWidths[i]);
        }

        // Set column widths to max values
        setColumnWidths(header, maxWidths);
        setColumnWidths(body, maxWidths);
    }

    /**
     * Finds maximum columns widths in given table layout.
     *
     * @param table table layout that will be examined for max column widths.
     * @return array of max columns widths for given table, it's length is equal to table's column count.
     */
    private int[] findMaxWidths(TableLayout table)
    {
        int[] colWidths = null;

        for (int rowNum = 0; rowNum < table.getChildCount(); rowNum++) {
            TableRow row = (TableRow) table.getChildAt(rowNum);

            if (colWidths == null)
                colWidths = new int[row.getChildCount()];

            for (int colNum = 0; colNum < row.getChildCount(); colNum++) {
                int cellWidth = row.getChildAt(colNum).getWidth();
                if (cellWidth > colWidths[colNum]) {
                    colWidths[colNum] = cellWidth;
                }
            }
        }

        return colWidths;
    }

    /**
     * Adjust given table columns width to sizes given in <tt>widths</tt> array.
     *
     * @param table the table layout which columns will be adjusted
     * @param widths array of columns widths to set
     */
    private void setColumnWidths(TableLayout table, int[] widths)
    {
        for (int rowNum = 0; rowNum < table.getChildCount(); rowNum++) {
            TableRow row = (TableRow) table.getChildAt(rowNum);

            for (int colNum = 0; colNum < row.getChildCount(); colNum++) {
                View column = row.getChildAt(colNum);
                TableRow.LayoutParams params = (TableRow.LayoutParams) column.getLayoutParams();
                params.width = widths[colNum];
            }
        }
    }
}
