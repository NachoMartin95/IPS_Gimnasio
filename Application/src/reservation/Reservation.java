package reservation;


import java.util.Calendar;

public class Reservation {
	
	private Calendar date;
	private Instalacion instalacion;
	private boolean administracion;
	



	public Reservation(Calendar date, String instalacion, boolean administracion) {
		this.date = date;
		setInstalacion(instalacion);
		this.administracion = administracion;
	}


	
	public int getDay() {return date.get(Calendar.DAY_OF_MONTH)-1;}
	public int getMonth() {	return date.get(Calendar.MONTH)+1;}
	public int getYear() {return date.get(Calendar.YEAR);}
	public int getHour() {return date.get(Calendar.HOUR_OF_DAY);}
	public int getWeekDay(){return date.get(Calendar.DAY_OF_WEEK)-2;}
	public Instalacion getInstalacion() {return instalacion;}
	
	
	public void setInstalacion(String instalacion) {
		//Logica para introducir el enum correcto
		switch(instalacion.toLowerCase()){
			case "gimnasio":
				this.instalacion = Instalacion.GIMNASIO;
				break;
			case "p futbol":
				this.instalacion = Instalacion.PISTAFUTBOL;
				break;
			case "p baloncesto":
				this.instalacion = Instalacion.PISTABALONCESTO;
				break;
		
		}
	}
	
	public boolean isAdministracion() {return administracion;}
	public void setAdministracion(boolean administracion) {this.administracion = administracion;}

	

}
