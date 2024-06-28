package net.proteanit.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class DummySimplestPlusMetaData extends DummySimplest {

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new DummySimplestMetaData();
	}

	private class DummySimplestMetaData implements ResultSetMetaData {

		public String getCatalogName(int column) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnClassName(int column) throws SQLException {
			return "java.lang.String";
		}

		public int getColumnCount() throws SQLException {
			return 1;
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
			if (column != 1) {
				throw new SQLException("Index can only be 1 for this metadata");
			}

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

}
