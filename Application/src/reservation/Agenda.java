package reservation;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import database.QueryExecutor;


public class Agenda {
	Map<Integer,List<Reservation>> reservations;
	int currentWeek;
	private Calendar calendar;
	private QueryExecutor queryExecutor;
	
	public Agenda(){		
		reservations = new  HashMap<Integer,List<Reservation>>();	
		currentWeek = 0;
		calendar = Calendar.getInstance();
		queryExecutor = new QueryExecutor();
		loadCurrentWeek();
	}
	
	
	
	public void oneWeekMore(){
		currentWeek++;
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		loadCurrentWeek();
	}
	
	public void oneWeekLess(){
		currentWeek--;
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		loadCurrentWeek();
	}


	private void loadCurrentWeek() {
		if(!reservations.containsKey(currentWeek)){//Si no esta en el diccionario entonces bajatelo de la base de datos
			Calendar auxCalendar = (Calendar) calendar.clone();
			
			int month = calendar.get(Calendar.MONTH)+1;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2;
			int year = calendar.get(Calendar.YEAR);
			
			auxCalendar.add(Calendar.DAY_OF_MONTH, (dayOfWeek)*-1);//Empiece de la semana
			int dayOfWeekBegin = auxCalendar.get(Calendar.DAY_OF_MONTH);
			auxCalendar.add(Calendar.DAY_OF_MONTH, 7);//Finalizacion de la semana
			int dayOfWeekEnd = auxCalendar.get(Calendar.DAY_OF_MONTH)-1;
			
			
			//Construccion de strings para casar los estandares de DATE en SQL
			String monthStr = (month<10)?"0"+month:month+"";						
			String dayOfWeekBeginStr = (dayOfWeekBegin<10)?"0"+dayOfWeekBegin:dayOfWeekBegin+"";				
			String dayOfWeekEndStr = (dayOfWeekEnd<10)?"0"+dayOfWeekEnd:dayOfWeekEnd+"";		
			
			
			String query = "SELECT * FROM reserva NATURAL JOIN instalacion NATURAL JOIN usuario NATURAL JOIN socio"+
					" WHERE fecha >"+"'"+year+"-"+monthStr+"-"+dayOfWeekBeginStr+" 00:00:00'"
					+"AND fecha <"+"'"+year+"-"+monthStr+"-"+dayOfWeekEndStr+" 23:59:59'";
			
			try {
				ResultSet rs = queryExecutor.execute(query);
				loadReservations(rs,false);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			query = "SELECT * FROM reserva NATURAL JOIN instalacion NATURAL JOIN usuario NATURAL JOIN administrador"+
					" WHERE fecha >"+"'"+year+"-"+monthStr+"-"+dayOfWeekBeginStr+" 00:00:00'"
					+"AND fecha <"+"'"+year+"-"+monthStr+"-"+dayOfWeekEndStr+" 23:59:59'";
			
			try {
				ResultSet rs = queryExecutor.execute(query);
				loadReservations(rs,true);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
		}
	}

	


	
		
	private void loadReservations(ResultSet rs, boolean administrador) {
		List<Reservation> auxReservations;
		if(reservations.containsKey(currentWeek))
			auxReservations = reservations.get(currentWeek);
		else
			auxReservations = new ArrayList<Reservation>();
		try {
			while(rs.next()){
				Calendar auxCalendar = Calendar.getInstance();
				auxCalendar.setTime(rs.getTimestamp("fecha"));
				auxReservations.add(new Reservation(auxCalendar,
						rs.getString("nombre_instalacion"),administrador));
				}					
		} catch (SQLException e) {e.printStackTrace();}
		
		reservations.put(currentWeek, auxReservations);
		
	}
	
	public List<Reservation> getReservations(){		
		return reservations.get(currentWeek);
	}
	
	public Calendar getCalendar() {
		return calendar;
	}

}



