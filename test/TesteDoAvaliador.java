
import br.com.caelum.leilao.dominio.Avaliador;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

import builder.CriadorDeLeilao;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TesteDoAvaliador {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario jose;
    private Usuario maria;

    @BeforeClass
    public static void testandoBeforeClass() {
        System.out.println("before class");
    }

    @AfterClass
    public static void testandoAfterClass() {
        System.out.println("after class");
    }

    @Before
    public void criaAvaliador() {
        this.leiloeiro = new Avaliador();
        System.out.println("Inicializando teste!");
        this.joao = new Usuario("João");
        this.jose = new Usuario("jose");
        this.maria = new Usuario("maria");
    }

    @After
    public void finaliza() {
        System.out.println("fim");
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {

        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();
        leiloeiro.avalia(leilao);

    }
    @Test
    public void deveEntenderLancesEmOrdemCrescente (){


        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 250.0));
        leilao.propoe(new Lance(jose, 400.0));
        leilao.propoe(new Lance(maria, 350.0));

        // parte 2 : acao
        leiloeiro.avalia(leilao);

        // parte 3 : validação
        double maiorEsperado =  400.0;
        double menorEsperado = 250.0;
        double avgEsperado = 333.3333333333333;
        assertEquals(maiorEsperado, leiloeiro.getMaiorLance(),0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(),0.00001);
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance (){

        // parte 1 : cenário
        Usuario joao = new Usuario("João");
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 1000.0));

        // parte 2 : acao
        leiloeiro.avalia(leilao);

        // parte 3 : validação
        double maiorEsperado =  1000.0;
        double menorEsperado = 1000.0;
        double avgEsperado = 1000.0;
        assertEquals(maiorEsperado, leiloeiro.getMaiorLance(),0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(),0.00001);
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica(){
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,450.0));
        leilao.propoe(new Lance(joao,120.0));
        leilao.propoe(new Lance(maria,700.0));
        leilao.propoe(new Lance(joao,630.0));
        leilao.propoe(new Lance(maria,230.0));

        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
    }



    @Test
    public void deveEncontrarOsTresMaioresLances(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao,100.0)
                .lance(maria,200.0)
                .lance(joao,300.0)
                .lance(maria,400.0)
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(3,maiores.size());
        assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
        assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
        assertEquals(200.0, maiores.get(2).getValor(), 0.00001);
    }


    @Test
    public void deveEncontrarOsTresMaioresLances2Lances(){
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,500.00));
        leilao.propoe(new Lance(maria,400.00));

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(2,maiores.size());
        assertEquals(500.00,maiores.get(0).getValor(),0.000001);
        assertEquals(400.00,maiores.get(1).getValor(),0.000001);
    }

    @Test
    public void deveEncontrarOsTresMaioresLances0Lances(){
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(0,maiores.size());
    }
}
