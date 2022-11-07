/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author mfernandez
 */
public class RandomAccessPersistencia implements IPersistencia {

    private static final int LONG_BYTES_PERSONA = 35 + Persona.MAX_LENGTH_NOMBRE * 2;
    private static final int OFFSET_BORRADO = 1; 
    private static final int OFFSET_SALARIO = 4;
  

 



    @Override
    public void escribirPersonas(ArrayList<Persona> personas, String ruta) {
        long longitudBytes = 0;

        if (personas != null) {
            try (
                     RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {

                longitudBytes = raf.length();
                raf.seek(longitudBytes);
                for (Persona persona : personas) {

                    raf.writeLong(persona.getId());
                    StringBuilder sb = new StringBuilder(persona.getDni());
                    sb.setLength(Persona.MAX_LENGTH_DNI);
                    raf.writeChars(sb.toString());

                    sb = new StringBuilder(persona.getNombre());
                    sb.setLength(Persona.MAX_LENGTH_NOMBRE);
                    raf.writeChars(sb.toString());

                    raf.writeInt(persona.getEdad());
                    raf.writeFloat(persona.getSalario());

                    raf.writeBoolean(persona.isBorrado());

                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            }
        }

    }
@Override
    public ArrayList<Persona> leerTodo(String ruta) {
        long id;
        String dni = "", nombre = "";
        int edad;
        float salario;
        StringBuilder sb =null;
        Persona persona = null;
        boolean borrado = false;
        ArrayList<Persona> personas = new ArrayList<>();
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "r");) {

            do {
                id = raf.readLong();
                sb = new StringBuilder();
                for (int i = 0; i <= (Persona.MAX_LENGTH_DNI -1); i++) {
                    sb.append(raf.readChar());
                }

                dni = sb.toString();

                sb = new StringBuilder();
                for (int i = 0; i < Persona.MAX_LENGTH_NOMBRE; i++) {
                    sb.append(raf.readChar());
                }

                nombre = sb.toString();

                edad = raf.readInt();
                salario = raf.readFloat();

                borrado = raf.readBoolean();

                persona = new Persona(id, dni, edad, salario, nombre);
                persona.setBorrado(borrado);

                personas.add(persona);

            } while (raf.getFilePointer() < raf.length());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return personas;

    }

    public Persona leerPersona(int posicion, String ruta) {
        long id = 0;
        String dni = "", nombre = "";
        int edad = 0;
        float salario = 0;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;
        boolean borrado = false;

        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "r");) {

            raf.seek(converToBytePosition(posicion));
            id = raf.readLong();
            for (int i = 0; i <= (Persona.MAX_LENGTH_DNI -1); i++) {
                sb.append(raf.readChar());
            }

            dni = sb.toString();
            sb = new StringBuilder();
            for (int i = 0; i < Persona.MAX_LENGTH_NOMBRE; i++) {
                sb.append(raf.readChar());
            }

            nombre = sb.toString();

            edad = raf.readInt();
            salario = raf.readFloat();

            borrado = raf.readBoolean();

            persona = new Persona(id, dni, edad, salario, nombre);
            persona.setBorrado(borrado);

        } catch (EOFException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }

        return persona;
    }

    private long converToBytePosition(int posicion) {
        if (posicion == 0) {
            return posicion;
        } else {
            return LONG_BYTES_PERSONA * (posicion - 1);
        }
    }

    public Persona add(int posicion, String ruta, Persona persona) {
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {

            raf.seek(converToBytePosition(posicion));

            raf.writeLong(persona.getId());
            StringBuilder sb = new StringBuilder(persona.getDni());
            sb.setLength(Persona.MAX_LENGTH_DNI);
            raf.writeChars(sb.toString());
           

            sb = new StringBuilder(persona.getNombre());
            sb.setLength(Persona.MAX_LENGTH_NOMBRE);
            raf.writeChars(sb.toString());

            raf.writeInt(persona.getEdad());
            raf.writeFloat(persona.getSalario());
            raf.writeBoolean(persona.isBorrado());

        } catch (FileNotFoundException ex) {
            persona = null;
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        } catch (IOException ex) {
            persona = null;
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return persona;
    }

    public float sumarSalario(int posicion, String ruta, float incremento) {

        float salario = 0;
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {

            raf.seek(posicion * LONG_BYTES_PERSONA - OFFSET_BORRADO - OFFSET_SALARIO);
//Nos posicionamos al final de la persona que ocupa la posicion que viene por parámetro
            salario = raf.readFloat();

            salario += incremento;
            raf.seek(raf.getFilePointer() - OFFSET_SALARIO);
            raf.writeFloat(salario);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return salario;
    }

    public boolean borrar(int posicion, String ruta, boolean borrado) {
        boolean exito = false;
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {

            raf.seek(posicion * LONG_BYTES_PERSONA - OFFSET_BORRADO);
            raf.writeBoolean(borrado);

            exito = true;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return exito;
    }
}
