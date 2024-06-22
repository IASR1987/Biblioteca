package com.IASR1987.Bibliotecario;



import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.tools.FileObject;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

public class App 
{
    public static void main( String[] args )
    {
    	Scanner teclado = new Scanner(System.in);
    	String opcion;//control del switch
    	
    	try {
    		//creamos la cadena de conexión
    		ConnectionString newConexion = new ConnectionString("mongodb://localhost:27017");
    		
    		//nos conectamos a MongoDB, creando un cliente de conexión
    		MongoClient Ismael = MongoClients.create(newConexion);
    		
    		//nos conectamos a la base de datos
            MongoDatabase Biblioteca = Ismael.getDatabase("Biblioteca");
            
            //accedemos a las colecciones
            //accedemos a la coleccion libro
            MongoCollection<Document>Catalogo = Biblioteca.getCollection("libro");
            MongoCollection<Document>Usuarios = Biblioteca.getCollection("usuario");
            MongoCollection<Document>Prestamos = Biblioteca.getCollection("prestamos");
            
            //creamos Listas de los documentos para utilizarlos despúes
            //listar todos los elementos de una lista
            FindIterable<Document> catalogoLibros = Catalogo.find();
            FindIterable<Document> listaUsuarios = Usuarios.find();
            FindIterable<Document> registroPrestamos = Prestamos.find();
            
            do {
            	
            
            System.out.println("------------------MENÚ-------------------");
            System.out.println("1.Añadir Libro.");
            System.out.println("2.Borrar Libro.");
            System.out.println("3.Añadir Usuario.");
            System.out.println("4.Borrar Usuario.");
            System.out.println("5.Realizar un Prestamo.");
            System.out.println("6.Realizar una Devolución.");
            System.out.println("7.Listar todos los libros");
            System.out.println("8.Ordenar por orden alfabético los libros.");
            System.out.println("9.Ordenar por libros disponibles.");
            System.out.println("10.Ordenar por los libros prestados.");
            System.out.println("11.Listar Usuarios.");
            System.out.println("12.Listar los libros leidos de un usuario concreto.");
            System.out.println("13. Prestamos de un Usuario");
            System.out.println("14.Abandonando Bibliotecario.");
            
            System.out.print("Seleccione una opción: ");
            opcion = teclado.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("Has seleccionado Añadir Libro.");
                                        
                    //Tenemos que obtener el título y el autor por variables
                    System.out.println("Introduce el título del libro");
                    String newTitulo= teclado.nextLine();
                    System.out.println("Introduce el autor del libro");
                    String autor= teclado.nextLine();
                    
                    //insertamos el nuevoLibro
                    InsertOneResult nuevoLibro = Catalogo.insertOne(new Document()
                    		.append("titulo", newTitulo)
                    		.append("autor", autor)
                    		.append("prestado", false)//con comillas guardamos un String, sin comillas es boolean
                    		);
                    
                    break;
                    
                case "2":
                    System.out.println("Has seleccionado Borrar Libro.");
                                        
                    //titulo del libro a eliminar
                    System.out.println("Introduce el título del libro");
                    String tituloEliminado= teclado.nextLine();
                    
                    //eliminamos el libro
                    Catalogo.deleteOne(Filters.eq("titulo", tituloEliminado));//filtro que busca en el campo titulo, el libro que queremos eliminar

                    break;
                    
                case "3":
                    System.out.println("Has seleccionado Añadir Usuario.");
                  
                    
                    //Tenemos que obtener el nombre y el email por variables
                    System.out.println("Introduce nombre del nuevo Usuario");
                    String newUsuario= teclado.nextLine();
                    System.out.println("Introduce el mail");
                    String email= teclado.nextLine();
                    
                    //insertamos el nuevoLibro
                    InsertOneResult nuevoUsuario = Usuarios.insertOne(new Document()
                    		.append("nombre", newUsuario)
                    		.append("email", email)
                    		.append("LibrosLeidos", Arrays.asList())//añade un arrays vacio
                    		);
                    break;
                    
                case "4":
                    System.out.println("Has seleccionado Borrar Usuario.");
                                       
                    //nombre del usuario a eliminar a eliminar
                    System.out.println("Introduce el nombre del Usuario a eliminar");
                    String usuarioEliminado= teclado.nextLine();
                    
                    //eliminamos el usuario
                    Usuarios.deleteOne(Filters.eq("nombre", usuarioEliminado));//el filtro busca del campo nombre el que coincida con el que queremos eliminar
                    
                    break;
                    
                case "5":
                    System.out.println("Has seleccionado Realizar un Prestamo.");
                    
                    //Insertamos el usuario a buscar
                    System.out.println("Introduce el nombre del usuario");
                    String nombreUsuario=teclado.nextLine();
                               	                   
                     //creamos variables
                    boolean usuarioCorrecto=false;//indica si el usuario existe
                    ObjectId IdUsuario=null;//registra el id del usuario
                    boolean disponibilidad = false;//registra true o false cuando el titulo del libro buscado se encuentra en el documento
                    ObjectId idLibro=null;//registra el id del libro
                    String libroPrestamo = null;//libro introducido por el usuario
                    String titulo = null;//registremos el titulo del libro
                    
                    //recorremos la lista de usuarios, buscando alguna coincidencia
                    for(Document Usuario: listaUsuarios) {
                    	String usuarioBuscado = Usuario.getString("nombre");//guarda en el string el contenido del campo nombre
                    	
                    	//si coincide el nombre introducido por teclado con alguno de la BD, pasa la variable a true
                    	if(usuarioBuscado.equals(nombreUsuario)) {
                    		System.out.println("El usuario existe");
                    		usuarioCorrecto=true;//indica que el usuario existe
                    		IdUsuario=Usuario.getObjectId("_id");//insertamos en la variable el valor del objeto id
                       	}
                    }
                    
                    //continuamos con el proceso de prestamos
                    //true significa usuario registrado en la BD
                    if(usuarioCorrecto==true) {

                        System.out.println("Introduce el nombre del libro que se va a prestar");
                        libroPrestamo = teclado.nextLine();                     
                                                
                        //recorremos el catalogo de libros buscando un libro que coinicda con el introcido por teclado
                        for (Document libro : catalogoLibros) {
                            String tituloBuscado = libro.getString("titulo");
                            
                            //coincide el titulo del documento q se encuentra en el for con el título que hemos introducido por teclado
                            if(tituloBuscado.equals(libroPrestamo)) {
                            	//guardamos el valor prestado en una variable si es false, el libro no esta prestado
                            	disponibilidad = libro.getBoolean("prestado");
                            		
                            		//comprobamos si el libro esta prestado o no
    						      	if(disponibilidad==false) {
    						                						            
    						            //modificamos el valor de prestado a true
    						       		Catalogo.updateOne(Filters.eq("titulo", libroPrestamo),new Document("$set", new Document("prestado", true)));//true sin comillas al ser boolean
    						            
    						       		idLibro= libro.getObjectId("_id");//registramos el id del libro
    						       		titulo= libro.getString("titulo");//registramos el titulo
    						                						       		  						       		
    						       		//realizamos las modificaciones en la tabla prestamos
    						        	
    						            //insertar un documento
    						            LocalDate fechaActual = LocalDate.now(); // Obtiene la fecha y hora actual
    						            InsertOneResult nuevoPrestamo = Prestamos.insertOne(new Document()
    						            		.append("idLibro", idLibro)
    						            		.append("Título", titulo)
    						            		.append("Usuario", IdUsuario)
    						            		.append("Nombre", nombreUsuario)
    						            		.append("FechaPrestamo", fechaActual)
    						            		.append("FechaDevolucion", "------")
    						            		.append("Estado", "A préstamo")
    						            		);
    						       		
    						      }else {
    						    	  System.out.println("El libro está prestado");
    						      }
                            }
                        }	
                    }else {
                		System.out.println("Debes dar de alta al usuario");
                	}
                    
                    //añadimos el libro al arrays de libros del usuario
                    if(disponibilidad==false&&usuarioCorrecto==true) {//indica que el prestamo se ha realizado, ya que el usuario existe y el libro no se encontraba prestado
                           	
                    	//actualizamos usuarios añadiendole el libro que tiene en prestamo
                        Usuarios.updateOne(
                        		    Filters.eq("_id", IdUsuario),//filtro en el documento que id sea igual a idUsuario
                        		    Updates.push("LibrosLeidos", libroPrestamo)//push añade al final del arrays
                        		);
                    }
                    
                    
                    break;
                    
                case "6":
                    System.out.println("Has seleccionado Realizar una Devolución.");
                    
                    System.out.println("Introduce el libro a devolver");
                    String libroDevolucion= teclado.nextLine();
                                                             
                    //se recorre el arrays buscando coincidencias entre el catalogo y el libro introducido por teclado
                    for (Document libro : catalogoLibros) {
                    	String tituloBuscar = libro.getString("titulo");
                    	
                    	//si existe la coincidencia
                    	if(tituloBuscar.equals(libroDevolucion)) {
                    		
                    		//tenemos que identificar el id del libro;
                    		 ObjectId idLibroDevuelto = libro.getObjectId("_id");//id del libro devuelto
                    		
                    		//recorremos el arrays buscando los prestamos con el id del libro, puede exister varios prestamos
                    		//uno estará A préstamo, el resto seran antiguos pedidos
		                    for(Document d:registroPrestamos) {
		                    	ObjectId idObjeto= d.getObjectId("idLibro");//id del libro
		                    	
		                    	//dentro de los prestamos si el id del libro introducido coincide con el libro del documento pedido
		                    	// y el estado del prestamo es A préstamo pasamos a modificarlo
		                    	if(idObjeto.equals(idLibroDevuelto)&& d.getString("Estado").equals("A préstamo")) {
		                    		
						            LocalDate fechaActual = LocalDate.now(); // Obtiene la fecha y hora actual
						            
						            //actualizamos el estado del prestamo
						            //primero se modifica la hora, ya que el filtro utilizado lo modficaremos en el siguiente paso
						            Prestamos.updateOne(
		                        		    Filters.eq("Estado", "A préstamo"),
		                        		    Updates.set("FechaDevolucion", fechaActual)//push para los arrays set para los demas tipos
		                        		);
						            //modificamos el estado del documento
			                        Prestamos.updateOne(
			                        		    Filters.eq("Estado", "A préstamo"),
			                        		    Updates.set("Estado", "Devolución")//push para los arrays set para los demas tipos
			                        		);
			                        
			                        //actualizamos el estado del libro
			                        Catalogo.updateOne(
			                        			Filters.eq("_id", idLibroDevuelto),
			                        			Updates.set("prestado", false)//false indica que el libro esta disponible para ser prestado
			                        		);
		                    	}
		                    }
                    		
                    	}
                    }
                    
                    break;
                    
                case "7":
                    System.out.println("Listar todos los libros.");
                                   
                    //mostrar por pantalla
                    for (Document libro : catalogoLibros) {
                        String mostrarTitulo = libro.getString("titulo");
                        String mostrarAutor = libro.getString("autor");
                        
                        //asi listamos solo los campos títulos y autor
                        //System.out.println("Título = "+ mostrarTitulo + " Autor = " + mostrarAutor );
                        
                        // Formatea la salida para que los títulos y autores estén tabulados en dos columnas
                        String output = String.format("Título: %-40s Autor: %s", mostrarTitulo, mostrarAutor);//40s hace referencia a 40 espacios
                        
                        // Imprime la salida formateada
                        System.out.println(output);
                    }
                    
                    break;
                    
                case "8":
                	System.out.println("Ordenar por orden alfabético los libros");
                	                	
                    //listar todos los elementos de una lista ordenandolos
                    FindIterable<Document> librosOrdenados = Catalogo.find().sort(new Document("titulo",1));//entre parentesis el campo y el modo de ordenación
                    
                    //mostrar por pantalla
                    for (Document libro : librosOrdenados) {
                        String mostrarTitulo = libro.getString("titulo");
                        String mostrarAutor = libro.getString("autor");
                        
                        //asi listamos solo los campos títulos y autor
                        //System.out.println("Título = "+ mostrarTitulo + " Autor = " + mostrarAutor );
                        
                        // Formatea la salida para que los títulos y autores estén tabulados en dos columnas
                        String output = String.format("Título: %-40s Autor: %s", mostrarTitulo, mostrarAutor);//40s hace referencia a 40 espacios
                        
                        // Imprime la salida formateada
                        System.out.println(output);
                    }
                    
                    break;
                    
                case "9":
                	System.out.println("Listar por libros disponibles");
                    
                    boolean disponible;
                    
                    //mostrar por pantalla
                    System.out.println("Libros disponibles");
                    
                    //recorremos el arrays buscando los libros con el campo prestado en false que nos indica que el libro esta disponible a ser prestado
                    for (Document libro : catalogoLibros) {
                    	disponible = libro.getBoolean("prestado");
                    	
                    	if(disponible==false) {
                    		String mostrarTitulo = libro.getString("titulo");//guardamos titulo
                            String mostrarAutor = libro.getString("autor");//guardamos autor
                            
                            //asi listamos solo los campos títulos y autor
                            //System.out.println("Título = "+ mostrarTitulo + " Autor = " + mostrarAutor );
                            
                            // Formatea la salida para que los títulos y autores estén tabulados en dos columnas
                            String output = String.format("Título: %-40s Autor: %s", mostrarTitulo, mostrarAutor);//40s hace referencia a 40 espacios
                            
                            // Imprime la salida formateada
                            System.out.println(output);
                    	}
                    }
                    
                    break;
                
                case "10":
                	System.out.println("Listar por los libros prestados");
                                   
                    //mostrar por pantalla
                    System.out.println("Libros prestados a los usuarios");
                    
                    //recorremos el arrays buscando los libros que se encuentran prestado, campo prestado==true;
                    for (Document libro : catalogoLibros) {
                    	disponible = libro.getBoolean("prestado");
                    	
                    	if(disponible==true) {
                    		String mostrarTitulo = libro.getString("titulo");//guardamos el título
                            idLibro = libro.getObjectId("_id");//con el título buscamos el usuario que lo tiene
                            String mostrarUsuario = identificarUsuarioPrestamo(registroPrestamos, idLibro);//función que nos devuelve el usuario que tiene el libro
                            
                            //asi listamos solo los campos títulos y autor
                            //System.out.println("Título = "+ mostrarTitulo + " Autor = " + mostrarAutor );
                            
                            // Formatea la salida para que los títulos y autores estén tabulados en dos columnas
                            String output = String.format("Título: %-40s Usuario: %s", mostrarTitulo, mostrarUsuario);//40s hace referencia a 40 espacios
                            
                            // Imprime la salida formateada
                            System.out.println(output);
                    	}
                    }
                    
                    break;
                
                case "11":
                	System.out.println("Listar Usuarios");
                	
                	int contador = 1;//indice para los sysos
                	
                	//mostramos todos los usuarios de la BD
                	for(Document user: listaUsuarios) {
                		
                		System.out.println(contador +".- " +user.getString("nombre")+ ".");
                		contador ++;
                	}
                	
                	break;
                
                case "12":
                	System.out.println("Listar los libros leidos de un usuario concreto");
                	
                	System.out.println("Introduce el nombre del usuario a buscar");
                	String usuarioBus= teclado.nextLine();
                	
                	//buscamos en la listaUsuarios, alguna coincidencia con el nombre introducido por teclado
                	for(Document user: listaUsuarios) {
                		
                		//si existe la coincidencia
                		if(user.get("nombre").equals(usuarioBus)) {
                			
                			//guardmos la lista en una variable Lista para poder despúes mostrarla
                			List<String> LibrosLeidos=user.getList("LibrosLeidos", String.class);
                			
                			contador=1;
                			//se muestra la lista
                			for(String a : LibrosLeidos) {
                				System.out.println(contador+".- "+a+".");
                				contador ++;
                			}
                		}
                	}
                	
                	break;
                
                case "13":
                	System.out.println("Prestamos de un Usuario");
                	System.out.println("Introduce el nombre del usuario");
                	String nameUser= teclado.nextLine();
                	
                	ObjectId usuarioEncontrado= null; 
                	
                	//buscamos algun usuauario que coincida con el introducido por teclado
                	for(Document d: listaUsuarios) {
                		
                		//si existe la coincidencia, guardamos su id
                		if(d.get("nombre").equals(nameUser)) {
                			usuarioEncontrado = d.getObjectId("_id");//guardamos id en variable 
                		}
                	}
                	
                	//si es distinto de null es que existe el usuario
                	if(usuarioEncontrado!=null) {
                		System.out.println("Usuario : "+ nameUser+".");
                		contador =1;//para los sysos
                		
                		//pasamos por todos los prestamos buscando coincidencias
                		for(Document d: registroPrestamos) {
                			
                			//usando el compareTo, si es igual a =, son iguales los Id, mostrmos el contenido
                			if(usuarioEncontrado.compareTo(d.getObjectId("Usuario"))==0) {
                				System.out.println("--------------------------------");
	                    		System.out.println(contador+"º Libro.");
	                    		System.out.println("--------------------------------");
	                    		ObjectId idBook = d.getObjectId("idLibro");//obtenemos el id del Libro
	                    		System.out.println(tituloPorId(catalogoLibros,idBook));////usamos una función que nos devuelve el título
	                    		System.out.println(d.get("Estado"));// indicamos si esta A préstamo o es Devolcuión
	                    		contador++;
                			};
                    		
                    	}
                	}else {
                		System.out.println("Usuario no encontrado");
                	}
                break;
                case "14":
                	System.out.println("Abandonando Bibliotecario");
                break;
                default:
                    System.out.println("Opción no válida.");
            }
            
            }while(!opcion.equals("14"));
            
            System.out.println("Eso es todo Amigos");
            
            //cerramos nuestra conexión
            Ismael.close();
            //cerramos el Scanner
            teclado.close();
            
    	}catch(Exception e) {
    		//mensaje de error
    		System.out.println("Error al establecer la conexión");
    		//nos indicará el error
    		e.printStackTrace();
    	}
    	
    	
    }
    
    /**
     * Busca y devuelve el título de un libro dado su ObjectId.
     *
     * @param catalogoLibros Una colección iterable de documentos que representan el catálogo de libros.
     * @param idLibro El ObjectId del libro cuyo título se desea buscar.
     * @return El título del libro si se encuentra, de lo contrario, null.
     */
   private static String tituloPorId(FindIterable<Document> catalogoLibros,ObjectId idLibro) {
	   String retorno=null;
	   
	   for(Document d: catalogoLibros) {
		   if(idLibro.compareTo(d.getObjectId("_id"))==0) {
			   
			   retorno=d.getString("titulo");
			   
		   }
	   }
	
	   return retorno;
   }
   
   /**
    * Busca y devuelve el ObjectId de un libro dado su Título.
    *
    * @param catalogoLibros Una colección iterable de documentos que representan el catálogo de libros.
    * @param titulo String es el título del libro cuyo Id queremos buscar.
    * @return el ObjectId se encuentra, de lo contrario, null.
    */
   public static ObjectId idPorTitulo(FindIterable<Document> catalogoLibros, String titulo ) {
	   ObjectId retorno=null;
	   
	   for(Document d: catalogoLibros) {
		   
		   if(titulo.equals(d.getString("titulo"))){
			   retorno=d.getObjectId("_id");
		   }
	   }
	   
	   return retorno;
   }
   
   /**
    * Busca y devuelve el nombre de un Usuario dado su ObjectId.
    *
    * @param listaUsuarios Una colección iterable de documentos que representan a los usuarios.
    * @param idUsuario  es el ObjectId para el cual buscamos el nombre del usuario que le corresponde.
    * @return un String con elnombre del Usuario si se encuentra, de lo contrario, null.
    */
   public static String nombrePorId(FindIterable<Document> listaUsuarios, ObjectId idUsuario) {
	   String retorno=null;
	   
	   for(Document d: listaUsuarios) {
		   if(idUsuario.compareTo(d.getObjectId("_id"))==0) {
			   retorno=d.getString("nombre");
		   }
	   }
	   
	   return retorno;
   }
   
   /**
    * Busca y devuelve el nombre de un libro dado su ObjectId.
    *
    * @param registroPrestamos Una colección iterable de documentos que representan el registro de los prestamos.
    * @param idLibro ObjectId del libro que el usuario tiene en Prestamo y del cual queremos saber su nombre.
    * @return un String con el nombre del usuario que ha realizado un prestmoa con ese idLibro si se encuentra, de lo contrario, null.
    */
   public static String identificarUsuarioPrestamo(FindIterable<Document> registroPrestamos, ObjectId idLibro) {
	   String retorno=null;
	   
	   for(Document d: registroPrestamos) {
		   if(idLibro.equals(d.getObjectId("idLibro"))) {
			   retorno=d.getString("Nombre");
		   }
	   }
	   
	   return retorno;
   }
}
