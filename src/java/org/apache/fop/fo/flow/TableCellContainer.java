/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.fo.flow;

import java.util.List;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.ValidationException;

/**
 * A common class for fo:table-body and fo:table-row which both can contain fo:table-cell.
 */
public abstract class TableCellContainer extends TableFObj implements ColumnNumberManagerHolder {

    /**
     * Used for determining initial values for column-numbers
     * in case of row-spanning cells
     * (for clarity)
     *
     */
    static class PendingSpan {

        /**
         * member variable holding the number of rows left
         */
        protected int rowsLeft;

        /**
         * Constructor
         * 
         * @param rows  number of rows spanned
         */
        public PendingSpan(int rows) {
            rowsLeft = rows;
        }
    }

    protected List pendingSpans;

    protected ColumnNumberManager columnNumberManager;

    public TableCellContainer(FONode parent) {
        super(parent);
    }

    protected void addTableCellChild(TableCell cell, boolean firstRow) throws FOPException {
        int colNumber = cell.getColumnNumber();
        int colSpan = cell.getNumberColumnsSpanned();
        int rowSpan = cell.getNumberRowsSpanned();

        Table t = getTable();
        if (t.hasExplicitColumns() && colNumber + colSpan - 1 > t.getNumberOfColumns()) {
            throw new ValidationException(FONode.errorText(locator) + "column-number or number "
                    + "of cells in the row overflows the number of fo:table-column specified "
                    + "for the table.");
        }
        if (firstRow) {
            handleCellWidth(cell, colNumber, colSpan);
            updatePendingSpansSize(cell, colNumber, colSpan);
        }

        /* if the current cell spans more than one row,
         * update pending span list for the next row
         */
        if (rowSpan > 1) {
            for (int i = 0; i < colSpan; i++) {
                pendingSpans.set(colNumber - 1 + i, new PendingSpan(rowSpan));
            }
        }

        columnNumberManager.signalUsedColumnNumbers(colNumber, colNumber + colSpan - 1);
    }

    private void handleCellWidth(TableCell cell, int colNumber, int colSpan) throws FOPException {
        Table t = getTable();
        Length colWidth = null;

        if (cell.getWidth().getEnum() != EN_AUTO
                && colSpan == 1) {
            colWidth = cell.getWidth();
        }

        for (int i = colNumber; i < colNumber + colSpan; ++i) {
            TableColumn col = t.getColumn(i - 1);
            if (col == null) {
                t.addDefaultColumn(colWidth,
                        i == colNumber
                            ? cell.getColumnNumber()
                            : 0);
            } else {
                if (!col.isDefaultColumn()
                        && colWidth != null) {
                    col.setColumnWidth(colWidth);
                }
            }
        }
    }

    private void updatePendingSpansSize(TableCell cell, int colNumber, int colSpan) {
        while (pendingSpans.size() < colNumber + colSpan - 1) {
            pendingSpans.add(null);
        }
    }

    /** {@inheritDoc} */
    public ColumnNumberManager getColumnNumberManager() {
        return columnNumberManager;
    }

}
