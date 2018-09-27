package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.Repositorio.Carteiro;
import br.com.caelum.leilao.Repositorio.EnviadorDeEmail;
import br.com.caelum.leilao.Repositorio.RepositorioDeLeiloes;
import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class EncerradorDeLeilaoTest {

    EnviadorDeEmail enviadorDeEmail;
    RepositorioDeLeiloes daoFalso;
    Carteiro carteirofalso;
    Leilao leilao1;
    Leilao leilao2;
    List<Leilao> leilosAntigos;
    EncerradorDeLeilao encerrador;

    @Before
    public void geramocks(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999,1,20);

        enviadorDeEmail = mock(EnviadorDeEmail.class);
        daoFalso = mock(RepositorioDeLeiloes.class);
        carteirofalso = mock(Carteiro.class);

        leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
        leilosAntigos = Arrays.asList(leilao1,leilao2);

        when(daoFalso.correntes()).thenReturn(leilosAntigos);
        encerrador = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerrador.encerra();

    }

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes(){
        assertEquals(2, encerrador.getTotalEncerrados());
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoEncerraLeilaoComMenosDeUmaSemana(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(2018,9,24);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        List<Leilao> leilosAntigos = Arrays.asList(leilao1);
        EnviadorDeEmail enviadorDeEmail = mock(EnviadorDeEmail.class);
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        Carteiro carteirofalso = mock(Carteiro.class);
        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
    }

    @Test
    public void naoEncerraLeilãoSeNaoTiver(){

        when(daoFalso.correntes()).thenReturn(new ArrayList<>());
        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerrador.encerra();

        assertEquals(0,encerrador.getTotalEncerrados());
    }

    @Test
    public void deveAtualizarLeiloesEncerrados(){
        // Verifica se o metodo atualiza foi chamado, times(n) -> diz quantas vezes o metodo deve ser chamado para o teste passa
        verify(daoFalso,times(1)).atualiza(leilao1);
    }

    @Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {

        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
                .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira")
                .naData(ontem).constroi();

        EnviadorDeEmail enviadorDeEmail = mock(EnviadorDeEmail.class);
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        Carteiro carteirofalso = mock(Carteiro.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());

        // verifys aqui
        verify(daoFalso,never()).atualiza(leilao1);
        verify(daoFalso,never()).atualiza(leilao2);
    }

    @Test
    public void leilaoEnviadoPorEmailNaOrdemCerta(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999,1,20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        EnviadorDeEmail enviadorDeEmail = mock(EnviadorDeEmail.class);
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        Carteiro carteirofalso = mock(Carteiro.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerrador.encerra();

        InOrder inOrder = inOrder(daoFalso, carteirofalso);
        inOrder.verify(daoFalso).atualiza(leilao1);
        inOrder.verify(carteirofalso).envia(leilao1);
    }

    @Test
    public void deveContinuarAExecucaoMesmoQuandoDaoFalha(){

        Calendar antiga = Calendar.getInstance();
        antiga.set(1999,1,20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        EnviadorDeEmail enviadorDeEmail = mock(EnviadorDeEmail.class);
        Carteiro carteirofalso = mock(Carteiro.class);

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1,leilao2));
        doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class)); // Simula o Lançamento de exeçoes
        EncerradorDeLeilao encerradorDeLeilao = new EncerradorDeLeilao(daoFalso,carteirofalso);
        encerradorDeLeilao.encerra();

        verify(carteirofalso, never()).envia(any(Leilao.class)); // any serve para nao precisar repetindo

    }

}
