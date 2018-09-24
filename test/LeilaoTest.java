import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeilaoTest {
    @Test
    public void deveReceberUmLance(){
        Leilao leilao = new Leilao("Macbook pro 15");
        assertEquals(0,leilao.getLances().size());

        leilao.propoe(new Lance(new Usuario("Stev Jobs"), 2000));

        assertEquals(1,leilao.getLances().size());
        assertEquals(2000.00,leilao.getLances().get(0).getValor(),0.00001);
    }

    @Test
    public void deveReceberVariosLances(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        leilao.propoe(new Lance(new Usuario("Stev Jobs"), 2000));
        leilao.propoe(new Lance(new Usuario("Stev wozniak"), 3000));

        assertEquals(2,leilao.getLances().size());
        assertEquals(2000.0,leilao.getLances().get(0).getValor(),0.00001);
        assertEquals(3000.0,leilao.getLances().get(1).getValor(),0.00001);
    }

    @Test
    public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve jobs");

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(steveJobs, 3000.0));

        assertEquals(1,leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(),0.00001);
    }

    @Test
    public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve jobs");
        Usuario billgates = new Usuario("Bill gates");

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(billgates, 3000.0));

        leilao.propoe(new Lance(steveJobs, 4000.0));
        leilao.propoe(new Lance(billgates, 5000.0));

        leilao.propoe(new Lance(steveJobs, 6000.0));
        leilao.propoe(new Lance(billgates, 7000.0));

        leilao.propoe(new Lance(steveJobs, 8000.0));
        leilao.propoe(new Lance(billgates, 9000.0));

        leilao.propoe(new Lance(steveJobs, 10000.0));
        leilao.propoe(new Lance(billgates, 11000.0));

        //deve ser ignorado
        leilao.propoe(new Lance(steveJobs, 12000.0));

        assertEquals(10,leilao.getLances().size());
        assertEquals(11000.0, leilao.getLances().get(leilao.getLances().size()-1).getValor(),0.00001);

    }

    @Test
    public void dobraMaiorValor(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve jobs");
        Usuario billgates = new Usuario("Bill gates");

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(billgates, 3000.0));

        leilao.propoe(new Lance(steveJobs, 4000.0));
        leilao.propoe(new Lance(billgates, 5000.0));

        leilao.propoe(new Lance(steveJobs, 6000.0));
        leilao.propoe(new Lance(billgates, 7000.0));

        leilao.propoe(new Lance(steveJobs, 8000.0));
        leilao.propoe(new Lance(billgates, 9000.0));

        leilao.dobraLance(steveJobs);
        assertEquals(9,leilao.getLances().size());
        assertEquals(16000.0,leilao.getLances().get(8).getValor(),0.00001);
    }
}
