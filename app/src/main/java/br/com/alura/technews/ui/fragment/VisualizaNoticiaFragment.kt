package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.alura.technews.R
import br.com.alura.technews.databinding.VisualizaNoticiaBinding
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.NOTICIA_ID_CHAVE
import br.com.alura.technews.ui.viewmodel.VisualizaNoticiaViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


private const val NOTICIA_NAO_ENCONTRADA = "Notícia não encontrada"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover notícia"

class VisualizaNoticiaFragment : Fragment() {

    private lateinit var binding: VisualizaNoticiaBinding

    private val noticiaId: Long by lazy {
        //Método específico para receber arguments enviados para o fragment a partir do Bundle
        requireArguments().getLong(NOTICIA_ID_CHAVE)
    }

    private val viewModel: VisualizaNoticiaViewModel by viewModel {
        parametersOf(noticiaId)
    }

    //Função para acionar o comportamento de transição de tela por meio da Activity
    var quandoSelecionaMenuEdicao: (noticia: Noticia) -> Unit = {}

    //função para acionar o "finish()" por meio da Activity
    var quandoFinalizaTela: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //implementação para ativar o uso de options menu no fragment
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VisualizaNoticiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaNoticiaSelecionada()
        verificaIdDaNoticia()

    }

    private fun buscaNoticiaSelecionada() {
        viewModel.noticiaEncontrada.observe(viewLifecycleOwner, Observer { noticiaEncontrada ->
            noticiaEncontrada?.let {
                preencheCampos(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.visualiza_noticia_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.visualiza_noticia_menu_edita -> {

                viewModel.noticiaEncontrada.value?.let {quandoSelecionaMenuEdicao}
            }
            R.id.visualiza_noticia_menu_remove -> remove()
        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun verificaIdDaNoticia() {
        if (noticiaId == 0L) {
            mostraErro(NOTICIA_NAO_ENCONTRADA)
            quandoFinalizaTela()
        }
    }

    private fun preencheCampos(noticia: Noticia) {
        binding.visualizaNoticiaTitulo.text = noticia.titulo
        binding.visualizaNoticiaTexto.text = noticia.texto
    }

    private fun remove() {
        viewModel.remove().observe(this, Observer {
            if (it.erro == null) {
                quandoFinalizaTela()
            } else {
                mostraErro(MENSAGEM_FALHA_REMOCAO)
            }
        })
    }
}