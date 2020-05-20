package br.com.brunozava.tests.suiteexecution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.brunozava.tests.CenariosNegativosClientesTests;
import br.com.brunozava.tests.CenariosNegativosPagamentosTests;
import br.com.brunozava.tests.CenariosNegativosPedidosTests;
import br.com.brunozava.tests.PedidosBoletoTests;
import br.com.brunozava.tests.PedidosCartaoCancelledByAdquirenteTests;
import br.com.brunozava.tests.PedidosCartaoCancelledTests;
import br.com.brunozava.tests.PedidosCartaoInAnalysisTests;
import br.com.brunozava.tests.PedidosCartaoTests;
import br.com.brunozava.tests.PedidosDebitoBancarioTests;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	PedidosBoletoTests.class,
	PedidosDebitoBancarioTests.class,
	PedidosCartaoTests.class,
	PedidosCartaoInAnalysisTests.class,
	PedidosCartaoInAnalysisTests.class,
	PedidosCartaoCancelledTests.class,
	PedidosCartaoCancelledByAdquirenteTests.class,
	CenariosNegativosClientesTests.class,
	CenariosNegativosPedidosTests.class,
	CenariosNegativosPagamentosTests.class
	
})
public class Suite {

}
