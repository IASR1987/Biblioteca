package com.IASR1987.Bibliotecario;

public class Borrador {
	/*
    //accedemos a la coleccion libro
    MongoCollection<Document>Catalogo = Biblioteca.getCollection("libro");
	
    //listar todos los elementos de una lista
    FindIterable<Document> libros = Catalogo.find();
    
    //mostrar por pantalla
    for (Document libro : libros) {
        System.out.println(libro.toJson());
    }
    /*
    //insertar un documento
    InsertOneResult nuevoLibro = Catalogo.insertOne(new Document()
    		.append("titulo", "El camino")
    		.append("autor", "Miguel Delibes")
    		.append("prestado", "false")
    		);
    */
    /*
    //creamos la lista de Documentos
    ArrayList<Document> nuevosLibros = new ArrayList<Document>();
    
    //creamos los nuevos objetos y los introducimos en el arraysList
    Document libro1 = new Document("titulo", "Viaje al Centro de la Tierra")
            .append("autor", "Julio Verne")
            .append("prestado", "false");

	Document libro2 = new Document("titulo", "Los pilares de la Tierra")
	                    .append("autor", "Ken Follett")
	                    .append("prestado", "false");
	
	Document libro3 = new Document("titulo", "La Celestina")
	                    .append("autor", "Fernando de Rojas")
	                    .append("prestado", "false");
    		
    //agregamos al arrays
    nuevosLibros.add(libro1);
    nuevosLibros.add(libro2);
    nuevosLibros.add(libro3);
	
    //los insertamos
    InsertManyResult AnadirLibros = Catalogo.insertMany(nuevosLibros);
    */
    /*
    //modificar un atributo de un documento
    Catalogo.updateOne(Filters.eq("titulo", "Viaje al Centro de la Tierra"), new Document("$set", new Document("prestado", "true")));
    
    //eliminar un elemento
    Catalogo.deleteOne(Filters.eq("titulo", "Viaje al Centro de la Tierra"));
    */
}
