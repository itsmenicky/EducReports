package net.proteanit.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class DbUtils {
    public static TableModel resultSetToTableModel(ResultSet rs) {
	try {
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numberOfColumns = metaData.getColumnCount();
	    Vector<String> columnNames = new Vector<String>();

	    // Get the column names
	    for (int column = 0; column < numberOfColumns; column++) {
		columnNames.addElement(metaData.getColumnLabel(column + 1));
	    }

	    // Get all rows.
	    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	    while (rs.next()) {
		Vector<Object> newRow = new Vector<Object>();

		for (int i = 1; i <= numberOfColumns; i++) {
		    newRow.addElement(rs.getObject(i));
		}

		rows.addElement(newRow);
	    }

	    return new DefaultTableModel(rows, columnNames);
	} catch (Exception e) {
	    e.printStackTrace();

	    return null;
	}
    }

    public static List<List<Object>> resultSetToNestedList(ResultSet rs, boolean includeColumnNames) {
	try {
	    // To contain all rows.
	    List<List<Object>> rows = new ArrayList<List<Object>>();
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numberOfColumns = metaData.getColumnCount();

	    // Include column headers as first row if required
	    if (includeColumnNames) {
		List<Object> columnNames = new ArrayList<Object>();

		// Get the column names
		for (int column = 0; column < numberOfColumns; column++) {
		    columnNames.add(metaData.getColumnLabel(column + 1));
		}
		rows.add(columnNames);
	    }

	    // Get the data
	    while (rs.next()) {
		List<Object> newRow = new ArrayList<Object>();

		for (int i = 1; i <= numberOfColumns; i++) {
		    newRow.add(rs.getObject(i));
		}

		rows.add(newRow);
	    }
	    return rows;
	} catch (Exception e) {
	    e.printStackTrace();

	    return null;
	}
    }

    public static List<List<Object>> resultSetToNestedList(ResultSet rs) {
	try {
	    // To contain all rows.
	    List<List<Object>> rows = new ArrayList<List<Object>>();
	    ResultSetMetaData metaData = rs.getMetaData();
	    int numberOfColumns = metaData.getColumnCount();

	    // Get the data
	    while (rs.next()) {
		List<Object> newRow = new ArrayList<Object>();

		for (int i = 1; i <= numberOfColumns; i++) {
		    newRow.add(rs.getObject(i));
		}

		rows.add(newRow);
	    }
	    return rows;
	} catch (Exception e) {
	    e.printStackTrace();

	    return null;
	}
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
	ResultSet rs = new DummySimplestPlusMetaData();
	TableModel tm = DbUtils.resultSetToTableModel(rs);
	System.out.println(tm.getValueAt(0, 0));
    }
}
