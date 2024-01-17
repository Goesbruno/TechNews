package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.databinding.ListaNoticiasBinding
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.recyclerview.adapter.ListaNoticiasAdapter
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar as novas notícias"
class ListaNoticiasFragment : Fragment() {

    private lateinit var binding: ListaNoticiasBinding


    private val adapter by lazy {
        context?.let {
        ListaNoticiasAdapter(context = it)
        } ?: throw IllegalArgumentException("Contexto Inválido")
    }

    private val viewModel: ListaNoticiasViewModel by viewModel()
    var quandoFabSalvaNoticiaClicado: () -> Unit = {}
    var quandoNoticiaSeleciona: (noticia: Noticia) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNoticias()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListaNoticiasBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNoticia()
    }

    private fun configuraFabAdicionaNoticia() {
        binding.listaNoticiasFabSalvaNoticia.setOnClickListener {
            quandoFabSalvaNoticiaClicado()
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.listaNoticiasRecyclerview.addItemDecoration(divisor)
        binding.listaNoticiasRecyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = quandoNoticiaSeleciona
    }

    private fun buscaNoticias() {
        // No método observe o lifecycle Owner será a própria activity
        //O Observer irá checar se houve mudança na lista do LiveData e irá executar a atualização do adapter
        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let { mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS) }
        })
    }
}