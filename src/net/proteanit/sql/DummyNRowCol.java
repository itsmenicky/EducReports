package net.proteanit.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DummyNRowCol extends DummySimplest {
	private class DummySimplestMetaData implements ResultSetMetaData {

		public String getCatalogName(int column) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnClassName(int column) throws SQLException {
			return "java.lang.String";
		}

		public int getColumnCount() throws SQLException {
			return numCols;
		}

		public int getColumnDisplaySize(int column) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getColumnLabel(int column) throws SQLException {
			return String.format("%s%d", "Column", column);
		}

		public String getColumnName(int column) throws SQLException {
			return String.format("%s%d", "Column", column);
		}

		public int getColumnType(int column) throws SQLException {
			return Types.VARCHAR;
		}

		public String getColumnTypeName(int column) throws SQLException {
			return "VARCHAR";
		}

		public int getPrecision(int column) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getScale(int column) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getSchemaName(int column) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getTableName(int column) throws SQLException {
			if (column != 1) {
				throw new SQLException("Index can only be 1 for this metadata");
			}

			return "DummyTable";
		}

		public boolean isAutoIncrement(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isCaseSensitive(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isCurrency(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isDefinitelyWritable(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public int isNullable(int column) throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean isReadOnly(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isSearchable(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isSigned(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isWritable(int column) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

	}

	private List<List<String>> data;

	private int numCols;

	private int numRows;

	private int rowPointer = -1;
	
	/**
	 * @param numRows
	 *            How many rows do we want?
	 * @param numCols
	 *            How many cols do we want?
	 */
	public DummyNRowCol(int numRows, int numCols) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
		data = new ArrayList<List<String>>(numRows);
		fillData(data);
	}

	private void fillData(List<List<String>> data) {
		for (int i = 0; i < numRows; i++) {
			List<String> row = new ArrayList<String>(numCols);
			data.add(row);
			for (int j = 0; j < numCols; j++) {
				String val = Integer.toString(
						(int) (Math.random() * Integer.MAX_VALUE), 36);
				row.add(val);
			}

		}

	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new DummySimplestMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return data.get(rowPointer).get(columnIndex - 1);
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return data.get(rowPointer).get(columnIndex - 1);
	}

	@Override
	public boolean next() throws SQLException {
		rowPointer++;
		return rowPointer < numRows;
	}

}
