/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.util.ArrayList;

/**
 *
 * @author mfernandez
 */
public interface IPersistencia {

    void escribirPersonas(ArrayList<Persona> personas, String ruta);

    ArrayList<Persona> leerTodo(String ruta);

}
