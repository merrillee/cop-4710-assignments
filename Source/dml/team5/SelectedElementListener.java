
package dml.team5;

import dml.team5.antlr.PLSQLBaseListener;
import dml.team5.antlr.PLSQLParser;

/**
 * @author Matthew Boyette (N00868808@ospreys.unf.edu)
 * @version 1.1
 * 
 *          This helper class walks the {@link org.antlr.v4.runtime.tree.ParseTree} to construct the master SELECTED_ELEMENTS {@link java.util.List}.
 */
public class SelectedElementListener extends PLSQLBaseListener
{
    /**
     * Constructs an instance of {@link dml.team5.SelectedElementListener}.
     * 
     * @since 1.1
     */
    public SelectedElementListener()
    {
        Utility.SELECTED_ELEMENTS.clear();
    }

    /**
     * Handle "SELECT ELEMENT_LIST FROM TABLE_LIST" queries.
     * Two possibilities exist: either the selected element is fully qualified and includes its table name, or it isn't and it doesn't.
     * 
     * @param ctx
     * @see dml.team5.antlr.PLSQLBaseListener#enterSelected_element(dml.team5.antlr.PLSQLParser.Selected_elementContext)
     * @since 1.1
     */
    @Override
    public void enterSelected_element(final PLSQLParser.Selected_elementContext ctx)
    {
        String tableName = "", columnName = "", columnLabel;

        try
        {
            columnLabel = ctx.column_alias().id().getText().trim();
        }
        catch ( final NullPointerException npe )
        {
            columnLabel = "";
        }

        try
        {
            // If it's fully qualified, just extract the table name and the attribute name.
            if ( ctx.select_list_elements().getText().trim().contains(".") )
            {
                String[] sel_elem = ctx.select_list_elements().getText().trim().split("\\.", 2);
                tableName = sel_elem[0].trim();
                columnName = sel_elem[1].trim();
            }
            // If it's not fully qualified, then we have to do some additional shenanigans.
            else
            {
                // Figure out columnName
                columnName = ctx.select_list_elements().getText().trim();
            }
        }
        catch ( final NullPointerException npe )
        {
            tableName = "";
            columnName = "";
        }

        if ( columnLabel.isEmpty() )
        {
            Utility.SELECTED_ELEMENTS.add(new SelectedElement(tableName, columnName));
        }
        else
        {
            Utility.SELECTED_ELEMENTS.add(new SelectedElement(tableName, columnName, columnLabel));
        }
    }
}