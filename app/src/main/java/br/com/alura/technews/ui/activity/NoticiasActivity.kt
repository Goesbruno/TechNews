package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment


private const val TITULO_APPBAR = "Notícias"

class NoticiasActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        title = TITULO_APPBAR
        configuraFragmentInicial()
        configuraListenerDosFragments()
    }


    private fun configuraFragmentInicial() {
        transacaoFragment {
            add(R.id.activity_noticias_container, ListaNoticiasFragment())
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
        fragment.quandoFinalizaTela = this::finish
        fragment.quandoSelecionaMenuEdicao = { noticiaSelecionada ->
            abreFormularioEdicao(noticiaSelecionada)
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
            replace(R.id.activity_noticias_container, fragment)
        }
    }



    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }
}
