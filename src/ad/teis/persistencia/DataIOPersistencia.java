/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static ad.teis.model.Persona.MAX_LENGTH_DNI;
import static ad.teis.model.Persona.MAX_LENGTH_NOMBRE;

/**
 *
 * @author mfernandez
 */
public class DataIOPersistencia implements IPersistencia {


    @java.lang.Override
    public void escribirPersonas(ArrayList<Persona> personas, String ruta) {
        try (FileOutputStream fos = new FileOutputStream(ruta); DataOutputStream dos = new DataOutputStream(fos)) {
            for (Persona p : personas) {
                dos.writeLong(p.getId());

                StringBuilder sbdni = new StringBuilder(p.getDni());
                sbdni.setLength(MAX_LENGTH_DNI);
                dos.writeChars(sbdni.toString());

                dos.writeInt(p.getEdad());

                dos.writeFloat(p.getSalario());

                StringBuilder sbnombre = new StringBuilder(p.getNombre());
                sbnombre.setLength(MAX_LENGTH_NOMBRE);
                dos.writeChars(sbnombre.toString());

                dos.writeBoolean(p.isBorrado());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @java.lang.Override
    public ArrayList<Persona> leerTodo(String ruta) {

        ArrayList<Persona> personas = new ArrayList();

        StringBuilder sbdni = new StringBuilder();
        StringBuilder sbnombre = new StringBuilder();

        try ( FileInputStream fis = new FileInputStream(ruta);  DataInputStream dis = new DataInputStream(fis)) {

            while (true) {

                //Leyendo ID
                long id = dis.readLong();

                //Leyendo DNI
                sbdni.delete(0, sbdni.length());
                for (int i = 0; i < Persona.MAX_LENGTH_DNI; i++) {
                    sbdni.append(dis.readChar());
                }

                //Leyendo edad
                int edad = dis.readInt();

                //Leyendo salario
                float salario = dis.readFloat();

                //Leyendo nombre
                sbnombre.delete(0, sbnombre.length());
                for (int i = 0; i < Persona.MAX_LENGTH_NOMBRE; i++) {
                    sbnombre.append(dis.readChar());
                }

                //Leyendo borrado
                boolean borrado = dis.readBoolean();

                //Instanciando persona
                Persona persona = new Persona(id, sbdni.toString(), edad, salario, sbnombre.toString());

                persona.setBorrado(borrado);

                //Añadiendo persona a lista de personas
                personas.add(persona);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personas;

    }
}
