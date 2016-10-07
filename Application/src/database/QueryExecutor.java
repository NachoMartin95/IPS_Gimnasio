package database;
import java.sql.*;
import java.util.concurrent.*;



public class QueryExecutor {
	
	static Connection conn;
	static Statement stat;
	
	public QueryExecutor(){
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:hsql://127.0.0.1:9001/DS_gimnasio","SA","");
			stat = conn.createStatement();
		} catch (SQLException e) {
			System.err.println("SQL Exception " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that executes a given query in parallel in order not to interfere with the main GUI thread
	 * 
	 * @param query
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public ResultSet execute(String query) throws InterruptedException, ExecutionException{		
		Callable<ResultSet> task = () -> {
			return stat.executeQuery(query);
		};
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<ResultSet> future = executor.submit(task);
		return future.get();	
	}
}
