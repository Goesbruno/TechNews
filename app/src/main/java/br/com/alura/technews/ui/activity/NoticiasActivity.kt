package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment


private const val TITULO_APPBAR = "Notícias"

class NoticiasActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        title = TITULO_APPBAR
        configuraFragment()
        configuraListenerDoFragment()
    }

    private fun configuraListenerDoFragment() {
        val listener = FragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is ListaNoticiasFragment) {
                fragment.quandoNoticiaSeleciona = {abreVisualizadorNoticia(it)}
                fragment.quandoFabSalvaNoticiaClicado = {abreFormularioModoCriacao()}
            }
            if (fragment is VisualizaNoticiaFragment) {
                fragment.quandoFinalizaTela = { finish() }
                fragment.quandoSelecionaMenuEdicao = {noticiaSelecionada ->
                    abreFormularioEdicao(noticiaSelecionada)}
            }
        }
        fragmentManager.addFragmentOnAttachListener(listener)
    }

    private fun configuraFragment() {
        val fragmentTrasaction = fragmentManager.beginTransaction()

        fragmentTrasaction.add(R.id.activity_noticias_container, ListaNoticiasFragment())
        fragmentTrasaction.commit()
    }


    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = VisualizaNoticiaFragment()
        //Criando um instância de Bundle para conter os argumentos que devem ser enviados
        val bundle = Bundle()
        bundle.putLong(NOTICIA_ID_CHAVE, noticia.id)
        //Enviando o bundle para o fragment a partir dos seus arguments
        fragment.arguments = bundle
        fragmentTransaction.replace(
            R.id.activity_noticias_container,
            fragment
        )
        fragmentTransaction.commit()
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }
}
