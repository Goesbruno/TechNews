package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentOnAttachListener
import androidx.fragment.app.FragmentTransaction
import br.com.alura.technews.R
import br.com.alura.technews.databinding.ActivityNoticiasBinding
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment


private const val TAG_FRAGMENT_VISUALIZA_NOTICIA = "visualizaNoticia"

class NoticiasActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var binding: ActivityNoticiasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticiasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configuraFragmentPeloEstado(savedInstanceState)
        configuraListenerDosFragments()
    }

    //Verificação do savedInstanceState para garantir que a transação de fragment
    // só ocorrerá caso seja a primeira execução do onCreate
    private fun configuraFragmentPeloEstado(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            abreListaNoticias()
        } else {
            tentaReabrirFragmentVisualizaNoticia()
        }
    }

    private fun tentaReabrirFragmentVisualizaNoticia() {
        //Encontrando o fragment atual pelo ID
        supportFragmentManager
            .findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTICIA)?.let { fragment ->
                val argumentos = fragment.arguments
                val novoFragment = VisualizaNoticiaFragment()
                //Os argumentos do fragment serão copiados e colocados em um novo fragment igual
                // pois não é possível reaproveitar o mesmo fragment em um container diferente
                novoFragment.arguments = argumentos

                //Transação exclusiva para remover o fragment antigo e evitar sobreposição
                removeFragmentVisualizaNoticia(fragment)
                //acionando a backstack para fazer com que o fragment de lista apareça ao
                // rotacionar a tela estando na tela de visualização de noticia
                supportFragmentManager.popBackStack()

                //Transação que irá criar o novo fragment no container primario
                transacaoFragment {

                    val container = configuraContainerFragmentVisualizaNoticia()
                    //Um novo fragment identico ao anterior é criado porém em um container diferente
                    replace(container, novoFragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
                }

            }
    }

    //Verificação para definir qual layout será usado na transação
    private fun FragmentTransaction.configuraContainerFragmentVisualizaNoticia() =
        if (binding.activityNoticiasContainerSecundario != null) {
            binding.activityNoticiasContainerSecundario!!.id

        } else {
            //addToBackStack adiciona o fragment anterior a pilha de retorno
            addToBackStack(null)
            binding.activityNoticiasContainerPrimario!!.id

        }


    private fun abreListaNoticias() {
        transacaoFragment {
            add(R.id.activity_noticias_container_primario, ListaNoticiasFragment())
        }
    }

    //Responsável por configurar os comportamentos de listener dos fragments utilizados
    private fun configuraListenerDosFragments() {

        val listener = FragmentOnAttachListener { fragmentManager, fragment ->
            when (fragment) {
                is ListaNoticiasFragment -> {
                    configuraListaNoticias(fragment)
                }

                is VisualizaNoticiaFragment -> {
                    configuraVisualizaNoticias(fragment)
                }
            }
        }
        fragmentManager.addFragmentOnAttachListener(listener)
    }

    //Comportamentos específicos do fragment VisualizaNoticia
    private fun configuraVisualizaNoticias(fragment: VisualizaNoticiaFragment) {
        fragment.quandoFinalizaTela = {
            supportFragmentManager
                .findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTICIA)?.let { fragment ->
                    removeFragmentVisualizaNoticia(fragment)
                }
        }
        fragment.quandoSelecionaMenuEdicao = { noticiaSelecionada ->
            abreFormularioEdicao(noticiaSelecionada)
        }
    }

    private fun removeFragmentVisualizaNoticia(fragment: Fragment) {
        transacaoFragment {
            remove(fragment)
        }
    }

    //Comportamentos específicos do fragment ListaNoticias
    private fun configuraListaNoticias(fragment: ListaNoticiasFragment) {
        fragment.quandoNoticiaSeleciona = this::abreVisualizadorNoticia
        fragment.quandoFabSalvaNoticiaClicado = this::abreFormularioModoCriacao
    }


    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    //Reponsável por substituir o fragment inicial pelo VisualizaNoticias
    private fun abreVisualizadorNoticia(noticia: Noticia) {
        val fragment = VisualizaNoticiaFragment()
        //Criando um instância de Bundle para conter os argumentos que devem ser enviados
        val bundle = Bundle()
        bundle.putLong(NOTICIA_ID_CHAVE, noticia.id)
        //Enviando o bundle para o fragment a partir dos seus arguments
        fragment.arguments = bundle

        transacaoFragment {
            //Verificação para definir qual layout será usado na transação
            val container = configuraContainerFragmentVisualizaNoticia()
            replace(container, fragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
        }
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }
}
