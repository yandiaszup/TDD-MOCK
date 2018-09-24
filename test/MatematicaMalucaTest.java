import br.com.caelum.leilao.dominio.MatematicaMaluca;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatematicaMalucaTest {

    @Test
    public void numeroMaiorQue30(){
        MatematicaMaluca a = new MatematicaMaluca();
        int b = a.contaMaluca(32);
        assertEquals(128,b);
    }

    @Test
    public void numeroEntre30E10(){
        MatematicaMaluca a = new MatematicaMaluca();
        int b = a.contaMaluca(20);
        assertEquals(60,b);
    }

    @Test
    public void numeroMenorQue10(){
        MatematicaMaluca a = new MatematicaMaluca();
        int b = a.contaMaluca(3);
        assertEquals(6,b);
    }
}
