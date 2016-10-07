# IPS_Gimnasio
Implementación de un gimnasio para la asignatura de Ingeniería del Proceso del Software.

HISTORIA 1: <br/>
La aplicación consta de tres partes: 
* La primera encargada de descargar informacion de la base de datos: database.QueryExecutor
* La segunda es la GUI(Interfaz de Usuario) en ella se encuentra la parte gui.Main (a dividir en cuanto haya mas pantallas), esta cuenta con la ventana en la que se deberan escoger la instalacion para ver las reservas mediante un codigo de colores.
* La tercera es la abstraccion de las reservas que cuenta en primer lugar con una clase agenda que se encarga de guardar temporalmente las reservas descargadas de la bbdd y encapsuladas en un objeto Reservation(reservation.Reservation) ademas de esto y por facilitar la comparacion de tipos de instalaciones se ha incorporado un enumerador(reservation.Instalacion).
  
**Para ejecutar la aplicación es necesario tener la base de datos abierta en local**
  
