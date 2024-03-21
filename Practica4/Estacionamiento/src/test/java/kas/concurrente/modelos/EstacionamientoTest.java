package kas.concurrente.modelos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstacionamientoTest {
    Estacionamiento es;
    final static int NUMLUGARES = 200;
    List<Thread> hilos;

    @BeforeEach
    void setUp(){
        es = new Estacionamiento(NUMLUGARES, 1); // Pasamos 1 como número de pisos para la prueba
        initHilos();
    }

    /**
     * Teste que revisa si tiene todos los lugares disponibles al iniciar
     */
    @Test
    void lugaresDisponiblesITest(){
        assertEquals(NUMLUGARES,es.getLugaresDisponibles());
    }

    /**
     * Test que comprueba que el numero de veces que se estaciona sea correcto
     * @throws InterruptedException
     */
    @Test
    void conteoVecesEstacionado() throws InterruptedException{
        for(int i = 0; i < NUMLUGARES; i++){
            es.entraCarro(i); // Simulamos la entrada de un carro
        }
        assertEquals(NUMLUGARES, verificaVecesEstacionado());
    }


    @Test
    void conteoGlobalVecesEstacionado() throws InterruptedException {
        for (Thread t : hilos) {
            t.start();
        }
    
        for (Thread t : hilos) {
            t.join();
        }
    
        assertEquals(NUMLUGARES * 2, verificaVecesEstacionado());
    }
    
    public int verificaVecesEstacionado() {
        int res = 0;
        for (int i = 0; i < es.getLugares().length; ++i) {
            for (int j = 0; j < es.getLugares()[0].length; j++) {
                res += es.getLugares()[i][j].getVecesEstacionado();
            }
        }
        return res;
    }

    /**
     * AGREGA 2 TEST MAS
     * TEST bien hechos
     */

    void initHilos(){
        hilos = new ArrayList<>();

        for(int i=0; i < NUMLUGARES*2; ++i){
            Thread t = new Thread(this::simulaCS,""+i);
            hilos.add(t);
        }
    }

    void simulaCS() {
        try{
            int id = Integer.parseInt(Thread.currentThread().getName());
            es.entraCarro(id);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
    }
}
