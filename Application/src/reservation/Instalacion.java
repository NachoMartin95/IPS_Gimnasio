package reservation;

public enum Instalacion {
	GIMNASIO,PISTAFUTBOL,PISTABALONCESTO;
	//TEST RECORDS
	
	@Override
	public String toString(){
		switch(this){
		case GIMNASIO:
			return "Gimnasio";
		case PISTAFUTBOL:
			return "Pista de Futbol";
		case PISTABALONCESTO:
			return "Pista de Baloncesto";
		default:
			return "Enum Instalaciones";
		}
	}
}
