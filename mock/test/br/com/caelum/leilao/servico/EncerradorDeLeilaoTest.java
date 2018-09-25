package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.Repositorio.RepositorioDeLeiloes;
import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import br.com.caelum.leilao.infra.dao.LeilaoDaoFalso;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EncerradorDeLeilaoTest {

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999,1,20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
        List<Leilao> leilosAntigos = Arrays.asList(leilao1,leilao2);
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

        when(daoFalso.correntes()).thenReturn(leilosAntigos);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

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
        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
    }

    @Test
    public void naoEncerraLeilãoSeNaoTiver(){

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(new ArrayList<>());

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);
        encerrador.encerra();

        assertEquals(0,encerrador.getTotalEncerrados());
    }

}
