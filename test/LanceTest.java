import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class LanceTest {

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveAceitarValorMenorQueZero(){
        Usuario usuario = new Usuario("Yan");
        Lance lance = new Lance(usuario,-500.0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void naoDeveAceitarValorIgualAZero(){
        Usuario usuario = new Usuario("Yan");
        Lance lance = new Lance(usuario,0.0);
    }

    @Test
    public void aceitaValorMaiorQue0(){
        Usuario usuario = new Usuario("Yan");
        Lance lance = new Lance(usuario,500.0);
        Assert.assertEquals(500.00,lance.getValor(),0.00001);
    }
}
