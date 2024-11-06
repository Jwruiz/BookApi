package com.jeheremicurso.proyectoLibreria.principal;

import com.jeheremicurso.proyectoLibreria.model.Datos;
import com.jeheremicurso.proyectoLibreria.model.DatosAutor;
import com.jeheremicurso.proyectoLibreria.model.DatosLibros;
import com.jeheremicurso.proyectoLibreria.service.ConsumoApi;
import com.jeheremicurso.proyectoLibreria.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoAPI = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu(){
        
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        // libros mas descargados
        System.out.println("Top 10, libros mas descargados: ");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDescargas).reversed())
                .limit(10)
                .map(l-> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //busqueda por nombre
        System.out.println("Ingresa el nombre del libro que deseas encontrar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l-> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado...");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("Libro no encontrado");
        }

        //Calculo de estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d-> d.numeroDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDescargas));
        System.out.println("Cantidad promedio de descargas: " + est.getAverage());
        System.out.println("Cantidad maxima de descargas: " + est.getMax());
        System.out.println("Cantidad minima de descargas: " + est.getMin());
        System.out.println("Cantidad de registrar evaluados para el calculo: " + est.getCount());

        //Filtrado de libros con autores del siglo XXI
        System.out.println("Ingresa el año desde el cual quieres consultar");
        var fechaLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?author_year_end=" + fechaLibro);
        var resultadosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Stream<DatosLibros> fechaBuscada = resultadosBusqueda.resultados().stream();
                //.filter(a -> a.() >= Integer.parseInt(fechaLibro));
        fechaBuscada.forEach(System.out::println);









    }

}
