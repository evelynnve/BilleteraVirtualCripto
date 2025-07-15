![Compra 2](https://github.com/user-attachments/assets/46921b74-8184-46eb-8474-adfd455370c7)Proyecto para la materia Taller de Lenguajes II - 2024 - Facultad de Informática - UNLP

<img width="613" height="362" alt="image" src="https://github.com/user-attachments/assets/b985353e-8985-44c0-9af2-07143abb05e9" />

1.Introducción

El objetivo del Entregable 3 es una nueva iteración donde incorporaremos los elementos que completarán nuestra aplicación: Interfaz Gráfica (GUI), manejo de Excepciones, Entrada/Salida de archivos y manejo de Concurrencia (threads). 


2. Base de Datos
Realizaremos algunas modificaciones en la estructura de la Base de Datos. El diagrama y código que se presenta a continuación es una propuesta, que puede modificarse para adaptarlo a  su proyecto. Note que no aparece la tabla BILLETERA, esto ocurre para simplificar la estructura, ya que podemos asociar los activos al usuario. En caso que prefiera modelar la BILLETERA, puede agregarla. 
Diagrama Entidad-Relación (ER) de la Base de Datos:

<img width="598" height="326" alt="image" src="https://github.com/user-attachments/assets/7202cd54-0470-4b45-b1b7-4842332bb0ad" />


3. Funcionalidades del Sistema
   
En líneas generales estas son las funcionalidades principales. 

   a. Login de usuario usando simplemente su e-mail y password.

   ![39630e40-ecdc-40ed-bbe0-6f5d5aa7f37c](https://github.com/user-attachments/assets/23f2ec85-4849-4e50-b2f1-72701777147d)

   b. Registración de un usuario. Los datos solicitados son los mínimos necesarios. No se contempla verificaciones a excepción de la cuenta de email, que no puede estar asociada a otro usuario, y la aceptación de términos y condiciones.

   ![Registro](https://github.com/user-attachments/assets/5bd263ac-d7a7-494d-b9c9-8fb333438432)

   c. Visualización del Balance y  Mis Activos, con posibilidad de exportación en archivo con formato “valores separado por coma” (CSV).

   ![Mis activos](https://github.com/user-attachments/assets/bfc925b3-5410-46bd-bfac-c8e665c7921a)

   d. Visualización de las Transacciones realizadas en el sistema.

   ![Mis operaciones](https://github.com/user-attachments/assets/f05a77fd-9793-495f-8519-f7bf27a58b83)

   e. Visualización de Cotizaciones de las Criptomonedas, cuyo valor se actualiza en segundo plano.

   ![Cotizaciones](https://github.com/user-attachments/assets/d7aff341-d104-4ac6-b812-ff8ab7ecd587)

   f. Compra de Criptomonedas.

   ![Compra 1](https://github.com/user-attachments/assets/facd932b-90f5-4302-9f59-3942610bda23)

   ![Compra 3](https://github.com/user-attachments/assets/76061a2b-60ed-4632-bfd6-4973abeb8546)

   g. Swap de Criptomonedas.

   ![Swap 1](https://github.com/user-attachments/assets/649643ee-b134-48a6-a226-33fd677f409c)

   ![Swap 2](https://github.com/user-attachments/assets/d4a5e63a-285c-4ed3-a43f-c88374ee6511)

4. Prototipo 
Aquí está disponible un ejemplo de un boceto de la aplicación en la que es posible observar el flujo entre las diferentes pantallas. Tenga en cuenta que a excepción de lo remarcado como comentarios, ud. puede realizar ajustes, siempre que amplíen y no reduzcan el alcance de la entrega. Lea todos los comentarios incluidos en el diagrama.

https://drive.google.com/file/d/1VeIrWCp7IO2zTUYDXPEFODtKga0D3iyZ/view

5. De la Implementación del Prototipo

A. La implementación de la aplicación sigue el patrón MVC.
B. Se crearon 3 excepciones propias.
C. Respecto de la obtención de la cotización actual de las monedas, puede adaptar el siguiente código. Tenga en cuenta que para usarlo necesitará incorporar la librería JSON a su aplicación.
IMPORTANTE: la obtención de estas cotizaciones no puede bloquear el funcionamiento de la aplicación. 

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject; // Necesita agregar la librería org.json para trabajar con JSON
public class ConsultarPrecioCripto {
   private static final String URL_API = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,usd-coin,tether,dogecoin&vs_currencies=usd";
   public static void main(String[] args) {
       HttpClient cliente = HttpClient.newHttpClient();
       HttpRequest solicitud = HttpRequest.newBuilder()
